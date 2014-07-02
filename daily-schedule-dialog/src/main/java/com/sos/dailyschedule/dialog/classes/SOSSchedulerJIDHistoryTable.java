package com.sos.dailyschedule.dialog.classes;
 import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
 
import org.eclipse.swt.widgets.TableItem;

 import com.sos.dialog.classes.SOSTable;
import com.sos.hibernate.classes.DbItem;
import com.sos.jid.dialog.classes.SosTabLogItem;
import com.sos.localization.Messages;
import com.sos.scheduler.history.SchedulerHistoryDataProvider;

/**
* \class SOSSchedulerJIDHistoryTable 
* 
* \brief SOSSchedulerJIDHistoryTable - 
* 
* \details
*
* \section SOSSchedulerJIDHistoryTable.java_intro_sec Introduction
*
* \section SOSSchedulerJIDHistoryTable.java_samples Some Samples
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
* \version 01.07.2014
* \see reference
*
* Created on 27.06.2014 09:59:28
 */
public abstract class SOSSchedulerJIDHistoryTable extends SOSTable  {
	@SuppressWarnings("unused")
	private final String	conClassName	= "SOSSchedulerJIDHistoryTable";
    protected static final String conTabLOG = "Log";
    protected CTabFolder logTabFolder = null;
    private SchedulerHistoryDataProvider detailHistoryDataProvider = null;
    protected Messages      messages                = null;
    public abstract void createTable();
    

	public SOSSchedulerJIDHistoryTable(Composite composite, int style) {
		super(composite, style);
		init();
 	}

	protected void checkSubclass() {
 	}
	
	private void init() {
	     
        this.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (!isRightMouseclick()) {
                    showLog();
                }
            }
        });
        
	}
	
	 

    public void showLog() {
         if (logTabFolder != null && detailHistoryDataProvider != null && this.getSelectionIndex() >= 0 && this.getSelectionIndex() >= 0) {
            SosTabLogItem logItem = (SosTabLogItem) logTabFolder.getSelection();
            if (logItem == null) {
                logTabFolder.setSelection(0);
                logItem = (SosTabLogItem) logTabFolder.getSelection();
            }
            TableItem t = this.getItem(this.getSelectionIndex());
            DbItem d = (DbItem) t.getData();
            logItem.addLog(this, d.getTitle(), detailHistoryDataProvider.getLogAsString(d));
        }
     }


    
    public void setLogTabFolder(CTabFolder logTabFolder) {
        this.logTabFolder = logTabFolder;
    }

    public void setDetailHistoryDataProvider(SchedulerHistoryDataProvider detailHistoryDataProvider) {
        this.detailHistoryDataProvider = detailHistoryDataProvider;
    }


    public void setMessages(Messages messages) {
        this.messages = messages;
    }     
      
    

  
        
   
    
 
}
