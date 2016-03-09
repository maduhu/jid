/*
 * Created on 13.10.2008 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.sos.eventing.eventhandler;

import java.util.LinkedHashSet;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SOSEventCommand {

    private Node command = null;
    private NamedNodeMap attr = null;
    private LinkedHashSet<SOSEventCommandElement> listOfCommandElements = null;

    private String getText(Node n) {
        if (n != null) {
            return n.getNodeValue();
        } else {
            return "";
        }
    }

    private String addCommandElements() {
        String erg = "";
        listOfCommandElements = new LinkedHashSet<SOSEventCommandElement>();
        NodeList c = command.getChildNodes();

        for (int i = 0; i < c.getLength(); i++) {
            Node node = c.item(i);
            if (node == null || node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            SOSEventCommandElement ece = new SOSEventCommandElement();
            ece.command = node;
            listOfCommandElements.add(ece);
        }
        return erg;
    }

    public String getAttribute(String a) {
        if (command.getNodeName().equals("command")) {
            if (attr == null)
                attr = command.getAttributes();
            return getText(attr.getNamedItem(a));
        }
        return "";
    }

    public Node getCommand() {
        return command;
    }

    public void setCommand(Node n) {
        command = n;
        addCommandElements();
    }

    public LinkedHashSet<SOSEventCommandElement> getListOfCommandElements() {
        return listOfCommandElements;
    }
}
