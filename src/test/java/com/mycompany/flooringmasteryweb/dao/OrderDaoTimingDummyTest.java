/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.BasicOrder;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.Timing;
import java.util.Date;
import java.util.List;
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

    /**
     * Test of create method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testCreate() {
        System.out.println("create");

        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.create(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of delete method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");

        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.delete(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of get method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        //new java.lang.Integer
        
        //OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        OrderDao dummyOrderDao = ctx.getBean("orderDao", OrderDao.class);
        
        long startTime = new Date().getTime();
        long systemStartTime = System.currentTimeMillis();
        dummyOrderDao.get(null);
        long stopTime = new Date().getTime();
        long systemStopTime = System.currentTimeMillis();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of getList method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testGetList() {
        System.out.println("getList");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.getList();
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of listOrderDates method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testListOrderDates() {
        System.out.println("listOrderDates");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.listOrderDates();
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of listOrderNumbers method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testListOrderNumbers() {
        System.out.println("listOrderNumbers");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.listOrderNumbers();
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of searchByDate method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByDate() {
        System.out.println("searchByDate");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.searchByDate(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of searchByName method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByName() {
        System.out.println("searchByName");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.searchByName(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of searchByProduct method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByProduct() {
        System.out.println("searchByProduct");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.searchByProduct(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of searchByState method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByState() {
        System.out.println("searchByState");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.searchByState(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of size method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.size();
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of update method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.update(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    /**
     * Test of searchByOrderNumber method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByOrderNumber() {
        System.out.println("searchByOrderNumber");
        long timeTaken = generateRandomTimeTakenTestValue();
        
        OrderDao dummyOrderDao = new OrderDaoTimingDummy(timeTaken);
        
        long startTime = new Date().getTime();
        dummyOrderDao.searchByOrderNumber(null);
        long stopTime = new Date().getTime();
        
        Timing timing = instance.getLast();
        
        assertEquals(timing.getDifferenceTime(), timeTaken);
        assertEquals(timing.getStartTime(), startTime);
        assertEquals(timing.getStopTime(), stopTime);
    }

    private long generateRandomTimeTakenTestValue() {
        Random random = new Random();
        long timeTaken = (long)random.nextInt(500);
        return timeTaken;
    }

}
