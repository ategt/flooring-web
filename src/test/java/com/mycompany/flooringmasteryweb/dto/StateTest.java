/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
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

        audita.setArea(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setArea(audita.getArea());

        assertEquals(auditb, auditb);

        audita.setCostPerSquareFoot(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setCostPerSquareFoot(audita.getCostPerSquareFoot());

        assertEquals(auditb, auditb);

        audita.setDate(new Date(random.nextLong()));

        assertNotEquals(audita, auditb);

        auditb.setDate(audita.getDate());

        assertEquals(auditb, auditb);

        audita.setId(random.nextInt());

        assertNotEquals(audita, auditb);

        auditb.setId(audita.getId());

        assertEquals(auditb, auditb);

        audita.setLaborCost(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setLaborCost(audita.getLaborCost());

        assertEquals(auditb, auditb);

        audita.setLaborCostPerSquareFoot(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setLaborCostPerSquareFoot(audita.getLaborCostPerSquareFoot());

        assertEquals(auditb, auditb);

        audita.setMaterialCost(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setMaterialCost(audita.getMaterialCost());

        assertEquals(auditb, auditb);
        audita.setName(UUID.randomUUID().toString());

        assertNotEquals(audita, auditb);

        auditb.setName(audita.getName());

        assertEquals(auditb, auditb);
        audita.setProduct(new Product());

        assertNotEquals(audita, auditb);

        auditb.setProduct(audita.getProduct());

        assertEquals(auditb, auditb);
        audita.setState(new State());

        assertNotEquals(audita, auditb);

        auditb.setState(audita.getState());

        assertEquals(auditb, auditb);
        audita.setTax(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setTax(audita.getTax());

        assertEquals(auditb, auditb);
        audita.setTaxRate(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setTaxRate(audita.getTaxRate());

        assertEquals(auditb, auditb);
        audita.setTotal(random.nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setTotal(audita.getTotal());

        assertEquals(auditb, auditb);
    }
}