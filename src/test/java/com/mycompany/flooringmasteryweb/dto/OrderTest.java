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
public class OrderTest {

    public OrderTest() {
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
     * Test of equals method, of class Order.
     */
    @Test
    public void testEqualsWithNull() {
        System.out.println("equals");
        Object object = null;
        Order instance = new Order();
        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithNew() {
        System.out.println("equals");
        Order object = new Order();
        Order instance = orderGenerator();

        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithTwoNew() {
        System.out.println("equals");
        Order object = new Order();
        Order instance = new Order();

        boolean expResult = true;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithRandom() {
        System.out.println("equals");
        Order object = new Order();
        for (int i = 0; i < 50; i++) {

            Order instance = orderGenerator();

            boolean expResult = false;
            boolean result = instance.equals(object);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithOtherRandom() {
        System.out.println("equals");
        Order object = new Order();
        for (int i = 0; i < 50; i++) {

            Order instance = orderGenerator();

            boolean expResult = false;
            boolean result = object.equals(instance);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithSelf() {
        System.out.println("equals");
        Order object = new Order();

        boolean expResult = true;
        boolean result = object.equals(object);
        assertEquals(expResult, result);
    }

    private Order orderGenerator() {
        Order instance = new Order();
        Random random = new Random();
        instance.setArea(random.nextDouble());
        instance.setCostPerSquareFoot(random.nextDouble());
        instance.setDate(new Date(random.nextLong()));
        instance.setId(random.nextInt());
        instance.setLaborCost(random.nextDouble());
        instance.setLaborCostPerSquareFoot(random.nextDouble());
        instance.setMaterialCost(random.nextDouble());
        instance.setName(UUID.randomUUID().toString());
        instance.setProduct(new Product());
        instance.setState(new State());
        instance.setTax(random.nextDouble());
        instance.setTaxRate(random.nextDouble());
        instance.setTotal(random.nextDouble());
        return instance;
    }

    @Test
    public void testHashCode() {
        Order audit = new Order();
        assertTrue(audit.hashCode() >= 0);
    }

    @Test
    public void testEqualsAnotherWay() {
        System.out.println("Equals Again.");

        Random random = new Random();
        Order ordera = new Order();

        assertNotEquals(ordera, null);

        Order orderb = new Order();

        assertEquals(orderb, orderb);
        assertEquals(ordera, orderb);

        ordera.setArea(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setArea(ordera.getArea());

        assertEquals(ordera, orderb);

        ordera.setCostPerSquareFoot(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setCostPerSquareFoot(ordera.getCostPerSquareFoot());

        assertEquals(ordera, orderb);

        ordera.setDate(new Date(random.nextLong()));

        assertNotEquals(ordera, orderb);

        orderb.setDate(ordera.getDate());

        assertEquals(ordera, orderb);

        ordera.setId(random.nextInt());

        assertNotEquals(ordera, orderb);

        orderb.setId(ordera.getId());

        assertEquals(ordera, orderb);

        ordera.setLaborCost(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setLaborCost(ordera.getLaborCost());

        assertEquals(ordera, orderb);

        ordera.setLaborCostPerSquareFoot(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setLaborCostPerSquareFoot(ordera.getLaborCostPerSquareFoot());

        assertEquals(ordera, orderb);

        ordera.setMaterialCost(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setMaterialCost(ordera.getMaterialCost());

        assertEquals(ordera, orderb);
        ordera.setName(UUID.randomUUID().toString());

        assertNotEquals(ordera, orderb);

        orderb.setName(ordera.getName());

        assertEquals(ordera, orderb);
        ordera.setProduct(new Product());

        assertNotEquals(ordera, orderb);

        orderb.setProduct(ordera.getProduct());

        assertEquals(ordera, orderb);
        ordera.setState(new State());

        assertNotEquals(ordera, orderb);

        orderb.setState(ordera.getState());

        assertEquals(ordera, orderb);
        ordera.setTax(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setTax(ordera.getTax());

        assertEquals(ordera, orderb);
        ordera.setTaxRate(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setTaxRate(ordera.getTaxRate());

        assertEquals(ordera, orderb);
        ordera.setTotal(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setTotal(ordera.getTotal());

        assertEquals(ordera, orderb);
    }
}