/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import org.junit.*;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 *
 * @author ATeg
 */
public class StateTest {
    
    public StateTest() {
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
     * Test of equals method, of class State.
     */
    @Test
    public void testEqualsWithNull() {
        System.out.println("equals");
        Object object = null;
        State instance = new State();
        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithNew() {
        System.out.println("equals");
        State object = new State();
        State instance = orderGenerator();

        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithTwoNew() {
        System.out.println("equals");
        State object = new State();
        State instance = new State();

        boolean expResult = true;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithRandom() {
        System.out.println("equals");
        State object = new State();
        for (int i = 0; i < 50; i++) {

            State instance = orderGenerator();

            boolean expResult = false;
            boolean result = instance.equals(object);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithOtherRandom() {
        System.out.println("equals");
        State object = new State();
        for (int i = 0; i < 50; i++) {

            State instance = orderGenerator();

            boolean expResult = false;
            boolean result = object.equals(instance);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithSelf() {
        System.out.println("equals");
        State object = new State();

        boolean expResult = true;
        boolean result = object.equals(object);
        assertEquals(expResult, result);
    }

    private State orderGenerator() {        
        State instance = new State();
        Random random = new Random();
        instance.setId(random.nextInt());
        instance.setState(UUID.randomUUID().toString());
        instance.setStateName(UUID.randomUUID().toString());
        instance.setStateTax(random.nextDouble());
        return instance;
    }

    @Test
    public void testHashCode() {
        State audit = new State();
        assertTrue(audit.hashCode() >= 0);
    }

    @Test
    public void testEqualsAnotherWay() {
        System.out.println("Equals Again.");

        Random random = new Random();
        State audita = new State();

        assertNotEquals(audita, null);

        State auditb = new State();

        assertEquals(auditb, auditb);
        assertEquals(audita, auditb);

        audita.setId(random.nextInt());

        assertNotEquals(audita, auditb);

        auditb.setId(audita.getId());

        assertEquals(audita, auditb);

        audita.setState(UUID.randomUUID().toString());

        assertNotEquals(audita, auditb);

        auditb.setState(audita.getState());

        assertEquals(audita, auditb);

        audita.setStateName(UUID.randomUUID().toString());

        assertNotEquals(audita, auditb);

        auditb.setStateName(audita.getStateName());

        assertEquals(audita, auditb);

        audita.setStateTax(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setStateTax(audita.getStateTax());

        assertEquals(audita, auditb);
    }

    public static State nonsenseStateGenerator(){
        State state = new State();
        state.setStateTax(new Random().nextDouble());
        state.setState(UUID.randomUUID().toString());
        return state;
    }
}