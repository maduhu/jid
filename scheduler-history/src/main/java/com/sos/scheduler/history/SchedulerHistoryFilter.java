package com.sos.scheduler.history;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSHibernateIntervalFilter;
import com.sos.hibernate.classes.SOSSearchFilterData;
import com.sos.hibernate.interfaces.ISOSHibernateFilter;
import com.sos.scheduler.history.classes.SOSIgnoreList;

public class SchedulerHistoryFilter extends SOSHibernateIntervalFilter implements ISOSHibernateFilter {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(SchedulerHistoryFilter.class);

    private final String conClassName = "SchedulerHistoryFilter";
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private Date executedFrom;
    private Date executedTo;
    private Date startTime;
    private Date endTime;
    private String schedulerId = "";
    private boolean showWithError = false;
    private boolean showRunning = false;
    private boolean showSuccessfull = false;
    private String executedFromIso = null;
    private String executedToIso = null;
    private SOSIgnoreList orderIgnoreList = null;
    private SOSIgnoreList taskIgnoreList = null;

    private boolean showJobs = true;
    private boolean showJobChains = true;
    private SOSSearchFilterData sosSearchFilterData;

    public SchedulerHistoryFilter() {
        super(DashBoardConstants.conPropertiesFileName);
        orderIgnoreList = new SOSIgnoreList();
        taskIgnoreList = new SOSIgnoreList();
        sosSearchFilterData = new SOSSearchFilterData();
    }

    public boolean isShowJobs() {
        return showJobs;
    }

    public void setShowJobs(final boolean showJobs) {
        this.showJobs = showJobs;
    }

    public boolean isShowJobChains() {
        return showJobChains;
    }

    public void setShowJobChains(final boolean showJobChains) {
        this.showJobChains = showJobChains;
    }

    public String getExecutedFromIso() {
        return executedFromIso;
    }

    public void setExecutedFromIso(final String executedFromIso) {
        this.executedFromIso = executedFromIso;
    }

    public String getExecutedToIso() {
        return executedToIso;
    }

    public void setExecutedToIso(final String executedToIso) {
        this.executedToIso = executedToIso;
    }

    public SOSIgnoreList getOrderIgnoreList() {
        return orderIgnoreList;
    }

    public SOSIgnoreList getTaskIgnoreList() {
        return taskIgnoreList;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Date getExecutedUtcFrom() {
        if (executedFrom == null) {
            return null;
        } else {
            return convertFromTimeZoneToUtc(executedFrom);

        }

    }

    public void setExecutedFrom(final String executedFrom) throws ParseException {
        if (executedFrom.equals("")) {
            this.executedFrom = null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Date d = formatter.parse(executedFrom);
            setExecutedFrom(d);
        }
    }

    public Date getExecutedUtcTo() {
        if (executedTo == null) {
            return null;
        } else {
            return convertFromTimeZoneToUtc(executedTo);
        }

    }

    public void setExecutedTo(final String executedTo) throws ParseException {
        if (executedTo.equals("")) {
            this.executedTo = null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Date d = formatter.parse(executedTo);
            setExecutedTo(d);
        }
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(final String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public void setExecutedFrom(final Date from) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String d = formatter.format(from);
        try {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            executedFrom = formatter.parse(d);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        executedFromIso = formatter.format(from);
    }

    public void setExecutedTo(final Date to) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String d = formatter.format(to);
        try {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            executedTo = formatter.parse(d);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        executedToIso = formatter.format(to);
    }

    public void setStartTime(final Date start) {
        startTime = start;
    }

    public void setEndTime(final Date end) {
        endTime = end;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Override
    public String getTitle() {
        String ignoreList = " ";
        int ignoreOrderCount = getOrderIgnoreList().size();
        int ignoreJobCount = getTaskIgnoreList().size();
        if (ignoreOrderCount > 0 || ignoreJobCount > 0) {
            ignoreList = String.format("%1s Jobs %2s Orders ignored", ignoreJobCount, ignoreOrderCount);
        }

        String s = "";
        if (schedulerId != null && !schedulerId.equals("")) {
            s += String.format("Id: %s ", schedulerId);
        }

        if (executedFrom != null) {
            s += String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_FROM) + ": %s ", date2Iso(executedFrom));
        }
        if (executedTo != null) {
            s += String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_TO) + ": %s ", date2Iso(executedTo));
        }
        if (showJobs) {
            s += " " + String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_JOBS));
        }
        if (showJobChains) {
            s += " " + String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_JOBCHAINS));
        }

        if (showWithError) {
            s += " " + String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_show_with_error));
        }
        if (showRunning) {
            s += " " + String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_show_running));
        }

        if (showSuccessfull) {
            s += " " + String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_show_successfull));
        }

        if (sosSearchFilterData != null && sosSearchFilterData.getSearchfield() != null) {
            s += sosSearchFilterData.getSearchfield();
        }

        String title = String.format("%1s %2s ", s, ignoreList);
        return title;
    }

    @Override
    public boolean isFiltered(final DbItem h) {
        return false;
    }

    public void setShowWithError(final boolean showWithError) {
        this.showWithError = showWithError;
    }

    public boolean isShowWithError() {
        return showWithError;
    }

    @Override
    public void setIntervalFromDate(Date d) {
        this.executedFrom = d;
    }

    @Override
    public void setIntervalToDate(Date d) {
        this.executedTo = d;
    }

    @Override
    public void setIntervalFromDateIso(String s) {
        this.executedFromIso = s;
    }

    @Override
    public void setIntervalToDateIso(String s) {
        this.executedToIso = s;
    }

    public void setShowRunning(boolean showRunning) {
        this.showRunning = showRunning;
    }

    public boolean isShowRunning() {
        return showRunning;
    }

    public boolean isShowSuccessfull() {
        return showSuccessfull;
    }

    public void setShowSuccessfull(boolean showSuccessfull) {
        this.showSuccessfull = showSuccessfull;
    }

    public SOSSearchFilterData getSosSearchFilterData() {
        return this.sosSearchFilterData;
    }

    public void setSosSearchFilterData(final SOSSearchFilterData sosSearchFilterData) {
        this.sosSearchFilterData = sosSearchFilterData;
    }
}
