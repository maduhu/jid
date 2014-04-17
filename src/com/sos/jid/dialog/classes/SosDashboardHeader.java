package com.sos.jid.dialog.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import com.sos.JSHelper.Basics.JSToolBox;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.classes.SOSIntegerInputDialog;
import com.sos.dialog.interfaces.ITableView;

/**
* \class SosDashboardHeader 
* 
* \brief SosDashboardHeader - 
* 
* \details
*
* \section SosDashboardHeader.java_intro_sec Introduction
*
* \section SosDashboardHeader.java_samples Some Samples
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
* \version 27.01.2012
* \see reference
*
* Created on 27.01.2012 16:04:42
 */
public class SosDashboardHeader extends JSToolBox {
	@SuppressWarnings("unused")
    protected Preferences                   prefs;
	private String prefNode="SosDashboardHeader";
	private final String		conClassName	= "SosDashboardHeader";
	private static final String	EMPTY_STRING	= "";
	private Display				display;
	public Timer				refreshTimer;
	public Timer				inputTimer;
	private DateTime			fromDate		= null;
	private DateTime			toDate			= null;
	private CCombo				cbSchedulerId	= null;
	private ITableView			main			= null;
	private Text				searchField		= null;
	private int					refresh			= 0;
 	private Composite			parent;
	private Text				refreshInterval;
	private Button				refreshButton;
	private Label lblBis;
	private Label lbSchedulerID;
	private Label lblVon;
	private Integer limit=-1;
	
	public Text getRefreshInterval() {
		return refreshInterval;
	}
	public class RefreshTask extends TimerTask {
		public void run() {
			if (display == null) {
				display = Display.getDefault();
			}
			display.syncExec(new Runnable() {
				public void run() {
			
					 main.getList();  
				};
			});
		}
	}
	public class InputTask extends TimerTask {
		public void run() {
			if (display == null) {
				display = Display.getDefault();
			}
			display.syncExec(new Runnable() {
				public void run() {
					if (!getSearchField().equals(EMPTY_STRING)) {
						main.actualizeList();
						inputTimer.cancel();
					}
				};
			});
		}
	}

	public SosDashboardHeader(Composite parent_, ITableView main_) {
		super(DashBoardConstants.conPropertiesFileName);
		
		refreshTimer = new Timer();
		inputTimer = new Timer();
		refreshTimer.schedule(new RefreshTask(), 1000, 60000);
		main = main_;
		parent = parent_;
		createHeader();
	}

	public int getIntValue(String s, int d) {
		try {
			return Integer.parseInt(s);
		}
		catch (NumberFormatException n) {
			return d;
		}
	}

	public void setRefresh(int refresh) {
	    if (refresh <= 0){
	        refresh = 60;
	    }
		this.refresh = refresh;
	}
	
	public void setRefresh(String refresh) {
	    setRefresh(getIntValue(refresh,60));
    }

	public void resetRefreshTimer() {
	    
		refreshTimer.cancel();
		refreshTimer = new Timer();
		refreshTimer.schedule(new RefreshTask(), refresh * 1000, refresh * 1000);
	}

	public void resetInputTimer() {
		inputTimer.cancel();
		inputTimer = new Timer();
		inputTimer.schedule(new InputTask(), 2 * 1000, 2 * 1000);
	}


	private void createHeader() {
		refreshButton = new Button(parent, SWT.NONE);
		refreshButton.setLayoutData(new GridData(74, SWT.DEFAULT));
		refreshButton.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Refresh));
		refreshInterval = new Text(parent, SWT.RIGHT | SWT.BORDER);
		refresh = getIntValue(refreshInterval.getText(), 60);
		final GridData gd_refreshInterval = new GridData(35, SWT.DEFAULT);
		gd_refreshInterval.minimumWidth = 50;
		refreshInterval.setLayoutData(gd_refreshInterval);
		lbSchedulerID = new Label(parent, SWT.NONE);
		lbSchedulerID.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_SchedulerID));
		
		cbSchedulerId = new CCombo(parent, SWT.BORDER);
		GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_combo.widthHint = 120;
		gd_combo.minimumWidth = 120;
		cbSchedulerId.setLayoutData(gd_combo);
		lblVon = new Label(parent, SWT.NONE);
		lblVon.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVon.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_FROM));
		fromDate = new DateTime(parent, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		lblBis = new Label(parent, SWT.NONE);
		lblBis.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBis.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_TO));
		toDate = new DateTime(parent, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		searchField = new Text(parent, SWT.BORDER);
		searchField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	public void createMenue() {
        Menu contentMenu = new Menu(refreshButton);
        refreshButton.setMenu(contentMenu);
        parent.setMenu(contentMenu);
        lblBis.setMenu(contentMenu);
        lbSchedulerID.setMenu(contentMenu);
        lblVon.setMenu(contentMenu);
        // =============================================================================================

        SOSMenuLimitItem setLimitItem = new SOSMenuLimitItem(contentMenu, SWT.PUSH,prefs,prefNode);
        setLimitItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                limit = -1;
                main.actualizeList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
	}
	
	 public void initLimit(String prefNode_) {
	        prefNode = prefNode_;
	        createMenue();

	    }

	
	public DateTime getFromDate() {
		return fromDate;
	}

	public Date getFrom() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, fromDate.getDay());
		cal.set(Calendar.MONTH, fromDate.getMonth());
		cal.set(Calendar.YEAR, fromDate.getYear());
		return cal.getTime();
	}

	public Text getSearchField() {
		return searchField;
	}

	public DateTime getToDate() {
		return toDate;
	}

	public Date getTo() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, toDate.getDay());
		cal.set(Calendar.MONTH, toDate.getMonth());
		cal.set(Calendar.YEAR, toDate.getYear());
		return cal.getTime();
	}

	public CCombo getCbSchedulerId() {
		return cbSchedulerId;
	}
	
	public void setEnabled(boolean enabled){
		refreshButton.setEnabled(enabled);
		searchField.setEnabled(false);
		toDate.setEnabled(enabled);
		fromDate.setEnabled(enabled);
		cbSchedulerId.setEnabled(enabled);
		refreshInterval.setEnabled(enabled);
	}

	public Button getRefreshButton() {
		return refreshButton;
	}

	public void reset() {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date()); //heute
  
        fromDate.setDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        toDate.setDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        cbSchedulerId.setText("");
		searchField.setText("");
	}

    public Timer getRefreshTimer() {
        return refreshTimer;
    }
    
   
     
    public int getLimit() {
        if (limit != -1) {
            return limit;
        }else {
            int defaultLimit = DashBoardConstants.conSettingLIMITDefault;
            try {
            limit = Integer.parseInt(prefs.node(prefNode).get(DashBoardConstants.conSettingLIMIT, String.valueOf(defaultLimit)));
            }catch (NumberFormatException e) { 
                limit = defaultLimit;}
            }
            return limit;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
