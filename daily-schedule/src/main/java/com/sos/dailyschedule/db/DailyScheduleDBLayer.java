package com.sos.dailyschedule.db;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import com.sos.dailyschedule.DailyScheduleFilter;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.UtcTimeHelper;
import com.sos.hibernate.layer.SOSHibernateIntervalDBLayer;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;

/** @author Uwe Risse */
public class DailyScheduleDBLayer extends SOSHibernateIntervalDBLayer {

    private String whereFromIso = null;
    private String whereToIso = null;
    private DailyScheduleFilter filter = null;
    private Logger logger = Logger.getLogger(DailyScheduleDBLayer.class);

    public DailyScheduleDBLayer(final String configurationFilename) {
        super();
        this.setConfigurationFileName(configurationFilename);
        this.initConnection(this.getConfigurationFileName());
        resetFilter();
    }

    public DailyScheduleDBLayer(final File configurationFile) {
        super();
        try {
            this.setConfigurationFileName(configurationFile.getCanonicalPath());
        } catch (IOException e) {
            this.setConfigurationFileName("");
            logger.error(e.getMessage(), e);
        }
        this.initConnection(this.getConfigurationFileName());
        resetFilter();
    }

    public DailyScheduleDBItem getHistory(final Long id) {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        try {
            connection.connect();
            connection.beginTransaction();
            return (DailyScheduleDBItem) ((Session) connection.getCurrentSession()).get(DailyScheduleDBItem.class, id);
        } catch (Exception e) {
            logger.error("Error occured receiving data: ", e);
        }
        return null;
    }

    public void resetFilter() {
        filter = new DailyScheduleFilter();
        filter.setExecutedFrom(new Date());
        filter.setShowJobs(true);
        filter.setShowJobChains(true);
        filter.setLate(false);
        filter.setSchedulerId("");
        filter.getSosSearchFilterData().setSearchfield("");
        filter.setStatus("");
    }

    public int delete() {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        String hql = "delete from DailyScheduleDBItem " + getWhere();
        Query query = null;
        int row = 0;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery(hql);
            if (filter.getPlannedUtcFrom() != null && !"".equals(filter.getPlannedUtcFrom())) {
                query.setTimestamp("schedulePlannedFrom", filter.getPlannedUtcFrom());
            }
            if (filter.getPlannedUtcTo() != null && !"".equals(filter.getPlannedUtcTo())) {
                query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
            }
            if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
                query.setParameter("schedulerId", filter.getSchedulerId());
            }
            row = query.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            logger.error("Error occurred trying to delete items: ", e);
        }
        return row;
    }

    public long deleteInterval() {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        String hql = "delete from DailyScheduleDBItem " + getWhere();
        Query query = null;
        int row = 0;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery(hql);
            if (filter.getPlannedUtcFrom() != null) {
                query.setTimestamp("schedulePlannedFrom", filter.getPlannedUtcFrom());
            }
            if (filter.getPlannedUtcTo() != null) {
                query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
            }
            row = query.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            logger.error("Error occurred trying to delete items for the given interval: ", e);
        }
        return row;
    }

    private String getWhere() {
        String where = "";
        String and = "";
        if (filter.getPlannedUtcFrom() != null && !"".equals(filter.getPlannedUtcFrom())) {
            where += and + " schedulePlanned>= :schedulePlannedFrom";
            and = " and ";
        }
        if (filter.getPlannedUtcTo() != null && !"".equals(filter.getPlannedUtcTo())) {
            where += and + " schedulePlanned <= :schedulePlannedTo ";
            and = " and ";
        }
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            where += and + " schedulerId = :schedulerId";
            and = " and ";
        }
        if (!"".equals(where.trim())) {
            where = "where " + where;
        }
        return where;
    }

    public List<DailyScheduleDBItem> getDailyScheduleList(final int limit) {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        List<DailyScheduleDBItem> daysScheduleList = null;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery("from DailyScheduleDBItem " + getWhere() + filter.getOrderCriteria() + filter.getSortMode());
            if (filter.getPlannedFrom() != null && !"".equals(filter.getPlannedFrom())) {
                query.setTimestamp("schedulePlannedFrom", filter.getPlannedFrom());
            }
            if (filter.getPlannedTo() != null && !"".equals(filter.getPlannedTo())) {
                query.setTimestamp("schedulePlannedTo", filter.getPlannedTo());
            }
            if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
                query.setParameter("schedulerId", filter.getSchedulerId());
            }
            if (limit > 0) {
                query.setMaxResults(limit);
            }
            daysScheduleList = query.list();
        } catch (Exception e) {
            logger.error("Error occurred receiving DailyScheduleList: ", e);
        }
        return daysScheduleList;
    }

    private List<DailyScheduleDBItem> executeQuery(Query query, int limit) {
        if (filter.getPlannedUtcFrom() != null && !"".equals(filter.getPlannedUtcFrom())) {
            query.setTimestamp("schedulePlannedFrom", filter.getPlannedUtcFrom());
        }
        if (filter.getPlannedUtcTo() != null && !"".equals(filter.getPlannedUtcTo())) {
            query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
        }
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return query.list();
    }

    public List<DailyScheduleDBItem> getDailyScheduleSchedulerList(int limit) {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        String q = "from DailyScheduleDBItem e where e.schedulerId IN (select DISTINCT schedulerId from DailyScheduleDBItem " + getWhere() + ")";
        Query query = null;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery(q);
        } catch (Exception e) {
            logger.error("Error occurred creating query: ", e);
        }
        return executeQuery(query, limit);
    }

    public List<DailyScheduleDBItem> getWaitingDailyScheduleList(final int limit) {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        String q = "from DailyScheduleDBItem " + getWhere() + "  and status = 0  " + filter.getOrderCriteria() + filter.getSortMode();
        Query query = null;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery(q);
        } catch (Exception e) {
            logger.error("Error occurred creating query: ", e);
        }
        return executeQuery(query, limit);
    }

    public DailyScheduleFilter getFilter() {
        return filter;
    }

    public void setWhereFrom(final Date whereFrom) {
        filter.setPlannedFrom(whereFrom);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        whereFromIso = formatter.format(whereFrom);
    }

    public void setWhereTo(final Date whereTo) {
        UtcTimeHelper.convertTimeZonesToDate(UtcTimeHelper.localTimeZoneString(), "UTC", new DateTime(whereTo));
        filter.setPlannedTo(whereTo);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        whereToIso = formatter.format(whereTo);
    }

    public void setWhereFromUtc(final String whereFrom) throws ParseException {
        if ("".equals(whereFrom)) {
            filter.setPlannedFrom("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereFrom);
            d = UtcTimeHelper.convertTimeZonesToDate(UtcTimeHelper.localTimeZoneString(), "UTC", new DateTime(d));
            setWhereFrom(d);
        }
    }

    public void setWhereToUtc(final String whereTo) throws ParseException {
        if ("".equals(whereTo)) {
            filter.setPlannedTo("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereTo);
            d = UtcTimeHelper.convertTimeZonesToDate(UtcTimeHelper.localTimeZoneString(), "UTC", new DateTime(d));
            setWhereTo(d);
        }
    }

    public void setWhereFrom(final String whereFrom) throws ParseException {
        if ("".equals(whereFrom)) {
            filter.setPlannedFrom("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereFrom);
            setWhereFrom(d);
        }
    }

    public void setWhereTo(final String whereTo) throws ParseException {
        if ("".equals(whereTo)) {
            filter.setPlannedTo("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereTo);
            setWhereTo(d);
        }
    }

    public Date getWhereUtcFrom() {
        return filter.getPlannedUtcFrom();
    }

    public Date getWhereUtcTo() {
        return filter.getPlannedUtcTo();
    }

    public void setWhereSchedulerId(final String whereSchedulerId) {
        filter.setSchedulerId(whereSchedulerId);
    }

    public void setDateFormat(final String dateFormat) {
        filter.setDateFormat(dateFormat);
    }

    public String getWhereFromIso() {
        return whereFromIso;
    }

    public String getWhereToIso() {
        return whereToIso;
    }

    public void setFilter(final DailyScheduleFilter filter) {
        this.filter = filter;
    }

    public boolean contains(final SchedulerTaskHistoryDBItem schedulerHistoryDBItem) {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        List<DailyScheduleDBItem> daysScheduleList = null;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery("from DailyScheduleDBItem where schedulerId=:schedulerId and schedulerHistoryId=:schedulerHistoryId");
            query.setParameter("schedulerId", schedulerHistoryDBItem.getSpoolerId());
            query.setParameter("schedulerHistoryId", schedulerHistoryDBItem.getId());
            daysScheduleList = query.list();
        } catch (Exception e) {
            logger.error("Error occurred trying to find specific history item: ", e);
        }
        return daysScheduleList != null ? !daysScheduleList.isEmpty() : false;
    }

    public boolean contains(final SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem) {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        List<DailyScheduleDBItem> daysScheduleList = null;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery("from DailyScheduleDBItem where schedulerId=:schedulerId and jobChain=:jobChain and "
                    + "schedulerOrderHistoryId=:schedulerOrderHistoryId");
            query.setParameter("schedulerId", schedulerOrderHistoryDBItem.getSpoolerId());
            query.setParameter("schedulerOrderHistoryId", schedulerOrderHistoryDBItem.getHistoryId());
            query.setParameter("jobChain", schedulerOrderHistoryDBItem.getJobChain());
            daysScheduleList = query.list();
        } catch (Exception e) {
            logger.error("Error occurred trying to find specific order history item: ", e);
        }
        return daysScheduleList != null ? !daysScheduleList.isEmpty() : false;
    }

    @Override
    public void onAfterDeleting(DbItem h) {
    }

    @Override
    public List<DbItem> getListOfItemsToDelete() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        int limit = this.getFilter().getLimit();
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        List<DbItem> schedulerPlannedList = null;
        try {
            connection.connect();
            connection.beginTransaction();
            query = connection.createQuery("from DailyScheduleDBItem " + getWhere() + filter.getOrderCriteria() + filter.getSortMode());
            if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
                query.setText("schedulerId", filter.getSchedulerId());
            }
            if (filter.getPlannedUtcFrom() != null) {
                query.setTimestamp("schedulePlannedFrom", filter.getExecutedUtcFrom());
            }
            if (filter.getPlannedUtcTo() != null) {
                query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
            }
            if (limit > 0) {
                query.setMaxResults(limit);
            }
            schedulerPlannedList = query.list();
        } catch (Exception e) {
            logger.error("Error occurred receiving list of items to delete: ", e);
        }
        return schedulerPlannedList;
    }

}