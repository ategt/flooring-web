/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Audit;
import java.util.ArrayList;
import java.util.Date;
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
public class AuditDaoTest {

    ApplicationContext ctx;
    AuditDao instance;

    public AuditDaoTest() {
        ctx = new ClassPathXmlApplicationContext("testAuditDb-DedicatedApplicationContext.xml");
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = ctx.getBean("auditDao", AuditDao.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of create method, of class AuditDao.
     */
    @Test
    public void testCreateNull() {
        System.out.println("create");
        Audit audit = null;

        Audit expResult = null;
        Audit result = instance.create(audit);
        assertEquals(expResult, result);
    }

    /**
     * Test of create method, of class AuditDao.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Audit audit = new Audit();
        Audit result = instance.create(audit);
        assertTrue(result.getId() > 0);
    }

    @Test
    public void testCreateWithGenerator() {
        System.out.println("create");
        Audit audit = auditGenerator();

        Audit result = instance.create(audit);
        assertEquals(audit, result);
    }

    @Test
    public void testCreateAndGet() {
        System.out.println("create and get");
        Audit audit = auditGenerator();

        int size = instance.getSize();
        Audit result = instance.create(audit);
        assertEquals(audit, result);

        assertEquals(size + 1, instance.getSize());
        assertEquals(size + 1, instance.getSize());

        Audit returned = instance.get(result.getId());
        assertTrue(returned.equals(result));
        assertEquals(returned, result);

        assertEquals(size + 1, instance.getSize());

        List<Audit> audits = instance.get();

        assertTrue(audits.contains(returned));
        assertTrue(audits.contains(result));
        assertEquals(audits.size(), instance.getSize());
    }

    @Test
    public void testPagination() {
        
        if (instance.getSize() < 25) {
            List<Audit> createdAudits = new ArrayList();
            for (int i = 0; i < 25; i++) {
                createdAudits.add(instance.create(auditGenerator()));
            }
        }
        
        List<Audit> audits = instance.get();

        int totalSize = audits.size();
        List<Audit> firstAudits = instance.getResultRange(0, 10);
        List<Audit> secondAudits = instance.getResultRange(10, 10);
        List<Audit> thirdAudits = instance.getResultRange(20, 10);

        List<Audit> results = instance.getResultRange(20, 16);
        int resultsSize = results.size();
        assertEquals(resultsSize, 16);

        List<Audit> cummulativeAudits = new ArrayList();
        cummulativeAudits.addAll(firstAudits);
        cummulativeAudits.addAll(secondAudits);
        cummulativeAudits.addAll(thirdAudits);

        assertEquals(cummulativeAudits.size(), 30);

        for (int i = 0; i < cummulativeAudits.size(); i++) {
            assertEquals(cummulativeAudits.get(i), audits.get(i));
        }

        List<Audit> paginatedAudits = instance.getWithPagination(2, 10);

        assertEquals(paginatedAudits.size(), 10);

        for (int i = 0; i < paginatedAudits.size(); i++) {
            assertEquals(paginatedAudits.get(i), thirdAudits.get(i));
        }

        List<Audit> endingAudits = instance.getResultRange(totalSize - 5, 10);
        assertEquals(endingAudits.size(), 5);

        for (int i = 0; i < endingAudits.size(); i++) {
            assertEquals(endingAudits.get(i), audits.get(audits.size() - 5 + i));
        }
    }

    private Audit auditGenerator() {

        Audit instance = new Audit();
        Random random = new Random();
        instance.setActionPerformed("action");
        instance.setDate(new Date(220000000L));
        instance.setId(random.nextInt());
        instance.setLogDate(new Date(32500000L));
        instance.setOrderName("Order Name");
        instance.setOrderTotal(random.nextInt(20000) / 100);
        instance.setOrderid(random.nextInt(10000) / 100);
        return instance;
    }
}