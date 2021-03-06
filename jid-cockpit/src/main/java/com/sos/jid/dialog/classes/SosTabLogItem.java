package com.sos.jid.dialog.classes;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import com.sos.dialog.components.SOSSearchFilter;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSSearchFilterData;
import com.sos.localization.Messages;

public class SosTabLogItem extends CTabItem {

    private static final Logger LOGGER = Logger.getLogger(SosTabLogItem.class);
    private final Display parentDisplay;
    private final Composite composite;
    private final GridLayout layout;
    private final GridData data;
    private final SOSDashboardLogArea log;
    private Table sourceTable;
    private String sourceItemId;
    private Text edSearchfield;
    public Timer inputTimer;
    private Display display = null;
    private final Messages messages;
    private SOSSearchFilterData sosSearchFilterData;

    public SosTabLogItem(String caption, CTabFolder parent, Messages messages_) {
        super(parent, SWT.NONE);
        this.messages = messages_;
        inputTimer = new Timer();
        setText(caption);
        parentDisplay = parent.getDisplay();
        composite = new Composite(parent, SWT.NONE);
        layout = new GridLayout(2, false);
        composite.setLayout(layout);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        Button btnFilterButton = new Button(composite, SWT.NONE);
        btnFilterButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                SOSSearchFilter sosSearchFilter = new SOSSearchFilter(composite.getShell());
                sosSearchFilterData = sosSearchFilter.execute(edSearchfield.getText());
                if (sosSearchFilter.getSosSearchFilterData() != null) {
                    edSearchfield.setText(sosSearchFilter.getSosSearchFilterData().getSearchfield());
                }
            }
        });
        btnFilterButton.setText("Filter");
        edSearchfield = new Text(composite, SWT.BORDER);
        edSearchfield.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        edSearchfield.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (edSearchfield != null) {
                    if (sosSearchFilterData == null) {
                        sosSearchFilterData = new SOSSearchFilterData();
                        sosSearchFilterData.setRegularExpression(false);
                    }
                    sosSearchFilterData.setSearchfield(edSearchfield.getText());
                    resetInputTimer();
                }
            }
        });
        log = new SOSDashboardLogArea(composite, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI, messages);
        log.setEditable(false);
        log.setBackground(new Color(parentDisplay, 255, 255, 255));
        log.setLayoutData(data);
        this.setControl(composite);
    }

    public void setSelection() {
        if (sourceTable != null) {
            int l = sourceTable.getItemCount();
            for (int i = 0; i < l; i++) {
                DbItem d = (DbItem) sourceTable.getItem(i).getData();
                String s = "";
                if (d.getLogId() != null) {
                    s = d.getIdentifier();
                }
                if (sourceItemId.equals(s)) {
                    sourceTable.setSelection(i);
                    return;
                }
            }
        }
    }

    public void clearLog() {
        this.setText("Log");
        log.setText("");
    }

    public void addLog(Table table, String caption, String logContent) {
        this.setText(caption);
        if (logContent != null) {
            log.setText(logContent);
            sourceTable = table;
            DbItem d = (DbItem) table.getItem(table.getSelectionIndex()).getData();
            sourceItemId = d.getIdentifier();
        }
    }

    public class InputTask extends TimerTask {

        private static final String EMPTYSTRING = "";

        @Override
        public void run() {
            if (display == null) {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable() {

                @Override
                public void run() {
                    if (!edSearchfield.getText().equals(EMPTYSTRING)) {
                        try {
                            log.setSOSSearchFilterData(sosSearchFilterData);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                        inputTimer.cancel();
                    }
                }
            });
        }
    }

    private void resetInputTimer() {
        inputTimer.cancel();
        inputTimer = new Timer();
        inputTimer.schedule(new InputTask(), 1 * 1000, 1 * 1000);
    }

}