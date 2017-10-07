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
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testCreateAuditEntry() {
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
        int lastAuditPosition = 1;
        Audit lastAudit = auditDao.getResultRange(lastAuditPosition - 1, 1).get(0);

        assertTrue("Order Creation Truth: " + (lastAudit.getDate().getTime() - order.getDate().getTime()), Math.abs(lastAudit.getDate().getTime() - order.getDate().getTime()) - 25000 < 50);

        assertEquals("Action Performed Check", lastAudit.getActionPerformed(), "create");
        assertEquals("Order ID Check", lastAudit.getOrderid(), (Object)order.getId());
        assertTrue("Log Time Check: " + Math.abs(lastAudit.getLogDate().getTime() - System.currentTimeMillis()), Math.abs(lastAudit.getLogDate().getTime() - System.currentTimeMillis()) < 95);
        assertEquals("Order Name Check", lastAudit.getOrderName(), order.getName());
        assertEquals("Total Check", lastAudit.getOrderTotal(), order.getTotal(), 100);

    }
}
