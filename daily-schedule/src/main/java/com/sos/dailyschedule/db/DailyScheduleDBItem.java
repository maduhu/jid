package com.sos.dailyschedule.db;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.joda.time.DateTime;

import com.sos.dailyschedule.ExecutionState;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.UtcTimeHelper;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;

@Entity
@Table(name = "DAYS_SCHEDULE")
public class DailyScheduleDBItem extends DbItem {

    private Long id;
    private String schedulerId;
    private String job;
    private String jobChain;
    private String orderId;
    private Date schedulePlanned;
    private Date scheduleExecuted;
    private Date periodBegin;
    private Date periodEnd;
    private boolean startStart;
    private Long isRepeat;
    private Integer status;
    private Integer result;
    private Date created;
    private Date modified;
    private Long schedulerOrderHistoryId;
    private Long schedulerHistoryId;
    private SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem;
    private SchedulerTaskHistoryDBItem schedulerTaskHistoryDBItem;
    private String dateFormat = "yyyy-MM-dd hh:mm";
    private ExecutionState executionState = new ExecutionState();

    public DailyScheduleDBItem(String dateFormat_) {
        this.dateFormat = dateFormat_;
    }

    public DailyScheduleDBItem() {

    }

    @ManyToOne(optional = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "`SCHEDULER_ORDER_HISTORY_ID`", referencedColumnName = "`HISTORY_ID`", insertable = false, updatable = false)
    public SchedulerOrderHistoryDBItem getSchedulerOrderHistoryDBItem() {
        return schedulerOrderHistoryDBItem;
    }

    public void setSchedulerOrderHistoryDBItem(SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem) {
        this.schedulerOrderHistoryDBItem = schedulerOrderHistoryDBItem;
    }

    @ManyToOne(optional = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "`SCHEDULER_HISTORY_ID`", referencedColumnName = "`ID`", insertable = false, updatable = false)
    public SchedulerTaskHistoryDBItem getSchedulerTaskHistoryDBItem() {
        return schedulerTaskHistoryDBItem;
    }

    public void setSchedulerTaskHistoryDBItem(SchedulerTaskHistoryDBItem schedulerTaskHistoryDBItem) {
        this.schedulerTaskHistoryDBItem = schedulerTaskHistoryDBItem;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "`ID`")
    public Long getId() {
        return id;
    }

    @Column(name = "`ID`")
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "`SCHEDULER_ID`", nullable = false)
    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    @Column(name = "`SCHEDULER_HISTORY_ID`", nullable = true)
    public void setSchedulerHistoryId(Long schedulerHistoryId) {
        this.schedulerHistoryId = schedulerHistoryId;
    }

    @Column(name = "`SCHEDULER_HISTORY_ID`", nullable = true)
    public Long getSchedulerHistoryId() {
        return schedulerHistoryId;
    }

    @Column(name = "`SCHEDULER_ORDER_HISTORY_ID`", nullable = true)
    public void setSchedulerOrderHistoryId(Long schedulerOrderHistoryId) {
        this.schedulerOrderHistoryId = schedulerOrderHistoryId;
    }

    @Column(name = "`SCHEDULER_ORDER_HISTORY_ID`", nullable = true)
    public Long getSchedulerOrderHistoryId() {
        return schedulerOrderHistoryId;
    }

    @Column(name = "`SCHEDULER_ID`", nullable = false)
    public String getSchedulerId() {
        return schedulerId;
    }

    @Column(name = "`JOB`", nullable = true)
    public void setJob(String job) {
        this.job = job;
    }

    @Column(name = "`JOB`", nullable = true)
    public String getJob() {
        return job;
    }

    @Transient
    public String getJobNotNull() {
        return null2Blank(job);
    }

    @Column(name = "`ORDER_ID`", nullable = true)
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "`ORDER_ID`", nullable = true)
    public String getOrderId() {
        return orderId;
    }

    @Transient
    public String getOrderIdNotNull() {
        return null2Blank(orderId);
    }

    @Column(name = "`JOB_CHAIN`", nullable = true)
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    @Column(name = "`JOB_CHAIN`", nullable = true)
    public String getJobChain() {
        return jobChain;
    }

    @Transient
    public String getJobChainNotNull() {
        return null2Blank(jobChain);
    }

    @Transient
    public String getJobOrJobchain() {
        if (this.isOrderJob()) {
            return null2Blank(String.format("%s(%s)", getJobChainNotNull(), getOrderIdNotNull()));
        } else {
            return null2Blank(getJobNotNull());
        }
    }

    @Column(name = "`STATUS`", nullable = false)
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "`STATUS`", nullable = false)
    public Integer getStatus() {
        return status;
    }

    @Column(name = "`RESULT`", nullable = false)
    public void setResult(Integer result) {
        this.result = result;
    }

    @Column(name = "`RESULT`", nullable = false)
    public Integer getResult() {
        return result;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`SCHEDULE_PLANNED`", nullable = true)
    public void setSchedulePlanned(Date schedulePlanned) {
        this.schedulePlanned = schedulePlanned;
    }

    @Transient
    public void setSchedulePlanned(String schedulePlanned) throws ParseException {
        DailyScheduleDate dailyScheduleDate = new DailyScheduleDate(dateFormat);
        dailyScheduleDate.setSchedule(schedulePlanned);
        this.schedulePlanned = dailyScheduleDate.getSchedule();
    }

    @Transient
    public void setScheduleExecuted(String scheduleExecuted) throws ParseException {
        DailyScheduleDate dailyScheduleDate = new DailyScheduleDate(dateFormat);
        dailyScheduleDate.setSchedule(scheduleExecuted);
        this.scheduleExecuted = dailyScheduleDate.getSchedule();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`SCHEDULE_PLANNED`", nullable = true)
    public Date getSchedulePlanned() {
        return schedulePlanned;
    }

    @Transient
    public String getSchedulePlannedIso() {
        if (this.getSchedulePlanned() == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(this.getSchedulePlanned());
        }
    }

    @Transient
    public String getSchedulePlannedFormated() {
        return getDateFormatted(this.getSchedulePlanned());
    }

    @Transient
    public String getScheduleEndedFormated() {
        if (this.isOrderJob()) {
            if (this.getSchedulerOrderHistoryDBItem() != null) {
                return getDateFormatted(this.getSchedulerOrderHistoryDBItem().getEndTime());
            } else {
                return "";
            }

        } else {
            if (this.getSchedulerTaskHistoryDBItem() != null) {
                return getDateFormatted(this.getSchedulerTaskHistoryDBItem().getEndTime());
            } else {
                return "";
            }

        }
    }

    @Transient
    public Date getEndTimeFromHistory() {
        if (this.isOrderJob()) {
            if (this.getSchedulerOrderHistoryDBItem() != null) {
                return this.getSchedulerOrderHistoryDBItem().getEndTime();
            } else {
                return null;
            }

        } else {
            if (this.getSchedulerTaskHistoryDBItem() != null) {
                return this.getSchedulerTaskHistoryDBItem().getEndTime();
            } else {
                return null;
            }

        }
    }

    @Transient
    public String getScheduleExecutedIso() {
        if (this.getScheduleExecuted() == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(this.getScheduleExecuted());
        }
    }

    @Transient
    public String getScheduleExecutedFormated() {
        return getDateFormatted(this.getScheduleExecuted());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`SCHEDULE_EXECUTED`", nullable = true)
    public void setScheduleExecuted(Date scheduleExecuted) {
        this.scheduleExecuted = scheduleExecuted;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`SCHEDULE_EXECUTED`", nullable = true)
    public Date getScheduleExecuted() {
        return scheduleExecuted;
    }

    @Column(name = "`IS_REPEAT`", nullable = true)
    public void setIsRepeat(Long repeat) {
        this.isRepeat = repeat;
    }

    @Override
    @Transient
    public String getDurationFormated() {
        return this.getDateDiff(this.getScheduleExecuted(), this.getEndTimeFromHistory());
    }

    @Transient
    public void setRepeat(BigInteger absolutRepeat_, BigInteger repeat_) {
        BigInteger r = BigInteger.ZERO;
        Long l = Long.valueOf(0);
        if (absolutRepeat_ != null && !absolutRepeat_.equals(BigInteger.ZERO)) {
            r = absolutRepeat_;
            this.startStart = true;
            if (r != null) {
                l = Long.valueOf(r.longValue());
            }
        } else {
            r = repeat_;
            this.startStart = false;
            if (r != null) {
                l = Long.valueOf(r.longValue());
            }
        }
        this.isRepeat = l;
    }

    @Column(name = "`IS_REPEAT`", nullable = true)
    public Long getIsRepeat() {
        return isRepeat;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`PERIOD_BEGIN`", nullable = true)
    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
        this.schedulePlanned = periodBegin;

    }

    @Transient
    public void setPeriodBegin(String periodBegin) throws ParseException {
        DailyScheduleDate daysScheduleDate = new DailyScheduleDate(dateFormat);
        daysScheduleDate.setSchedule(periodBegin);
        this.periodBegin = daysScheduleDate.getSchedule();
        this.schedulePlanned = this.periodBegin;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`PERIOD_BEGIN`", nullable = true)
    public Date getPeriodBegin() {
        return periodBegin;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`PERIOD_END`", nullable = true)
    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    @Transient
    public void setPeriodEnd(String periodEnd) throws ParseException {
        DailyScheduleDate daysScheduleDate = new DailyScheduleDate(dateFormat);
        daysScheduleDate.setSchedule(periodEnd);
        this.periodEnd = daysScheduleDate.getSchedule();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`PERIOD_END`", nullable = true)
    public Date getPeriodEnd() {
        return periodEnd;
    }

    @Column(name = "`START_START`", nullable = true)
    public void setStartStart(Boolean startStart) {
        this.startStart = startStart;
    }

    @Column(name = "`START_START`", nullable = true)
    public Boolean getStartStart() {
        return startStart;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`CREATED`", nullable = false)
    public Date getCreated() {
        return created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`CREATED`", nullable = false)
    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`MODIFIED`", nullable = true)
    public Date getModified() {
        return modified;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`MODIFIED`", nullable = true)
    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Transient
    public boolean isEqual(SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem) {
        String job_chain = this.getJobChain().replaceAll("^/", "");
        String job_chain2 = schedulerOrderHistoryDBItem.getJobChain().replaceAll("^/", "");
        return (this.getSchedulePlanned().equals(schedulerOrderHistoryDBItem.getStartTime()) 
                || this.getSchedulePlanned().before(schedulerOrderHistoryDBItem.getStartTime()))
                && job_chain.equalsIgnoreCase(job_chain2) && this.getOrderId().equalsIgnoreCase(schedulerOrderHistoryDBItem.getOrderId());
    }

    @Transient
    public boolean isEqual(SchedulerTaskHistoryDBItem schedulerHistoryDBItem) {
        String job = this.getJob().replaceAll("^/", "");
        String job2 = schedulerHistoryDBItem.getJobName().replaceAll("^/", "");
        return (this.getSchedulePlanned().equals(schedulerHistoryDBItem.getStartTime()) 
                || this.getSchedulePlanned().before(schedulerHistoryDBItem.getStartTime())) && job.equalsIgnoreCase(job2);
    }

    @Transient
    public boolean isOrderJob() {
        return !this.isStandalone();
    }

    @Transient
    public boolean isStandalone() {
        return this.job != null && !"".equals(this.job) && (this.jobChain == null || "".equals(this.jobChain));
    }

    @Transient
    public String getName() {
        if (isStandalone()) {
            return this.job;
        } else {
            return this.jobChain + "/" + this.orderId;
        }
    }

    @Transient
    public ExecutionState getExecutionState() {
        String fromTimeZoneString = "UTC";
        DateTime dateTimePlannedInUtc = new DateTime(schedulePlanned);
        DateTime dateTimeExecutedInUtc = null;
        DateTime dateTimePeriodBeginInUtc = null;
        if (scheduleExecuted != null) {
            dateTimeExecutedInUtc = new DateTime(scheduleExecuted);
        }
        if (periodBegin != null) {
            dateTimePeriodBeginInUtc = new DateTime(periodBegin);
        }
        String toTimeZoneString = TimeZone.getDefault().getID();
        Date plannedLocal = UtcTimeHelper.convertTimeZonesToDate(fromTimeZoneString, toTimeZoneString, dateTimePlannedInUtc);
        Date executedLocal = UtcTimeHelper.convertTimeZonesToDate(fromTimeZoneString, toTimeZoneString, dateTimeExecutedInUtc);
        Date periodBeginLocal = UtcTimeHelper.convertTimeZonesToDate(fromTimeZoneString, toTimeZoneString, dateTimePeriodBeginInUtc);
        this.executionState.setSchedulePlanned(plannedLocal);
        this.executionState.setScheduleExecuted(executedLocal);
        this.executionState.setPeriodBegin(periodBeginLocal);
        return executionState;
    }

    @Transient
    public Long getLogId() {
        if (isOrderJob()) {
            return this.getSchedulerOrderHistoryId();
        } else {
            return this.getSchedulerHistoryId();
        }
    }

    @Transient
    public String getTitle() {
        if (isOrderJob()) {
            return this.getJobChain() + "/" + this.getOrderId();
        } else {
            return this.getJob();
        }
    }

    @Transient
    public String getIdentifier() {
        if (getLogId() != null) {
            return getTitle() + getLogId().toString();
        } else {
            return getTitle();
        }
    }

    @Transient
    public String getJobName() {
        return getJob();
    }

    @Transient
    public boolean haveError() {
        if (this.isOrderJob()) {
            return this.schedulerOrderHistoryDBItem != null && this.schedulerOrderHistoryDBItem.haveError();
        } else {
            return this.schedulerTaskHistoryDBItem != null && this.schedulerTaskHistoryDBItem.haveError();
        }
    }

    @Transient
    public String getResultValue() {
        if (this.isOrderJob()) {
            if (this.schedulerOrderHistoryDBItem != null) {
                return this.schedulerOrderHistoryDBItem.getState();
            } else {
                return String.valueOf(this.getResult());
            }
        } else {
            if (this.schedulerTaskHistoryDBItem != null) {
                return String.valueOf(this.schedulerTaskHistoryDBItem.getExitCode());
            } else {
                return String.valueOf(this.getResult());
            }
        }
    }

}