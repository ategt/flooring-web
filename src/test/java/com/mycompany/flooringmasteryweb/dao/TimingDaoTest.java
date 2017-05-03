/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Timing;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
        timing.setModifiers(random.nextInt(32700));
        timing.setInvokingClassName(UUID.randomUUID().toString());
        timing.setInvokingMethodName(UUID.randomUUID().toString());

        Timing result = instance.create(timing);

        assertEquals(timing, result);

        assertTrue(result.getId() > 0);
        
        Timing storedTiming = instance.get(result.getId());
        
        assertEquals(storedTiming, result);
        
        Timing otherTiming = new Timing();

        otherTiming.setStartTime(random.nextLong());
        otherTiming.setStopTime(random.nextLong());
        otherTiming.setDifferenceTime(random.nextLong());

        assertNotEquals(result, otherTiming);
        Timing secondResult = instance.create(otherTiming);

        assertNotEquals(result, secondResult);
        assertEquals(result, timing);                
    }

    /**
     * Test of getAll method, of class TimingDao.
     */
    @Test
    public void testGetAllandSizeandDelete() {
        System.out.println("create and get all");
        
        int startingSize = instance.getAll().size();
        assertEquals(startingSize, instance.size());
        
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
        assertEquals(timings.size(), startingSize + 2);
        assertEquals(timings.size(), instance.size());
        
        int beforeDelete = timings.size();
        assertEquals(beforeDelete, instance.size());

        instance.delete(timing);
        
        List<Timing> timingsAfterFirstDelete = instance.getAll();
        assertEquals(timingsAfterFirstDelete.size(), beforeDelete - 1);
        assertFalse(timingsAfterFirstDelete.contains(timing));
        
        int beforeSecondDelete = instance.size();
        assertEquals(beforeSecondDelete, instance.size());
        instance.delete(secondResult.getId());
        
        List<Timing> timingsAfterSecondDelete = instance.getAll();
        assertEquals(timingsAfterSecondDelete.size(), beforeSecondDelete - 1);
        assertFalse(timingsAfterSecondDelete.contains(secondResult));
        assertEquals(startingSize, instance.getAll().size());
        assertEquals(startingSize, instance.size());
    }

    /**
     * Test of getLast method, of class TimingDao.
     */
    @Test
    public void testGetLast() {
        System.out.println("getLast");
        Random random = new Random();

        Timing timing = new Timing();

        timing.setStartTime(random.nextLong());
        timing.setStopTime(random.nextLong());
        timing.setDifferenceTime(random.nextLong());
        timing.setModifiers(random.nextInt(32700));
        timing.setInvokingClassName(UUID.randomUUID().toString());
        timing.setInvokingMethodName(UUID.randomUUID().toString());
        
        Timing result = instance.create(timing);
        
        Timing otherTiming = new Timing();

        otherTiming.setStartTime(random.nextLong());
        otherTiming.setStopTime(random.nextLong());
        otherTiming.setDifferenceTime(random.nextLong());

        Timing secondResult = instance.create(otherTiming);

        Timing lastTiming = instance.getLast();
        assertEquals(otherTiming, lastTiming);
        assertNotEquals(secondResult, result);
    }
}
