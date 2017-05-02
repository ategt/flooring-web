/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Timing;
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
public class TimingDaoTest {

    ApplicationContext ctx;
    TimingDao instance;

    public TimingDaoTest() {
        ctx = new ClassPathXmlApplicationContext("testTimingDb-DedicatedApplicationContext.xml");
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
     * Test of create method, of class TimingDao.
     */
    @Test
    public void testCreateAndGet() {
        System.out.println("create and get");
        Timing timing = new Timing();

        Random random = new Random();
        long start = random.nextLong();
        long stop = random.nextLong();
        long difference = random.nextLong();

        timing.setStartTime(start);
        timing.setStopTime(stop);
        timing.setDifferenceTime(difference);

        Timing result = instance.create(timing);

        assertEquals(timing, result);

        assertTrue(result.getId() > 0);
        
        Timing storedTiming = instance.get(result.getId());
        
        assertEquals(storedTiming, result);
        
        Timing otherTiming = new Timing();

        otherTiming.setStartTime(random.nextLong());
        otherTiming.setStopTime(random.nextLong());
        otherTiming.setDifferenceTime(random.nextLong());

        assertEquals(result, otherTiming);
        Timing secondResult = instance.create(otherTiming);

        assertNotEquals(result, secondResult);
        assertEquals(result, timing);
    }

    /**
     * Test of getAll method, of class TimingDao.
     */
    @Test
    public void testGetAll() {
        System.out.println("create and get all");
        
        int startingSize = instance.getAll().size();
        
        Timing timing = new Timing();

        Random random = new Random();
        long start = random.nextLong();
        long stop = random.nextLong();
        long difference = random.nextLong();

        timing.setStartTime(start);
        timing.setStopTime(stop);
        timing.setDifferenceTime(difference);

        Timing result = instance.create(timing);
        
        Timing otherTiming = new Timing();

        otherTiming.setStartTime(random.nextLong());
        otherTiming.setStopTime(random.nextLong());
        otherTiming.setDifferenceTime(random.nextLong());

        Timing secondResult = instance.create(otherTiming);

        List<Timing> timings = instance.getAll();
        assertTrue(timings.contains(result));
        assertTrue(timings.contains(secondResult));
        assertEquals(timings.size(), startingSize);
    }

    /**
     * Test of getLast method, of class TimingDao.
     */
    @Test
    public void testGetLast() {
        System.out.println("getLast");
        TimingDao instance = null;
        Timing expResult = null;
        Timing result = instance.getLast();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class TimingDao.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Integer id = null;
        TimingDao instance = null;
        Timing expResult = null;
        Timing result = instance.get(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class TimingDao.
     */
    @Test
    public void testDelete_Timing() {
        System.out.println("delete");
        Timing timing = null;
        TimingDao instance = null;
        instance.delete(timing);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class TimingDao.
     */
    @Test
    public void testDelete_int() {
        System.out.println("delete");
        int id = 0;
        TimingDao instance = null;
        instance.delete(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
