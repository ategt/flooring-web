/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import org.junit.*;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 *
 * @author ATeg
 */
public class AuditTest {

    public AuditTest() {
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
     * Test of equals method, of class Audit.
     */
    @Test
    public void testEqualsWithNull() {
        System.out.println("equals");
        Object object = null;
        Audit instance = new Audit();
        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithNew() {
        System.out.println("equals");
        Audit object = new Audit();
        Audit instance = auditGenerator();

        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithTwoNew() {
        System.out.println("equals");
        Audit object = new Audit();
        Audit instance = new Audit();

        boolean expResult = true;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithRandom() {
        System.out.println("equals");
        Audit object = new Audit();
        for (int i = 0; i < 50; i++) {

            Audit instance = auditGenerator();

            boolean expResult = false;
            boolean result = instance.equals(object);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithOtherRandom() {
        System.out.println("equals");
        Audit object = new Audit();
        for (int i = 0; i < 50; i++) {

            Audit instance = auditGenerator();

            boolean expResult = false;
            boolean result = object.equals(instance);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithSelf() {
        System.out.println("equals");
        Audit object = new Audit();

        boolean expResult = true;
        boolean result = object.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithExample() {
        System.out.println("equals");
        Audit firstAudit = new Audit();
        firstAudit.setId(11);
        firstAudit.setDate(new Date());
        firstAudit.setOrderid(62);

        firstAudit.setActionPerformed("action");
        firstAudit.setLogDate(new Date());
        firstAudit.setOrderName("Order name");
        firstAudit.setOrderTotal(103.0);

        Audit secondAudit = new Audit();
        secondAudit.setId(11);
        secondAudit.setDate(new Date());
        secondAudit.setOrderid(62);

        secondAudit.setActionPerformed("action");
        secondAudit.setLogDate(new Date());
        secondAudit.setOrderName("Order name");
        secondAudit.setOrderTotal(103.0);

        boolean expResult = true;
        boolean result = firstAudit.equals(secondAudit);
        assertEquals(expResult, result);
        assertEquals(secondAudit, firstAudit);
    }

    private Audit auditGenerator() {
        Audit instance = new Audit();
        Random random = new Random();
        instance.setActionPerformed(UUID.randomUUID().toString());
        instance.setDate(new Date(random.nextLong()));
        instance.setId(random.nextInt());
        instance.setLogDate(new Date(random.nextLong()));
        instance.setOrderName(UUID.randomUUID().toString());
        instance.setOrderTotal(random.nextDouble());
        instance.setOrderid(random.nextInt());
        return instance;
    }

    @Test
    public void testHashCode() {
        Audit audit = new Audit();
        assertTrue(audit.hashCode() >= 0);
    }

    @Test
    public void testEqualsAnotherWay() {
        System.out.println("Equals Again.");

        Audit audita = new Audit();
        Audit auditb = new Audit();

        assertEquals(auditb, auditb);

        audita.setActionPerformed(UUID.randomUUID().toString());

        assertNotEquals(audita, auditb);

        auditb.setActionPerformed(audita.getActionPerformed());

        assertEquals(auditb, auditb);

        audita.setDate(new Date());

        assertNotEquals(audita, auditb);

        auditb.setDate(audita.getDate());

        assertEquals(auditb, auditb);

        audita.setId(new Random().nextInt());

        assertNotEquals(audita, auditb);

        auditb.setId(audita.getId());

        assertEquals(auditb, auditb);

        audita.setLogDate(new Date());

        assertNotEquals(audita, auditb);

        auditb.setLogDate(audita.getLogDate());

        assertEquals(auditb, auditb);

        audita.setOrderName(UUID.randomUUID().toString());

        assertNotEquals(audita, auditb);

        auditb.setOrderName(audita.getOrderName());

        assertEquals(auditb, auditb);

        audita.setOrderTotal(new Random().nextDouble());

        assertNotEquals(audita, auditb);

        auditb.setOrderTotal(audita.getOrderTotal());

        assertEquals(auditb, auditb);

        audita.setOrderid(new Random().nextInt());

        assertNotEquals(audita, auditb);

        auditb.setOrderid(audita.getOrderid());

        assertEquals(auditb, auditb);
    }
}