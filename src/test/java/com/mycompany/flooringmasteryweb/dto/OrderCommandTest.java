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
public class OrderCommandTest {

    public OrderCommandTest() {
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
     * Test of equals method, of class OrderCommand.
     */
    @Test
    public void testEqualsWithNull() {
        System.out.println("equals");
        Object object = null;
        OrderCommand instance = new OrderCommand();
        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithNew() {
        System.out.println("equals");
        OrderCommand object = new OrderCommand();
        OrderCommand instance = orderGenerator();

        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithTwoNew() {
        System.out.println("equals");
        OrderCommand object = new OrderCommand();
        OrderCommand instance = new OrderCommand();

        boolean expResult = true;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithRandom() {
        System.out.println("equals");
        OrderCommand object = new OrderCommand();
        for (int i = 0; i < 50; i++) {

            OrderCommand instance = orderGenerator();

            boolean expResult = false;
            boolean result = instance.equals(object);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithOtherRandom() {
        System.out.println("equals");
        OrderCommand object = new OrderCommand();
        for (int i = 0; i < 50; i++) {

            OrderCommand instance = orderGenerator();

            boolean expResult = false;
            boolean result = object.equals(instance);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithSelf() {
        System.out.println("equals");
        OrderCommand object = new OrderCommand();

        boolean expResult = true;
        boolean result = object.equals(object);
        assertEquals(expResult, result);
    }

    private OrderCommand orderGenerator() {
        OrderCommand instance = new OrderCommand();
        Random random = new Random();
        instance.setArea(random.nextDouble());
        instance.setDate(new Date(random.nextLong()));
        instance.setId(random.nextInt());
        instance.setName(UUID.randomUUID().toString());
        instance.setProduct(UUID.randomUUID().toString());
        instance.setState(UUID.randomUUID().toString());
        return instance;
    }

    @Test
    public void testHashCode() {
        OrderCommand audit = new OrderCommand();
        assertTrue(audit.hashCode() >= 0);
    }

    @Test
    public void testEqualsAnotherWay() {
        System.out.println("Equals Again.");

        Random random = new Random();
        OrderCommand ordera = new OrderCommand();

        assertNotEquals(ordera, null);

        OrderCommand orderb = new OrderCommand();

        assertEquals(orderb, orderb);
        assertEquals(ordera, orderb);

        ordera.setArea(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setArea(ordera.getArea());

        assertEquals(ordera, orderb);

        ordera.setDate(new Date(random.nextLong()));

        assertNotEquals(ordera, orderb);

        orderb.setDate(ordera.getDate());

        assertEquals(ordera, orderb);

        ordera.setId(random.nextInt());

        assertNotEquals(ordera, orderb);

        orderb.setId(ordera.getId());

        assertEquals(ordera, orderb);

        ordera.setName(UUID.randomUUID().toString());

        assertNotEquals(ordera, orderb);

        orderb.setName(ordera.getName());

        assertEquals(ordera, orderb);

        ordera.setProduct(UUID.randomUUID().toString());

        assertNotEquals(ordera, orderb);

        orderb.setProduct(ordera.getProduct());

        assertEquals(ordera, orderb);

        ordera.setState(UUID.randomUUID().toString());

        assertNotEquals(ordera, orderb);

        orderb.setState(ordera.getState());

        assertEquals(ordera, orderb);
    }
}