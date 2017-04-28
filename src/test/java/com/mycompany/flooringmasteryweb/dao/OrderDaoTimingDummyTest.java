/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.BasicOrder;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.State;
import java.util.Date;
import java.util.List;
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
public class OrderDaoTimingDummyTest {
    
    public OrderDaoTimingDummyTest() {
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
     * Test of create method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Order order = null;
        OrderDao instance = null;
        Order expResult = null;
        Order result = instance.create(order);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Order order = null;
        OrderDaoTimingDummy instance = null;
        instance.delete(order);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of extractDate method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testExtractDate() {
        System.out.println("extractDate");
        String dateString = "";
        OrderDaoTimingDummy instance = null;
        Date expResult = null;
        Date result = instance.extractDate(dateString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Integer id = null;
        OrderDaoTimingDummy instance = null;
        Order expResult = null;
        Order result = instance.get(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getList method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testGetList() {
        System.out.println("getList");
        OrderDaoTimingDummy instance = null;
        List<Order> expResult = null;
        List<Order> result = instance.getList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listOrderDates method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testListOrderDates() {
        System.out.println("listOrderDates");
        OrderDaoTimingDummy instance = null;
        List<Date> expResult = null;
        List<Date> result = instance.listOrderDates();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listOrderNumbers method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testListOrderNumbers() {
        System.out.println("listOrderNumbers");
        OrderDaoTimingDummy instance = null;
        List<Integer> expResult = null;
        List<Integer> result = instance.listOrderNumbers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of orderBuilder method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testOrderBuilder() {
        System.out.println("orderBuilder");
        BasicOrder basicOrder = null;
        OrderDaoTimingDummy instance = null;
        Order expResult = null;
        Order result = instance.orderBuilder(basicOrder);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of purgeTestFiles method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testPurgeTestFiles() {
        System.out.println("purgeTestFiles");
        OrderDaoTimingDummy instance = null;
        instance.purgeTestFiles();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resolveOrderCommand method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testResolveOrderCommand() {
        System.out.println("resolveOrderCommand");
        Order order = null;
        OrderDaoTimingDummy instance = null;
        OrderCommand expResult = null;
        OrderCommand result = instance.resolveOrderCommand(order);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByDate method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByDate() {
        System.out.println("searchByDate");
        Date date = null;
        OrderDaoTimingDummy instance = null;
        List<Order> expResult = null;
        List<Order> result = instance.searchByDate(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByName method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByName() {
        System.out.println("searchByName");
        String orderName = "";
        OrderDaoTimingDummy instance = null;
        List<Order> expResult = null;
        List<Order> result = instance.searchByName(orderName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByProduct method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByProduct() {
        System.out.println("searchByProduct");
        Product product = null;
        OrderDaoTimingDummy instance = null;
        List<Order> expResult = null;
        List<Order> result = instance.searchByProduct(product);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByState method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByState() {
        System.out.println("searchByState");
        State state = null;
        OrderDaoTimingDummy instance = null;
        List<Order> expResult = null;
        List<Order> result = instance.searchByState(state);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        OrderDaoTimingDummy instance = null;
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sortByOrderNumber method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSortByOrderNumber() {
        System.out.println("sortByOrderNumber");
        List<Order> orders = null;
        OrderDaoTimingDummy instance = null;
        List<Order> expResult = null;
        List<Order> result = instance.sortByOrderNumber(orders);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testToString_Order() {
        System.out.println("toString");
        Order order = null;
        OrderDaoTimingDummy instance = null;
        String expResult = "";
        String result = instance.toString(order);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testToString_Order_String() {
        System.out.println("toString");
        Order order = null;
        String TOKEN = "";
        OrderDaoTimingDummy instance = null;
        String expResult = "";
        String result = instance.toString(order, TOKEN);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testToString_3args() {
        System.out.println("toString");
        Order order = null;
        String TOKEN = "";
        String CSV_ESCAPE = "";
        OrderDaoTimingDummy instance = null;
        String expResult = "";
        String result = instance.toString(order, TOKEN, CSV_ESCAPE);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Order order = null;
        OrderDaoTimingDummy instance = null;
        instance.update(order);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByOrderNumber method, of class OrderDaoTimingDummy.
     */
    @Test
    public void testSearchByOrderNumber() {
        System.out.println("searchByOrderNumber");
        Integer orderNumber = null;
        OrderDaoTimingDummy instance = null;
        List<Order> expResult = null;
        List<Order> result = instance.searchByOrderNumber(orderNumber);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
