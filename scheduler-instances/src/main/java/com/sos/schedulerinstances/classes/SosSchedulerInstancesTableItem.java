package com.sos.schedulerinstances.classes;


import sos.scheduler.editor.app.ResourceManager;

import org.apache.log4j.Logger;
 import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.classes.SOSTableItem;
import com.sos.dialog.components.SOSTableColumn;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.interfaces.ISOSTableItem;
import com.sos.scheduler.db.SchedulerInstancesDBItem;

/**
* \class SosSchedulerInstancesTableItem
*
* \brief SosSchedulerInstancesTableItem -
*
* \details
*
* \section SosSchedulerInstancesTableItem.java_intro_sec Introduction
*
* \section SosSchedulerInstancesTableItem.java_samples Some Samples
*
* \code
*   .... code goes here ...
* \endcode
*
* <p style="text-align:center">
* <br />---------------------------------------------------------------------------
* <br /> APL/Software GmbH - Berlin
* <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
* <br />---------------------------------------------------------------------------
* </p>
* \author Uwe Risse
* \version 14.12.2011
* \see reference
*
* Created on 14.12.2011 13:51:13
 */

public class SosSchedulerInstancesTableItem extends SOSTableItem implements ISOSTableItem {
    private static final int ERROR_COLUMN_NUMBER = 7;
    private static final int STATUS_COLUMN_NUMBER = 8;
    private SchedulerInstancesDBItem schedulerInstancesDBItem=null;
    private  String[] textBuffer = null;

	private static Logger					logger						= Logger.getLogger(SosSchedulerInstancesTableItem.class);


	public SosSchedulerInstancesTableItem(final Table arg0, final int arg1) {
		super(arg0, arg1);
 	}
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
		}

	public DbItem getData() {
		return (SchedulerInstancesDBItem) super.getData();
	}

	public void setDBItem(final DbItem d) {
		schedulerInstancesDBItem = (SchedulerInstancesDBItem) d;
		this.setData(d);
	}


	  private void setImage(int column, Boolean checked) {
	       
	        
	        Image checkedImage =  ResourceManager.getImageFromResource("/sos/scheduler/editor/icons/config.gif");
	        Image uncheckedImage =  ResourceManager.getImageFromResource("/sos/scheduler/editor/icons/thin_close_view.gif");
	        
	        if (checked) {
	            this.setImage(column, checkedImage);
	        }else {
	            this.setImage(column, uncheckedImage);
	        }

	    }

	public void setColor() {
         org.eclipse.swt.graphics.Color magenta = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);
         org.eclipse.swt.graphics.Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
 	     org.eclipse.swt.graphics.Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		 org.eclipse.swt.graphics.Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		 org.eclipse.swt.graphics.Color gray = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
		 org.eclipse.swt.graphics.Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		 org.eclipse.swt.graphics.Color yellow = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);

  		 this.setForeground(black);  //SingleStart


		if (schedulerInstancesDBItem.getIsPaused() ) {  //Keine Ausführung
            this.setBackground(0,gray);  //Ausführung in der Zukunft
            this.setBackground(STATUS_COLUMN_NUMBER,gray);  //Ausführung in der Zukunft
		}else {
 			this.setBackground(white);
		 
            }
		colorSave();  
	}

	public void setColumns() {
		SchedulerInstancesDBItem d = schedulerInstancesDBItem;
		logger.debug("...creating tableItem: " + d.getSchedulerId() + ":" + getParent().getItemCount());


		textBuffer = new String[] { d.getSchedulerId(),
		                            d.getHostname(),
				                    d.getTcpPortValue(),
				                    d.getUdpPortValue(),
				                    d.getStartTimeFormated(),
				                    d.getStopTimeFormated(),
 				                    d.getDbName(),
                                    d.getDbHistoryTableName(),
                                    d.getDbOrderHistoryTableName(),
                                    d.getDbOrdersTableName(),
                                    d.getDbTasksTableName(),
                                    d.getDbVariablesTableName(),
                                    d.getWorkingDirectory(),
                                    d.getLiveDirectory(),
                                    d.getLogDir(),
                                    d.getIncludePath(),
                                    d.getIniPath(),
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    d.getParam(),
                                    d.getJettyHttpPortValue(),
                                    d.getJettyHttpsPortValue(),
                                    ""};
 				                
 
		this.setText(textBuffer);
		
        setImage(17,d.getIsService());
        setImage(18,d.getIsRunning());
        setImage(19,d.getIsPaused());
        setImage(20,d.getIsCluster());
        setImage(21,d.getIsAgent());
        setImage(25,d.getIsSosCommandWebservice());
        
	}

	
	   public void setColumnsShort() {
	        SchedulerInstancesDBItem d = schedulerInstancesDBItem;
	        logger.debug("...creating tableItem: " + d.getSchedulerId() + ":" + getParent().getItemCount());


	        textBuffer = new String[] { d.getSchedulerId(),
	                                    d.getHostname(),
	                                    d.getTcpPortValue(),
	                                    d.getUdpPortValue(),
	                                    };
	                                
	 
	        this.setText(textBuffer);
	        
	    }
	
	public String[] getTextBuffer() {
		return textBuffer;
	}
	@SuppressWarnings("unused")
	private final String	conClassName	= "SosSchedulerInstancesTableItem";


    @Override
    public Color[] getBackgroundColumn() {
       return colorsBackground;
    }
    @Override
    public Color[] getForegroundColumn() {
        return colorsForeground;
    }

    @Override
    public Color getBackground() {
        return null;
    }
    @Override
    public Color getForeground() {
        return null;
    }
    @Override
    public void setForeground(final Color c) {
    }

    @Override
    public boolean isDisposed() {
        // TODO Auto-generated method stub
        return false;
    }


    
 
     
      



}
