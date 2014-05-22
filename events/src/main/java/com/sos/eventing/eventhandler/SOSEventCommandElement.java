/*
 * Created on 13.10.2008
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.sos.eventing.eventhandler;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
 
 
public class SOSEventCommandElement {
   protected Node command=null;
   private NamedNodeMap attr=null;
   
   private String getText( Node n) {
	  if (n != null) {
		 return n.getNodeValue();
	  }else {
		 return "";
      }
   }
   
   public String node2String() {
 	 if (command != null && command.getNodeType() == Node.ELEMENT_NODE) { 
	    NamedNodeMap attr = command.getAttributes();
	    String attr_string = "";
	    if (attr != null)    {
	   	    for (int ii = 0;ii < attr.getLength();ii++) {
	  	        attr_string += attr.item(ii).getNodeName() + "=" + attr.item(ii).getNodeValue() + " ";
	       }
	     }
  	    return command.getNodeName() + " " + attr_string ;
 	 }else {
 		return "";
 	 }
	 
   }
	      
   
   public String getAttribute(String a) {
	  if (command.getNodeName().equals("command")){
 		 if (attr == null) attr = command.getAttributes();
   		 return getText(attr.getNamedItem(a));
   	 }
	  return "";
   }

   public Node getCommand() {
      return command;
   }
    
}
