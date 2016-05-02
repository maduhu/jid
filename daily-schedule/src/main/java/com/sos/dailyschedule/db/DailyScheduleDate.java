package com.sos.dailyschedule.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DailyScheduleDate {

    private static final Logger LOGGER = Logger.getLogger(DailyScheduleDate.class);
    private final String conClassName = "DailyScheduleDate";
    private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private Date schedule;
    private String isoDate;

    public DailyScheduleDate(String dateFormat_) {
        this.dateFormat = dateFormat_;
    }

    private void setIsoDate() throws ParseException {
        String isoDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(isoDateFormat);
        this.isoDate = formatter.format(schedule);
    }

    public void setSchedule(String schedule) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        if ("now".equals(schedule)) {
            this.schedule = new Date();
        } else {
            this.schedule = formatter.parse(schedule);
        }
        this.setIsoDate();
    }

    public Date getSchedule() {
        return schedule;
    }

    public String getIsoDate() {
        return isoDate;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
        try {
            this.setIsoDate();
        } catch (ParseException e) {
            LOGGER.info(conClassName + ".setScheduler: Could not set Iso-Date");
        }
    }

}