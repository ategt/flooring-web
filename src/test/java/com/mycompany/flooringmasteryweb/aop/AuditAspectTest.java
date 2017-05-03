/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.aop;

import com.mycompany.flooringmasteryweb.dao.AuditDao;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dto.Order;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
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
        
        order.setDate(new Date());
        order.setId(-Math.abs(random.nextInt()));
        order.setTotal(random.nextDouble());
        order.setName(UUID.randomUUID().toString());

        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        orderDao.create(order);
        
        AuditDao auditDao = ctx.getBean("auditAspect", AuditDao.class);
        auditDao.
    }
}
