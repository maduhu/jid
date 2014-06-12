package com.sos.dailyschedule.dialog.classes;


import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.components.SOSSearchFilter;
import com.sos.dialog.interfaces.ITableView;
import com.sos.jid.dialog.classes.SOSDashboardTableView;
import com.sos.jid.dialog.classes.SosDashboardHeader;
import com.sos.jid.dialog.classes.SosTabLogItem;
 
 

import org.eclipse.swt.SWT;
 
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class SOSDashboardTableViewSchedulerInstances extends SOSDashboardTableView implements ITableView {
    
    private final String className = "SOSDashboardTableViewSchedulerInstances";
    private static final int                NUMBER_OF_COLUMNS_IN_GRID           = 9;
    private static final String conTabLOG = "Log";

    public SOSDashboardTableViewSchedulerInstances(Composite composite_) {
        super(composite_);
        colPosForSort = 4;

    }

    private   Shell getParentShell() {
        return this.getShell();
    }
  
    @Override
    public void createMenue() {
        Menu contentMenu = new Menu(tableList);
        tableList.setMenu(contentMenu);

        MenuItem showLog = new MenuItem(contentMenu, SWT.PUSH);

        showLog.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_show_log_in_new_tab));
        showLog.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                SosTabLogItem tbtmLog = new SosTabLogItem(conTabLOG, logTabFolder, Messages);
                logTabFolder.setSelection(tbtmLog);
                showLog(tableList);
 
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        // =============================================================================================

        new MenuItem(contentMenu, SWT.SEPARATOR);

       

        // =============================================================================================
        
        MenuItem search = new MenuItem(contentMenu, SWT.PUSH);
        search.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Search));
        search.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
               
                SOSSearchFilter sosSearchFilter = new SOSSearchFilter(getParentShell());
                sosSearchFilter.setEnableFilterCheckbox(false);
                 sosSearchFilter.execute(EMPTY_STRING);
                 if (sosSearchFilter.getSosSearchFilterData() != null) {
                    if (!sosSearchFilter.getSosSearchFilterData().getSearchfield().equals(EMPTY_STRING)) {
                         try {
                             tableDataProvider.setSearchField(sosSearchFilter.getSosSearchFilterData());
                             actualizeList();
                         }
                          catch (Exception ee) {
                              ee.printStackTrace();
                          }
                    }
                 }
                
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        
      
        
        new MenuItem(contentMenu, SWT.SEPARATOR);
        MenuItem excel = new MenuItem(contentMenu, SWT.PUSH);
        excel.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Export_To_Excel));
        excel.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
              public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                  tableList.createExcelFile();
              }

              public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
              }
          });
        
         
      
    }

  
  
 
    public void createTable () {

        mainViewComposite = new Composite(leftTabFolder, SWT.NONE);
        GridLayout layout = new GridLayout(NUMBER_OF_COLUMNS_IN_GRID, false);
        mainViewComposite.setLayout(layout);
        sosDashboardHeader = new SosDashboardHeader(mainViewComposite, this);
        sosDashboardHeader.setPrefs(prefs);
        sosDashboardHeader.initLimit(DashBoardConstants.SOS_DASHBOARD_HEADER + "_" + className);


        tableList = new SosSchedulerInstancesTable(mainViewComposite, SWT.FULL_SELECTION | SWT.MULTI);
        

        super.createTable();
    }
    
   

 

}