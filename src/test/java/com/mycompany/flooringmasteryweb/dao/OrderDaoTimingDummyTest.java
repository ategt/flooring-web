/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.Timing;
import java.util.Date;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ATeg
 */
public class OrderDaoTimingDummyTest {

    ApplicationContext ctx;
    TimingDao instance;

    public OrderDaoTimingDummyTest() {
        ctx = new ClassPathXmlApplicationContext("testTimingOrderDaoAspectDb-DedicatedApplicationContext.xml");
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = ctx.getBean("timingDao", TimingDao.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testIntegerExpected() {
        System.out.println("Random Int");

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);
        assertEquals(expectedTime, new Integer(100));
    }

    /**
     * Test of create method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testCreate() {
        System.out.println("create");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.create(null);
        long stopTime = new Date().getTime();

        long timeTaken = stopTime - startTime;

        Timing timing = instance.getLast();

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of delete method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.delete((Order)null);
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();
        long timeTaken = stopTime - startTime;

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken));

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
    }

    /**
     * Test of get method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testGet() {
        System.out.println("get");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.get(null);
        long stopTime = new Date().getTime();

        long timeTaken = stopTime - startTime;

        Timing timing = instance.getLast();

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        assertTrue("Start Time Variance Exceeds Expectations: " + Math.abs(timing.getStartTime() - startTime),
                Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue("Stop Time Variance Exceeds Expectations: " + Math.abs(timing.getStopTime() - stopTime),
                Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed: " + Math.abs(timing.getDifferenceTime() - timeTaken),
                Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Expectation Failed: " + Math.abs(timing.getDifferenceTime() - expectedTime),
                Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of getList method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testGetList() {
        System.out.println("getList");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.getList();
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of listOrderDates method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testListOrderDates() {
        System.out.println("listOrderDates");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.listOrderDates();
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of listOrderNumbers method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testListOrderNumbers() {
        System.out.println("listOrderNumbers");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.listOrderNumbers();
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of searchByDate method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByDate() {
        System.out.println("searchByDate");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.searchByDate(null);
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of searchByName method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByName() {
        System.out.println("searchByName");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.searchByName(null);
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of searchByProduct method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByProduct() {
        System.out.println("searchByProduct");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.searchByProduct(null);
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);
        System.out.println("Expected: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " - " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed: " + Math.abs(timing.getDifferenceTime() - timeTaken), Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Expectation Failed: " + Math.abs(timing.getDifferenceTime() - expectedTime), Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of searchByState method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByState() {
        System.out.println("searchByState");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.searchByState(null);
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of size method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSize() {
        System.out.println("size");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.size();
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("2000ms Expectation Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 2000);
        assertTrue("1000ms Expectation Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 1000);
        assertTrue("500ms Expectation Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 500);
        assertTrue("250ms Expectation Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 250);
        assertTrue("150ms Expectation Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 150);
        assertTrue("50ms Expectation Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of update method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.update(null);
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }

    /**
     * Test of searchByOrderNumber method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByOrderNumber() {
        System.out.println("searchByOrderNumber");

        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);

        long startTime = new Date().getTime();
        dummyOrderDao.searchByOrderNumber(null);
        long stopTime = new Date().getTime();

        Timing timing = instance.getLast();

        long timeTaken = stopTime - startTime;

        Integer expectedTime = ctx.getBean("orderDaoIdleTime", Integer.class);

        System.out.println("start: " + Math.abs(timing.getStartTime() - startTime));
        System.out.println("stop: " + Math.abs(timing.getStopTime() - stopTime));
        System.out.println("difference: " + Math.abs(timing.getDifferenceTime() - timeTaken) + " / " + expectedTime);

        assertTrue(Math.abs(timing.getStartTime() - startTime) < 50);
        assertTrue(Math.abs(timing.getStopTime() - stopTime) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - timeTaken) < 50);
        assertTrue("Difference Failed.", Math.abs(timing.getDifferenceTime() - expectedTime) < 50);
    }
}
