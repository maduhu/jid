package com.sos.eventing.eventhandler;

import java.io.File;
import java.io.StringReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import sos.scheduler.command.SOSSchedulerCommand;
import sos.scheduler.job.JobSchedulerConstants;
import sos.util.SOSDate;

public class SOSEvaluateEvents {

    private LinkedHashSet<SOSActions> listOfActions;
    private LinkedHashSet<SchedulerEvent> listOfActiveEvents;
    private String errmsg;
    private String host;
    private int port;
    private Document activeEvents = null;

    private static Logger logger = Logger.getLogger(SOSEvaluateEvents.class);

    public SOSEvaluateEvents(final String host_, final int port_) {
        super();

        host = host_;
        port = port_;
        listOfActiveEvents = new LinkedHashSet<SchedulerEvent>();

    }

    public void reconnect(final String host_, final int port_) {
        host = host_;
        port = port_;
        listOfActiveEvents = new LinkedHashSet<SchedulerEvent>();
    }

    public String getEventStatus(final SchedulerEvent event) {
        String erg = "missing";
        Iterator<SchedulerEvent> i = listOfActiveEvents.iterator();
        while (i.hasNext()) {
            SchedulerEvent e = i.next();
            if (event.isEqual(e)) {
                event.setCreated(e.getCreated());
                event.setExpires(e.getExpires());
                erg = "active";
            }

        }
        return erg;
    }

    private String sendCommand(final String command) {
        String s = "";
        SOSSchedulerCommand socket = null;
        try {

            socket = new SOSSchedulerCommand();
            socket.connect(host, port);

            logger.debug(String.format("Sending command '%3$s' to JobScheduler %1$s:%2$d ", host, port, command));
            socket.sendRequest(command);
            s = socket.getResponse();

            logger.debug(String.format("Answer is '%1$s' ", s));
        } catch (Exception ee) {
            logger.info("Error sending Command: " + ee.getMessage());

        } finally {
            if (socket != null)
                try {
                    socket.disconnect();
                } catch (Exception e1) {
                    logger.error(e1.getMessage(), e1);
                }

        }
        return s;
    }

    public void buildEventsFromXMl() throws DOMException, Exception {
        String response = "";
        Document doc = null;
        if (activeEvents == null) {
            response = sendCommand("<param.get name=\"" + JobSchedulerConstants.eventVariableName + "\"/>");
        }
        if (!response.equals("") || activeEvents != null) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            try {

                listOfActiveEvents.clear();

                if (activeEvents == null) {
                    docBuilder = docFactory.newDocumentBuilder();
                    doc = docBuilder.parse(new InputSource(new StringReader(response)));
                    NodeList params = doc.getElementsByTagName("param");
                    if (params.item(0) == null) {
                        errmsg = "No events param found in Job Scheduler answer for " + JobSchedulerConstants.eventVariableName;
                    } else {
                        NamedNodeMap attrParam = params.item(0).getAttributes();
                        String eventString = getText(attrParam.getNamedItem("value"));
                        eventString = eventString.replaceAll(String.valueOf((char) 254), "<").replaceAll(String.valueOf((char) 255), ">");

                        docFactory = DocumentBuilderFactory.newInstance();
                        docBuilder = docFactory.newDocumentBuilder();
                        doc = docBuilder.parse(new InputSource(new StringReader(eventString)));
                    }
                } else {
                    doc = activeEvents;
                }

                if (doc != null) {
                    NodeList nodes = org.apache.xpath.XPathAPI.selectNodeList(doc, "//events/event");
                    int activeNodeCount = 0;
                    int expiredNodeCount = 0;
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node node = nodes.item(i);
                        if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }
                        Node curEventExpires = node.getAttributes().getNamedItem("expires");
                        if (curEventExpires == null || curEventExpires.getNodeValue() == null || curEventExpires.getNodeValue().length() == 0) {
                            activeNodeCount++;
                            continue;
                        }
                        Calendar expiresDate = GregorianCalendar.getInstance();
                        Calendar now = GregorianCalendar.getInstance();
                        expiresDate.setTime(SOSDate.getTime(curEventExpires.getNodeValue()));
                        if (expiresDate.before(now)) {
                            doc.getFirstChild().removeChild(node);
                            expiredNodeCount++;
                        } else {
                            activeNodeCount++;
                        }
                    }

                    NodeList events = doc.getElementsByTagName("event");

                    for (int ii = 0; ii < events.getLength(); ii++) {

                        Node n = events.item(ii);
                        NamedNodeMap attr = n.getAttributes();
                        SchedulerEvent e = new SchedulerEvent();
                        e.setProperties(attr);

                        listOfActiveEvents.add(e);
                    }
                }

            } catch (ParserConfigurationException e) {
                errmsg = "XML-Answer from Scheduler was invalid";
            }
        }
    }

    private void fillTreeItem(final SOSActions a, final Node n) {
        a.commandNodes = n.getChildNodes();
        for (int i = 0; i < a.commandNodes.getLength(); i++) {
            Node command = a.commandNodes.item(i);
            if (command.getNodeName().equals("command") || command.getNodeName().equals("remove_event") || command.getNodeName().equals("add_event")) {
                SOSEventCommand ec = new SOSEventCommand();
                ec.setCommand(command);
                a.listOfCommands.add(ec);
            }
        }
    }

    private void fillEvents(final Node eventGroup, final SOSEventGroups evg) {

        NodeList events = eventGroup.getChildNodes();
        for (int i = 0; i < events.getLength(); i++) {
            Node event = events.item(i);
            if (event.getNodeName().equals("event")) {
                NamedNodeMap attr = event.getAttributes();
                SchedulerEvent e = new SchedulerEvent();

                e.setProperties(attr);
                e.setEventClassIfBlank(evg.event_class);

                evg.listOfEvents.add(e);
            }
        }
    }

    private void fillEventGroups(final SOSActions a, final Node n) {

        NamedNodeMap attrEvents = n.getAttributes();
        a.condition = getText(attrEvents.getNamedItem("logic"));

        NodeList eventGroups = n.getChildNodes();

        for (int i = 0; i < eventGroups.getLength(); i++) {
            Node eventGroup = eventGroups.item(i);
            if (eventGroup.getNodeName().equals("event_group")) {
                NamedNodeMap attr = eventGroup.getAttributes();
                SOSEventGroups evg = new SOSEventGroups(getText(attr.getNamedItem("group")));
                evg.condition = getText(attr.getNamedItem("logic"));
                evg.group = getText(attr.getNamedItem("group"));
                evg.event_class = getText(attr.getNamedItem("event_class"));
                fillEvents(eventGroup, evg);
                a.listOfEventGroups.add(evg);
            }
        }
    }

    private void fillAction(final SOSActions a, final NodeList actionChilds) {
        for (int i = 0; i < actionChilds.getLength(); i++) {
            Node n = actionChilds.item(i);
            if (n.getNodeName().equals("commands")) {
                a.commands = n;
                fillTreeItem(a, n);
            }
            if (n.getNodeName().equals("events")) {
                fillEventGroups(a, n);
            }
        }
    }

    public void readConfigurationFile(final File f) throws DOMException, Exception {
        listOfActions = new LinkedHashSet();
        if (f.exists()) {
            buildEventsFromXMl();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            try {
                listOfActions.clear();
                docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(f);
                NodeList actions = doc.getElementsByTagName("action");
                if (actions.item(0) == null) {
                    errmsg = "No actions defined in " + f.getCanonicalPath();
                    logger.info(errmsg);
                } else {
                    for (int i = 0; i < actions.getLength(); i++) {
                        NamedNodeMap attr = actions.item(i).getAttributes();
                        String action_name = getText(attr.getNamedItem("name"));
                        SOSActions a = new SOSActions(action_name);
                        listOfActions.add(a);

                        fillAction(a, actions.item(i).getChildNodes());
                    }
                }
            } catch (ParserConfigurationException e) {
                errmsg = "Error reading actions from " + f.getAbsolutePath();
                logger.info(errmsg);
            }
        }
    }

    private String getText(final Node n) {
        if (n != null) {
            return n.getNodeValue();
        } else {
            return "";
        }
    }

    public LinkedHashSet<SOSActions> getListOfActions() {
        return listOfActions;
    }

    public LinkedHashSet<SchedulerEvent> getListOfActiveEvents() {
        return listOfActiveEvents;
    }

    public static void main(final String[] args) {

        SOSEvaluateEvents eval = new SOSEvaluateEvents("localhost", 4454);
        String configuration_filename = "c:/roche/scheduler/config/events/splitt_gsg.actions.xml";
        File f = new File(configuration_filename);

        try {
            eval.readConfigurationFile(f);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        Iterator<SOSActions> iActions = eval.getListOfActions().iterator();
        while (iActions.hasNext()) {
            SOSActions a = iActions.next();

            // Die Nodelist verwenden
            for (int i = 0; i < a.getCommandNodes().getLength(); i++) {
                Node n = a.getCommandNodes().item(i);
                if (n.getNodeName().equals("command")) {
                    logger.info(n.getNodeName());
                    NamedNodeMap attr = n.getAttributes();
                    if (attr != null) {
                        for (int ii = 0; ii < attr.getLength(); ii++) {
                            logger.info(attr.item(ii).getNodeName() + "=" + attr.item(ii).getNodeValue());
                        }
                    }
                }
            }

            for (int i = 0; i < a.getCommands().getChildNodes().getLength(); i++) {
                Node n = a.getCommands().getChildNodes().item(i);
                if (n.getNodeName().equals("command")) {
                    logger.info(n.getNodeName());
                    NamedNodeMap attr = n.getAttributes();
                    if (attr != null) {
                        for (int ii = 0; ii < attr.getLength(); ii++) {
                            logger.info(attr.item(ii).getNodeName() + "=" + attr.item(ii).getNodeValue());
                        }
                    }
                }
            }

            if (a != null) {

                Iterator<SOSEventGroups> i = a.getListOfEventGroups().iterator();
                while (i.hasNext()) {
                    SOSEventGroups evg = i.next();
                    Iterator<SchedulerEvent> iEvents = evg.getListOfEvents().iterator();
                    while (iEvents.hasNext()) {
                        SchedulerEvent event = iEvents.next();
                        logger.info(event.getJob_name() + " " + eval.getEventStatus(event));
                    }
                }
            }

            if (a.isActive(eval.getListOfActiveEvents())) {
                logger.info(a.name + " is active");
            } else {
                logger.info(a.name + " is NOT active");
            }

        }
    }

    public void setActiveEvents(final Document activeEvents) {
        this.activeEvents = activeEvents;
    }

}
