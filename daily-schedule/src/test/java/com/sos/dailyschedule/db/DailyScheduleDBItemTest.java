package com.sos.dailyschedule.db;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.dailyschedule.db.DailyScheduleDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;

public class DailyScheduleDBItemTest {

    @SuppressWarnings("unused")
    private final String conClassName = "DailyScheduleDBItemTest";
    private DailyScheduleDBItem dailyScheduleDBItem = null;

    public DailyScheduleDBItemTest() {
        //
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        dailyScheduleDBItem = new DailyScheduleDBItem();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSetId() {
        Long myId = new Long(4711);
        dailyScheduleDBItem.setId(myId);
        Long id = dailyScheduleDBItem.getId();
        assertEquals("testSetid faild: ", myId, id);
    }

    @Test
    public void testSetSchedulerId() {
        String myId = "mySchedulerId";
        dailyScheduleDBItem.setSchedulerId(myId);
        String id = dailyScheduleDBItem.getSchedulerId();
        assertEquals("testSetSchedulerId faild: ", myId, id);
    }

    @Test
    public void testSetSchedulerHistoryId() {
        Long myId = new Long(4711);
        dailyScheduleDBItem.setSchedulerHistoryId(myId);
        Long id = dailyScheduleDBItem.getSchedulerHistoryId();
        assertEquals("testSetSchedulerHistoryId faild: ", myId, id);
    }

    @Test
    public void testSetSchedulerOrderHistoryId() {
        Long myId = new Long(4711);
        dailyScheduleDBItem.setSchedulerOrderHistoryId(myId);
        Long id = dailyScheduleDBItem.getSchedulerOrderHistoryId();
        assertEquals("testSetSchedulerOrderHistoryId faild: ", myId, id);
    }

    @Test
    public void testSetJob() {
        String myJob = "Job";
        dailyScheduleDBItem.setJob(myJob);
        String job = dailyScheduleDBItem.getJob();
        assertEquals("testSetjob failed: ", myJob, job);
    }

    @Test
    public void testSetOrderId() {
        String myId = "myId";
        dailyScheduleDBItem.setOrderId(myId);
        String id = dailyScheduleDBItem.getOrderId();
        assertEquals("testSetOrderId faild: ", myId, id);
    }

    @Test
    public void testSetJobChain() {
        String myJobChain = "JobChain";
        dailyScheduleDBItem.setJobChain(myJobChain);
        String jobChain = dailyScheduleDBItem.getJobChain();
        assertEquals("testSetjobChain failed: ", myJobChain, jobChain);
    }

    @Test
    public void testSetStatus() {
        Integer myStatus = new Integer(4711);
        dailyScheduleDBItem.setStatus(myStatus);
        Integer status = dailyScheduleDBItem.getStatus();
        assertEquals("testSetSchedulerHistoryId faild: ", myStatus, status);
    }

    @Test
    public void testSetResult() {
        Integer myResult = new Integer(4711);
        dailyScheduleDBItem.setResult(myResult);
        Integer result = dailyScheduleDBItem.getResult();
        assertEquals("testSetSchedulerHistoryId faild: ", myResult, result);
    }

    @Test
    public void testSetSchedulePlannedDate() {
        Date d1 = new Date();
        dailyScheduleDBItem.setSchedulePlanned(d1);
        Date d2 = dailyScheduleDBItem.getSchedulePlanned();
        assertEquals("Test setSchedulePlannedString failed...", 0, d1.compareTo(d2));
    }

    @Test
    public void testSetSchedulePlannedString() throws ParseException {

        dailyScheduleDBItem.setSchedulePlanned("now");

        Date d1 = dailyScheduleDBItem.getSchedulePlanned();
        Date d2 = new Date();
        String testDateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(testDateFormat);
        String today = formatter.format(d1);
        String today2 = formatter.format(d2);

        assertEquals("Test setSchedulePlannedString failed...", today, today2);
    }

    @Test
    public void getSchedulePlannedIso() {
        DailyScheduleDBItem dailyScheduleDBItem = new DailyScheduleDBItem();
        assertEquals("Test getSchedulePlannedIso failed...", "", dailyScheduleDBItem.getSchedulePlannedIso());

        Date d1 = new Date();
        dailyScheduleDBItem.setSchedulePlanned(d1);
        String today = dailyScheduleDBItem.getSchedulePlannedIso();

        String testDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(testDateFormat);
        String today2 = formatter.format(d1);

        assertEquals("Test setSchedulePlannedString failed...", today, today2);
    }

    @Test
    public void testGetScheduleExecutedIso() {
        DailyScheduleDBItem dailyScheduleDBItem = new DailyScheduleDBItem();
        assertEquals("Test getScheduleExecutedIso failed...", "", dailyScheduleDBItem.getScheduleExecutedIso());

        Date d1 = new Date();
        dailyScheduleDBItem.setScheduleExecuted(d1);
        String today = dailyScheduleDBItem.getScheduleExecutedIso();

        String testDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(testDateFormat);
        String today2 = formatter.format(d1);

        assertEquals("Test etScheduleExecutedIso failed...", today, today2);
    }

    @Test
    public void testSetScheduleExecuted() throws ParseException {
        Date d1 = new Date();
        dailyScheduleDBItem.setScheduleExecuted(d1);
        Date d2 = dailyScheduleDBItem.getScheduleExecuted();
        assertEquals("Test setScheduleExecuted failed...", 0, d1.compareTo(d2));
    }

    @Test
    public void testSetIsRepeatLong() {
        Long myRepeat = new Long(4711);
        dailyScheduleDBItem.setIsRepeat(myRepeat);
        Long repeat = dailyScheduleDBItem.getIsRepeat();
        assertEquals("test setRepeatLong faild: ", myRepeat, repeat);
    }

    @Test
    public void testSetRepeatBigIntegerBigInteger() {
        BigInteger b = BigInteger.valueOf(18);

        dailyScheduleDBItem.setRepeat(b, BigInteger.ZERO);
        Long l = dailyScheduleDBItem.getIsRepeat();
        assertEquals("Test setRepeatBigIntegerBigInteger...", b, BigInteger.valueOf(l));
        boolean isStartStart = dailyScheduleDBItem.getStartStart();
        assertEquals("Test setRepeatBigIntegerBigInteger...", true, isStartStart);

        dailyScheduleDBItem.setRepeat(BigInteger.ZERO, b);
        l = dailyScheduleDBItem.getIsRepeat();
        assertEquals("Test setRepeatBigIntegerBigInteger...", b, BigInteger.valueOf(l));
        isStartStart = dailyScheduleDBItem.getStartStart();
        assertEquals("Test setRepeatBigIntegerBigInteger...", false, isStartStart);
    }

    @Test
    public void testSetPeriodBeginDate() {
        Date d1 = new Date();
        dailyScheduleDBItem.setPeriodBegin(d1);
        Date d2 = dailyScheduleDBItem.getPeriodBegin();
        Date d3 = dailyScheduleDBItem.getSchedulePlanned();

        assertEquals("Test setPeriodBeginDate failed...", 0, d1.compareTo(d2));
        assertEquals("Test setPeriodBeginDate failed...", 0, d1.compareTo(d3));
    }

    @Test
    public void testSetPeriodBeginString() throws ParseException {
        dailyScheduleDBItem.setPeriodBegin("now");

        Date d1 = dailyScheduleDBItem.getPeriodBegin();
        Date d2 = dailyScheduleDBItem.getSchedulePlanned();
        Date d3 = new Date();
        String testDateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(testDateFormat);
        String today = formatter.format(d1);
        String today2 = formatter.format(d3);

        assertEquals("Test setPeriodBeginString failed...", today, today2);
        assertEquals("Test setPeriodBeginString failed...", 0, d1.compareTo(d2));
    }

    @Test
    public void testSetPeriodEndDate() {
        Date d1 = new Date();
        dailyScheduleDBItem.setPeriodEnd(d1);
        Date d2 = dailyScheduleDBItem.getPeriodEnd();

        assertEquals("Test setPeriodEndDate failed...", 0, d1.compareTo(d2));
    }

    @Test
    public void testSetPeriodEndString() throws ParseException {
        dailyScheduleDBItem.setPeriodBegin("now");

        Date d1 = dailyScheduleDBItem.getPeriodBegin();
        Date d3 = new Date();
        String testDateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(testDateFormat);
        String today = formatter.format(d1);
        String today2 = formatter.format(d3);

        assertEquals("Test setPeriodEndString failed...", today, today2);
    }

    @Test
    public void testSetStartStart() {
        dailyScheduleDBItem.setStartStart(true);
        assertEquals("Test setSchedulePlannedString failed...", dailyScheduleDBItem.getStartStart(), true);
    }

    @Test
    public void testSetCreated() {
        Date d1 = new Date();
        dailyScheduleDBItem.setCreated(d1);
        Date d2 = dailyScheduleDBItem.getCreated();

        assertEquals("Test setCreated failed...", 0, d1.compareTo(d2));
    }

    @Test
    public void testSetModified() {
        Date d1 = new Date();
        dailyScheduleDBItem.setModified(d1);
        Date d2 = dailyScheduleDBItem.getModified();

        assertEquals("Test setScheduleModified failed...", 0, d1.compareTo(d2));
    }

    @Test
    public void testIsEqualSchedulerOrderHistoryDBItem() {
        SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem = new SchedulerOrderHistoryDBItem();
        Date d = new Date();
        String jobChain = "/test/rest/fest";
        String orderId = "2789034";
        schedulerOrderHistoryDBItem.setStartTime(d);
        schedulerOrderHistoryDBItem.setOrderId(orderId);
        schedulerOrderHistoryDBItem.setJobChain(jobChain);
        dailyScheduleDBItem.setJobChain(jobChain);
        dailyScheduleDBItem.setSchedulePlanned(d);
        dailyScheduleDBItem.setOrderId(orderId);

        assertEquals("Test �sEqualSchedulerOrderHistoryDBItem failed...", true, dailyScheduleDBItem.isEqual(schedulerOrderHistoryDBItem));

        dailyScheduleDBItem.setOrderId(orderId + "*");
        assertEquals("Test �sEqualSchedulerOrderHistoryDBItem failed...", false, dailyScheduleDBItem.isEqual(schedulerOrderHistoryDBItem));
    }

    @Test
    public void testIsEqualSchedulerHistoryDBItem() {
        SchedulerTaskHistoryDBItem schedulerHistoryDBItem = new SchedulerTaskHistoryDBItem();
        Date d = new Date();
        String job = "/test/rest/fest";
        schedulerHistoryDBItem.setStartTime(d);
        schedulerHistoryDBItem.setJobName(job);
        dailyScheduleDBItem.setJob(job);
        dailyScheduleDBItem.setSchedulePlanned(d);

        assertEquals("Test �sEqualSchedulerOrderHistoryDBItem failed...", true, dailyScheduleDBItem.isEqual(schedulerHistoryDBItem));
        dailyScheduleDBItem.setJob(job + "*");
        assertEquals("Test isEqualSchedulerOrderHistoryDBItem failed...", false, dailyScheduleDBItem.isEqual(schedulerHistoryDBItem));

    }

    @Test
    public void testIsOrderJob() {
        dailyScheduleDBItem.setJob(null);
        assertEquals("Test isOrderJob failed...", true, dailyScheduleDBItem.isOrderJob());

        dailyScheduleDBItem.setJob("job");
        dailyScheduleDBItem.setJobChain("");
        assertEquals("Test isOrderJob failed...", false, dailyScheduleDBItem.isOrderJob());

        dailyScheduleDBItem.setJob("job");
        dailyScheduleDBItem.setJobChain("jobChain");
        assertEquals("Test isOrderJob failed...", true, dailyScheduleDBItem.isOrderJob());

    }

    @Test
    public void testIsStandalone() {
        dailyScheduleDBItem.setJob(null);
        assertEquals("Test isStandalone failed...", false, dailyScheduleDBItem.isStandalone());

        dailyScheduleDBItem.setJob("job");
        dailyScheduleDBItem.setJobChain("");
        assertEquals("Test isStandalone failed...", true, dailyScheduleDBItem.isStandalone());

        dailyScheduleDBItem.setJob("job");
        dailyScheduleDBItem.setJobChain("jobChain");
        assertEquals("Test isStandalone failed...", false, dailyScheduleDBItem.isStandalone());
    }
}
