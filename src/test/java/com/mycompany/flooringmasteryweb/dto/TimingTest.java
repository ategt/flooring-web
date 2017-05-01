/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ATeg
 */
public class TimingTest {

    public TimingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of equals method, of class Timing.
     */
    @Test
    public void testEqualsWithoutId() {
        Timing timinga = new Timing();
        Timing timingb = new Timing();
        Timing timingc = new Timing();

        assertTrue(timinga.equals(timingb));

        Random random = new Random();

        long start = random.nextLong();
        long stop = random.nextLong();
        long difference = random.nextLong();

        timinga.setDifferenceTime(difference);
        assertFalse(timinga.equals(timingb));
        assertNotEquals(timinga, timingb);
        timingb.setDifferenceTime(difference);
        assertTrue(timinga.equals(timingb));
        assertEquals(timinga, timingb);


        timingb.setStartTime(start);
        assertFalse(timinga.equals(timingb));
        assertNotEquals(timinga, timingb);
        timinga.setStartTime(start);
        assertTrue(timinga.equals(timingb));
        assertEquals(timinga, timingb);

        timinga.setStopTime(stop);
        assertFalse(timinga.equals(timingb));
        assertNotEquals(timinga, timingb);
        timingb.setStopTime(stop);

        assertTrue(timinga.equals(timingb));
        assertTrue(timingb.equals(timinga));
        assertTrue(timingb.equals(timingb));
        assertTrue(timinga.equals(timinga));
        assertEquals(timinga, timingb);
        assertEquals(timingb, timinga);
        assertEquals(timingb, timingb);
        assertEquals(timinga, timinga);

        timingc.setDifferenceTime(random.nextLong());
        timingc.setStartTime(random.nextLong());
        timingc.setStopTime(random.nextLong());

        assertFalse(timinga.equals(timingc));
        assertFalse(timingb.equals(timingc));
        assertFalse(timingc.equals(timinga));
        assertNotEquals(timinga, timingc);
        assertNotEquals(timingb, timingc);
        assertNotEquals(timingc, timinga);

        timingb.setDifferenceTime(random.nextLong());

        assertFalse(timinga.equals(timingb));
        assertNotEquals(timinga, timingb);

        timingb.setDifferenceTime(difference);

        assertTrue(timinga.equals(timingb));
        assertEquals(timinga, timingb);

        timingb.setStartTime(random.nextLong());

        assertFalse(timinga.equals(timingb));
        assertNotEquals(timinga, timingb);

        timingb.setStartTime(start);

        assertTrue(timinga.equals(timingb));
        assertEquals(timinga, timingb);

        timingb.setStopTime(random.nextLong());

        assertFalse(timinga.equals(timingb));
        assertNotEquals(timinga, timingb);

        timingb.setStopTime(stop);

        assertTrue(timinga.equals(timingb));
        assertEquals(timinga, timingb);

        timingb.setDifferenceTime(random.nextLong());
        timingb.setStartTime(random.nextLong());
        timingb.setStopTime(random.nextLong());

        assertFalse(timinga.equals(timingb));
        assertFalse(timingb.equals(timinga));
        assertTrue(timingb.equals(timingb));
        assertTrue(timinga.equals(timinga));

        assertNotEquals(timinga, timingb);
        assertNotEquals(timingb, timinga);
        assertEquals(timingb, timingb);
        assertEquals(timinga, timinga);
    }

    /**
     * Test of equals method, of class Timing.
     */
    @Test
    public void testEqualsWithId() {
        Timing timinga = new Timing();
        Timing timingb = new Timing();
        Timing timingc = new Timing();

        assertTrue(timinga.equals(timingb));
        assertEquals(timinga, timingb);

        Random random = new Random();

        int id = random.nextInt();
        long start = random.nextLong();
        long stop = random.nextLong();
        long difference = random.nextLong();

        timinga.setId(id);
        timinga.setDifferenceTime(difference);
        timinga.setStartTime(start);
        timinga.setStopTime(stop);

        assertFalse(timinga.equals(timingb));
        assertNotEquals(timinga, timingb);

        timingb.setId(id);
        timingb.setDifferenceTime(difference);
        timingb.setStartTime(start);
        timingb.setStopTime(stop);

        assertTrue(timinga.equals(timingb));
        assertEquals(timinga, timingb);
        assertTrue(timingb.equals(timinga));
        assertTrue(timingb.equals(timingb));
        assertTrue(timinga.equals(timinga));

        timingc.setId(random.nextInt());
        timingc.setDifferenceTime(random.nextLong());
        timingc.setStartTime(random.nextLong());
        timingc.setStopTime(random.nextLong());

        assertFalse(timinga.equals(timingc));
        assertNotEquals(timinga, timingc);
        assertFalse(timingb.equals(timingc));
        assertFalse(timingc.equals(timinga));

        timingb.setId(random.nextInt());
        timingb.setDifferenceTime(random.nextLong());
        timingb.setStartTime(random.nextLong());
        timingb.setStopTime(random.nextLong());

        assertNotEquals(timinga, timingb);
        assertFalse(timinga.equals(timingb));
        assertFalse(timingb.equals(timinga));
        assertTrue(timingb.equals(timingb));
        assertTrue(timinga.equals(timinga));
    }

    /**
     * Test of hashCode method, of class Timing.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Timing instancea = new Timing();
        Timing instanceb = new Timing();
        
        assertTrue(instancea.equals(instanceb));
        assertEquals(instancea, instanceb);
        assertEquals(instancea.hashCode() == instanceb.hashCode(), instancea.equals(instanceb));
        assertEquals(instancea.hashCode(), instanceb.hashCode());
        
        Random random = new Random();
        instancea.setId(random.nextInt());
        
        assertNotEquals(instancea, instanceb);
        assertFalse(instancea.equals(instanceb));
        assertEquals(instancea.hashCode() == instanceb.hashCode(), instancea.equals(instanceb));
        assertNotEquals(instancea.hashCode(), instanceb.hashCode());        
    }
}
