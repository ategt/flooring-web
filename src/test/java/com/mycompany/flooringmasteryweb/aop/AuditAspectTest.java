/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.aop;

import com.mycompany.flooringmasteryweb.dao.AuditDao;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dto.Audit;
import com.mycompany.flooringmasteryweb.dto.Order;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ATeg
 */
public class AuditAspectTest {

    ApplicationContext ctx;

    public AuditAspectTest() {
        ctx = new ClassPathXmlApplicationContext("testAuditAspectDb-DedicatedApplicationContext.xml");
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
     * Test of createAuditEntry method, of class AuditAspect.
     */
    @Test
    public void testCreateAuditEntry() throws Exception {
        System.out.println("createAuditEntry");

        Random random = new Random();

        Order order = new Order();

        order.setDate(new Date(System.currentTimeMillis() - 25000));
        order.setId(-Math.abs(random.nextInt()));
        order.setTotal(random.nextDouble());
        order.setName(UUID.randomUUID().toString());

        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        orderDao.create(order);

        AuditDao auditDao = ctx.getBean("auditDao", AuditDao.class);
        int lastAuditPosition = auditDao.getSize();
        Audit lastAudit = auditDao.getResultRange(lastAuditPosition - 1, 1).get(0);

        assertTrue(Math.abs(lastAudit.getDate().getTime() - order.getDate().getTime()) - 25000 < 50);

        assertEquals(lastAudit.getActionPerformed(), "create");
        assertEquals(lastAudit.getOrderid(), order.getId());
        assertTrue(Math.abs(lastAudit.getLogDate().getTime() - System.currentTimeMillis()) < 50);
        assertEquals(lastAudit.getOrderName(), order.getName());
        assertEquals(lastAudit.getOrderTotal(), order.getTotal(), 100);

    }
}
