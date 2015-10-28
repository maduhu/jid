package com.sos.scheduler.history.db;

import java.io.File;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSHibernateIntervalFilter;
import com.sos.hibernate.layer.SOSHibernateIntervalDBLayer;
import com.sos.scheduler.history.SchedulerOrderHistoryFilter;

/**
 * 
 * \class SchedulerHistoryDBLayer \brief SchedulerHistoryDBLayer -
 * 
 * \details
 * 
 * \section SchedulerHistoryDBLayer.java_intro_sec Introduction
 * 
 * \section SchedulerHistoryDBLayer.java_samples Some Samples
 * 
 * \code .... code goes here ... \endcode
 * 
 * <p style="text-align:center">
 * <br />
 * --------------------------------------------------------------------------- <br />
 * APL/Software GmbH - Berlin <br />
 * ##### generated by ClaviusXPress (http://www.sos-berlin.com) ######### <br />
 * ---------------------------------------------------------------------------
 * </p>
 * \author Uwe Risse \version 27.09.2011 \see reference
 * 
 * Created on 27.09.2011 14:40:18
 */

public class SchedulerOrderHistoryDBLayer extends SOSHibernateIntervalDBLayer {
    private static Logger       logger          = Logger.getLogger(SchedulerOrderHistoryDBLayer.class);
	protected SchedulerOrderHistoryFilter filter = null;
	private String lastQuery="";

	public SchedulerOrderHistoryDBLayer(String configurationFilename) {
		super();
		this.setConfigurationFileName(configurationFilename);
		this.resetFilter();
		this.initConnection(this.getConfigurationFileName());
	}

	public SchedulerOrderHistoryDBItem get(Long id) {
		if (id == null) {
			return null;
		}
		try {
			connection.connect();
			connection.beginTransaction();
			return (SchedulerOrderHistoryDBItem) ((Session)connection.getCurrentSession()).get(SchedulerOrderHistoryDBItem.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	protected String getWhere() {
		String where = "";
		String and = "";
		if (filter.getSchedulerOrderHistoryId() != null) {
			where += and + " schedulerOrderHistoryId=:schedulerOrderHistoryId";
			and = " and ";
		} else {
			if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
				where += and + " spoolerId=:schedulerId";
				and = " and ";
			}
			if (filter.getJobchain() != null && !filter.getJobchain().equals("")) {
				where += and + " jobChain=:jobChain";
				and = " and ";
			}
			if (filter.getOrderid() != null && !filter.getOrderid().equals("")) {
				where += and + " orderId=:orderId";
				and = " and ";
			}
			if (filter.getStartTime() != null) {
				where += and + " startTime>= :startTime";
				and = " and ";
			}
			if (filter.getEndTime() != null) {
				where += and + " endTime <= :endTime ";
				and = " and ";
			}
			if (!where.trim().equals("")) {
				where = "where " + where;
			}
		}
		return where;
	}

	protected String getWhereFromTo() {
		String where = "";
		String and = "";
		if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
			where += and + " spoolerId=:schedulerId";
			and = " and ";
		}
		if (filter.getJobchain() != null && !filter.getJobchain().equals("")) {
			if (filter.getJobchain().contains("%")) {
				where += and + " jobChain like :jobChain";
			} else {
				where += and + " jobChain=:jobChain";
			}
			and = " and ";
		}
		if (filter.getOrderid() != null && !filter.getOrderid().equals("")) {
			if (filter.getOrderid().contains("%")) {
				where += and + " orderId like :orderId";
			} else {
				where += and + " orderId=:orderId";
			}
			and = " and ";
		}
		if (!filter.isShowJobChains() ) {
			where += and  + " 1=0";
	        and = " and ";
	    }
		if (filter.getExecutedUtcFrom() != null) {
			where += and + " startTime>= :startTimeFrom";
			and = " and ";
		}
		if (filter.getExecutedUtcTo() != null) {
			where += and + " startTime <= :startTimeTo ";
			and = " and ";
		}
		if (!where.trim().equals("")) {
			where = "where " + where;
		}
		return where;
	}
	
    protected String getWhereFromToStep() {
        String where = "";
        String and = "";
        if (filter.getExecutedUtcFrom() != null) {
            where += and + " startTime>= :startTimeFrom";
            and = " and ";
        }
        if (filter.getExecutedUtcTo() != null) {
            where += and + " startTime <= :startTimeTo ";
            and = " and ";
        }
        if (!where.trim().equals("")) {
            where = "where " + where;
        }
        return where;
    }
    
    @Override
    public void onAfterDeleting(DbItem h) {
    	int row = 0;
        SchedulerOrderHistoryDBItem x = (SchedulerOrderHistoryDBItem)h;
        String q = "delete from SchedulerOrderStepHistoryDBItem where id.historyId=" + x.getHistoryId();
        if(connection == null){
        	initConnection(getConfigurationFileName());
        }
        try {
        	connection.connect();
        	connection.beginTransaction();
			Query query = connection.createQuery(q);
			row = query.executeUpdate();
			connection.commit();
			logger.debug(String.format("%s steps deleted", row));
		} catch (Exception e) {
			logger.error("Error occurred while trying to delete Items: ", e);
		} 
    }
 
	public int delete() {
		String q = "delete from SchedulerOrderStepHistoryDBItem e where e.schedulerOrderHistoryDBItem.historyId IN (select historyId from SchedulerOrderHistoryDBItem "
				+ getWhereFromTo() + ")";
        if(connection == null){
        	initConnection(getConfigurationFileName());
        }
        int row = 0;
		try {
        	connection.connect();
			connection.beginTransaction();
			Query query = connection.createQuery(q);
			if (filter.getExecutedUtcFrom() != null) {
				query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
			}
			if (filter.getExecutedUtcTo() != null) {
				query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
			}
			row = query.executeUpdate();
			String hql = "delete from SchedulerOrderHistoryDBItem "	+ getWhereFromTo();
			query = connection.createQuery(hql);
			if (filter.getExecutedUtcFrom() != null) {
				query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
			}
			if (filter.getExecutedUtcTo() != null) {
				query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
			}
			row = query.executeUpdate();
		} catch (Exception e) {
			logger.error("Error occurred trying to delete Items: ", e);
		}
		return row;
	}

	private List<SchedulerOrderHistoryDBItem> executeQuery(Query query,int limit) {
        lastQuery = query.getQueryString();
		if (filter.getExecutedUtcFrom() != null && !filter.getExecutedUtcFrom().equals("")) {
			query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
		}
		if (filter.getExecutedUtcTo() != null && !filter.getExecutedUtcTo().equals("")) {
			query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
		}
		if (filter.getOrderid() != null && !filter.getOrderid().equals("")) {
			query.setText("orderId", filter.getOrderid());
		}
		if (filter.getJobchain() != null && !filter.getJobchain().equals("")) {
			query.setText("jobChain", filter.getJobchain());
		}
		if (filter.getSchedulerId() != null
				&& !filter.getSchedulerId().equals("")) {
			query.setText("schedulerId", filter.getSchedulerId());
		}
		if (limit > 0) {
			query.setMaxResults(limit);
		}
		List<SchedulerOrderHistoryDBItem> schedulerOrderHistoryList = query.list();
		int i = schedulerOrderHistoryList.size();
		return schedulerOrderHistoryList;
	}

	public List<SchedulerOrderHistoryDBItem> getSchedulerOrderHistoryListFromTo() {
		int limit = filter.getLimit();
        if(connection == null){
        	initConnection(getConfigurationFileName());
        }
        Query query = null;
		try {
        	connection.connect();
			connection.beginTransaction();
			query = connection.createQuery("from SchedulerOrderHistoryDBItem " + getWhereFromTo() + filter.getOrderCriteria() + filter.getSortMode());
		} catch (Exception e) {
			logger.error("Error occurred creating Query: ", e);
		}
		return executeQuery(query, limit);

	}

	public List<SchedulerOrderHistoryDBItem> getUnassignedSchedulerOrderHistoryListFromTo() {
		int limit = filter.getLimit();
        if(connection == null){
        	initConnection(getConfigurationFileName());
        }
        Query query = null;
		try {
        	connection.connect();
			connection.beginTransaction();
	        query = connection.createQuery("from SchedulerOrderHistoryDBItem "
	        		+ getWhereFromTo() +  " and historyId not in (select schedulerOrderHistoryId from DailyScheduleDBItem where not schedulerOrderHistoryId is null and status=1 and schedulerId=:schedulerId) " + filter.getOrderCriteria()
	                + filter.getSortMode());
		} catch (Exception e) {
			logger.error("Error occurred creating Query: ", e);
		}
        return executeQuery(query, limit);
	    }
	   
	  public List<SchedulerOrderHistoryDBItem> getOrderHistoryItems() {
		List<SchedulerOrderHistoryDBItem> historyList = null;
        if(connection == null){
        	initConnection(getConfigurationFileName());
        }
        try {
        	connection.connect();
			connection.beginTransaction();
			Query query = connection.createQuery("from SchedulerOrderHistoryDBItem " + getWhere());
			if (filter.getStartTime() != null && !filter.getStartTime().equals("")) {
				query.setTimestamp("startTime", filter.getStartTime());
			}
			if (filter.getEndTime() != null && !filter.getEndTime().equals("")) {
				query.setTimestamp("endTime", filter.getEndTime());
			}
			if (filter.getOrderid() != null && !filter.getOrderid().equals("")) {
				query.setText("orderId", filter.getOrderid());
			}
			if (filter.getJobchain() != null && !filter.getJobchain().equals("")) {
				query.setText("jobChain", filter.getJobchain());
			}
			if (filter.getSchedulerId() != null
					&& !filter.getSchedulerId().equals("")) {
				query.setText("schedulerId", filter.getSchedulerId());
			}
			if (filter.getLimit() > 0){
			   query.setMaxResults(filter.getLimit());
			}
			historyList = query.list();
		} catch (Exception e) {
			logger.error("Error occurred receiving data: ", e);
		}
		return historyList;
	}

	public List<SchedulerOrderHistoryDBItem> getSchedulerOrderHistoryListSchedulersFromTo() {
		int limit = filter.getLimit();
		Query query = null;
		if(connection == null){
			initConnection(getConfigurationFileName());
		}
		try {
        	connection.connect();
			connection.beginTransaction();
			String q = "from SchedulerOrderHistoryDBItem e where e.spoolerId IN (select distinct e.spoolerId from SchedulerOrderHistoryDBItem " + getWhereFromTo() + ")";
			query = connection.createQuery(q);
		} catch (Exception e) {
			logger.error("Error occurred creating Query: ", e);
		}
		return executeQuery(query, limit);
	}

	public SchedulerOrderHistoryDBItem getOrderHistoryItem() {
		List<SchedulerOrderHistoryDBItem> historyList = null;
		this.filter.setLimit(1);
		if(connection == null){
			initConnection(getConfigurationFileName());
		}
		try {
        	connection.connect();
			connection.beginTransaction();
			Query query = connection.createQuery("from SchedulerOrderHistoryDBItem " + getWhere());
			if (filter.getSchedulerOrderHistoryId() != null) {
				query.setLong("schedulerOrderHistoryId", filter.getSchedulerOrderHistoryId());
			} else {
				if (filter.getStartTime() != null && !filter.getStartTime().equals("")) {
					query.setTimestamp("startTime", filter.getStartTime());
				}
				if (filter.getEndTime() != null && !filter.getEndTime().equals("")) {
					query.setTimestamp("endTime", filter.getEndTime());
				}
				if (filter.getOrderid() != null && !filter.getOrderid().equals("")) {
					query.setText("orderId", filter.getOrderid());
				}
				if (filter.getJobchain() != null && !filter.getJobchain().equals("")) {
					query.setText("jobChain", filter.getJobchain());
				}
				if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
					query.setText("schedulerId", filter.getSchedulerId());
				}
			}
			query.setMaxResults(filter.getLimit());
			historyList = query.list();
		} catch (Exception e) {
			logger.error("Error occurred receiving Items: ", e);
		}
		if (historyList != null && historyList.size() > 0) {
			return historyList.get(0);
		} else {
			return null;
		}
	}

	public SchedulerOrderHistoryFilter getFilter() {
		return filter;
	}

	public void resetFilter() {
		this.filter = new SchedulerOrderHistoryFilter();
		this.filter.setDateFormat("yyyy-MM-dd HH:mm:ss");
		this.filter.setOrderCriteria("startTime");
		this.filter.setSortMode("desc");
	}

	private void setFilter(SchedulerOrderHistoryFilter filter) {
		this.filter = filter;
	}

    public String getLastQuery() {
        return lastQuery;
    }

    @Override      
    public List<DbItem> getListOfItemsToDelete() {
	    TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
	    int limit = this.getFilter().getLimit();
	    List<DbItem> schedulerHistoryList = null;
	    if (connection == null){
	    	initConnection(getConfigurationFileName());
	    }
	    try {
        	connection.connect();
			connection.beginTransaction();
			Query query = connection.createQuery("from SchedulerOrderHistoryDBItem " + getWhereFromTo() + filter.getOrderCriteria() + filter.getSortMode());
			if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
			    query.setText("schedulerId", filter.getSchedulerId());
			}
			if (filter.getExecutedUtcFrom() != null) {
			    query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
			}
			if (filter.getExecutedUtcTo() != null ) {
			    query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
			}
			if (limit > 0) {
			    query.setMaxResults(limit);
			}
			schedulerHistoryList = query.list();
		} catch (Exception e) {
			logger.error("Error occurred receiving list of items to delete: ", e);
		}
	    return schedulerHistoryList;
    }

    @Override
    public long deleteInterval() {
        return delete();
    }

}
