/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.*;
import com.mycompany.flooringmasteryweb.utilities.ProductUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class OrderDaoDbImplTest {

    ApplicationContext ctx;
    Integer idOfAPatOrder;

    public OrderDaoDbImplTest() {
        ctx = new ClassPathXmlApplicationContext("test-OrdersSQLStateSQLProductSQL-applicationContext.xml");
        idOfAPatOrder = ctx.getBean("idOfAPatOrder", Integer.class);
        establishFixtures();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public void establishFixtures() {
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);
        if (Objects.nonNull(instance) && instance.size() < 200) {
            for (int i = 0; i < 200; i++) {
                instance.create(OrderTest.orderGenerator());
            }
        }
    }

    @Test
    public void testCreate() {
        System.out.println("create");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);
        Order expResult = order;
        Order result = instance.create(order);
        assertEquals(expResult, result);

        int id = result.getId();
        assertTrue(result.getId() != 0);
        assertTrue(result.getId() >= instance.size());

        // Test get method.
        Order returnedOrder = instance.get(id);
        assertTrue(OrderTest.verifyOrder(returnedOrder, result));
        instance.delete(order);

        returnedOrder = instance.get(id);
        assertEquals(returnedOrder, null);
    }

    @Test
    public void testCreateAndDeleteWithId() {
        System.out.println("create");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);
        Order expResult = order;
        Order result = instance.create(order);
        assertEquals(expResult, result);

        int id = result.getId();
        assertTrue(result.getId() != 0);
        assertTrue(result.getId() >= instance.size());

        // Test get method.
        Order returnedOrder = instance.get(id);
        assertTrue(OrderTest.verifyOrder(returnedOrder, result));
        instance.delete(id);

        returnedOrder = instance.get(id);
        assertEquals(returnedOrder, null);
    }

    @Test
    public void testNullCreate() {
        System.out.println("create");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = null;
        Order expResult = order;
        Order result = instance.create(order);

        //This is a test create with nulls.
        // If it makes it to here, it passed.
        assertTrue(true);

    }

    @Test
    public void testNullCreateB() {
        System.out.println("create");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);
        Order expResult = order;
        Order result = instance.create(order);
        assertEquals(expResult, result);

        int id = result.getId();
        assertTrue(result.getId() != 0);
        assertTrue(result.getId() >= instance.size());

        Order orderNull = null;
        Order resultNull = instance.create(orderNull);

        assertNull(resultNull);
        assertNull(orderNull);
        assertEquals(orderNull, resultNull);

        // Test get method.
        Order returnedOrder = instance.get(id);
        assertTrue(OrderTest.verifyOrder(returnedOrder, result));
        instance.delete(order);

        returnedOrder = instance.get(id);
        assertEquals(returnedOrder, null);

        //This is a test update with nulls.
        // If it makes it to here, it passed.
        assertTrue(true);

    }

    @Test
    public void testNullDelete() {
        System.out.println("delete");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);
        Order expResult = order;
        Order result = instance.create(order);
        assertEquals(expResult, result);

        int id = result.getId();
        assertTrue(result.getId() != 0);
        assertTrue(result.getId() >= instance.size());

        // Test get method.
        Order returnedOrder = instance.get(id);
        assertTrue(OrderTest.verifyOrder(returnedOrder, result));
        instance.delete((Order)null);
        instance.delete((Integer)null);
        instance.delete(order);
        returnedOrder = instance.get(id);
        assertEquals(returnedOrder, null);
    }

    @Test
    public void testNullUpdate() {
        System.out.println("delete");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);
        Order expResult = order;
        Order result = instance.create(order);
        assertEquals(expResult, result);

        int id = result.getId();
        assertTrue(result.getId() != 0);
        assertTrue(result.getId() >= instance.size());

        // Test get method.
        Order returnedOrder = instance.get(id);
        assertTrue(OrderTest.verifyOrder(returnedOrder, result));
        instance.update(null);
        instance.delete(order);
        returnedOrder = instance.get(id);
        assertEquals(returnedOrder, null);

        //This is a test update with nulls.
        // If it makes it to here, it passed.
        assertTrue(true);

    }

    @Test
    public void testNullGet() {
        System.out.println("delete");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);
        Order expResult = order;
        Order result = instance.create(order);
        assertEquals(expResult, result);

        int id = result.getId();
        assertTrue(result.getId() != 0);
        assertTrue(result.getId() >= instance.size());

        // Test get method.
        Order returnedOrder = instance.get(null);
        assertNull(returnedOrder);
        //instance.delete(null);
        instance.delete(order);

        returnedOrder = instance.get(id);
        assertEquals(returnedOrder, null);
    }

    @Test
    public void testNullGetB() {
        System.out.println("delete");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order returnedOrder = instance.get(null);
        assertNull(returnedOrder);

    }

    @Test
    public void testNullUpdateB() {
        System.out.println("update");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        instance.update(null);
        // If it got here, it passes.
        assertTrue(true);

    }

    @Test
    public void testCreateB() {
        System.out.println("create - null");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order order = null;
        Order expResult = null;
        OrderCommand result = instance.resolveOrderCommand(order);

        assertEquals(expResult, result);

    }

    @Test
    public void testCreateC() {
        System.out.println("create - null");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        OrderCommand order = null;
        Order expResult = null;
        Order result = instance.orderBuilder(order);

        assertEquals(expResult, result);

    }

    /**
     * Test of getAllOrderes method, of class OrderDao.
     */
    @Test
    public void testGetAllOrderes() {
        System.out.println("getAllOrderes");

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao instance = ctx.getBean("orderDao", OrderDao.class);

        Order orderOne = OrderTest.orderFactory(ctx);
        Order orderTwo = OrderTest.orderFactory(ctx);
        Order orderThree = OrderTest.orderFactory(ctx);
        Order orderFour = OrderTest.orderFactory(ctx);

        Date firstDate = new Date();
        Date secondDate = new Date();
        Date thirdDate = new Date();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.roll(java.util.Calendar.DAY_OF_MONTH, -3);
        thirdDate = calendar.getTime();

        calendar.roll(java.util.Calendar.DAY_OF_MONTH, +8);
        Date fourthDate = calendar.getTime();

        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyyMMdd");

        try {
            secondDate = fmt.parse(fmt.format(secondDate));
        } catch (ParseException ex) {
            fail("Parse Exception - " + ex.getMessage());
        }

        try {
            firstDate = fmt.parse(fmt.format(firstDate));
        } catch (ParseException ex) {
            fail("Parse Exception - " + ex.getMessage());
        }

        try {
            thirdDate = fmt.parse(fmt.format(thirdDate));
        } catch (ParseException ex) {
            fail("Parse Exception - " + ex.getMessage());
        }

        try {
            fourthDate = fmt.parse(fmt.format(fourthDate));
        } catch (ParseException ex) {
            fail("Parse Exception - " + ex.getMessage());
        }

        orderOne.setDate(firstDate);
        orderTwo.setDate(secondDate);
        orderThree.setDate(thirdDate);
        orderFour.setDate(fourthDate);

        instance.create(orderOne);
        instance.create(orderTwo);
        instance.create(orderThree);
        instance.create(orderFour);

        List<Order> testList = instance.list(null);

        assertTrue(OrderTest.isOrderInList(orderOne, testList));
        assertTrue(OrderTest.isOrderInList(orderTwo, testList));
        assertTrue(OrderTest.isOrderInList(orderThree, testList));
        assertTrue(OrderTest.isOrderInList(orderFour, testList));

        List<Order> result = instance.searchByDate(firstDate);

        assertEquals(false, result.isEmpty());

        assertEquals(true, OrderTest.isOrderInList(orderOne, result));
        assertEquals(true, OrderTest.isOrderInList(orderTwo, result));
        assertEquals(false, OrderTest.isOrderInList(orderThree, result));
        assertEquals(false, OrderTest.isOrderInList(orderFour, result));

        instance.delete(orderOne);
        instance.delete(orderTwo);
        instance.delete(orderThree);
        instance.delete(orderFour);

        List<Order> orderList = instance.list(null);

        assertEquals(orderList.contains(orderOne), false);
        assertEquals(orderList.contains(orderTwo), false);
        assertEquals(orderList.contains(orderThree), false);
        assertEquals(orderList.contains(orderFour), false);
    }

    @Test
    public void testEncodeAndDecode() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("OH");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Wood");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Wise";
        double taxRate = 6.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 61.88;
        double total = 1051.88;

        // Set the above values to the appropriate attributes.
        //order.setId(1);
        order.setName(name);
        order.setState(ohio);
        order.setTaxRate(taxRate);
        order.setProduct(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);

        // Use the update method to save this new text to file.
        orderDao.update(order);

        // Load a new instance of the OrderDao.
        //OrderDao secondDao = new OrderDao(true);
        ProductDao secondProductDao = ctx.getBean("productDao", ProductDao.class);
        StateDao secondStateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao secondOrderDao = ctx.getBean("orderDao", OrderDao.class);

        // Pull a order  using the id number recorded earlier.
        Order thirdOrder = secondOrderDao.get(id);

        assertTrue(thirdOrder != null);

        // Check that the update method saved the new text.
        assertEquals(name, thirdOrder.getName());
        assertEquals(ohio.getState(), thirdOrder.getState().getState());
        assertEquals(taxRate, thirdOrder.getTaxRate(), 1e-8);
        assertEquals(product.getType(), thirdOrder.getProduct().getType());
        assertEquals(area, thirdOrder.getArea(), 1e-8);
        assertEquals(costPerSquareFoot, thirdOrder.getCostPerSquareFoot(), 1e-8);
        assertEquals(laborCostPerSquareFoot, thirdOrder.getLaborCostPerSquareFoot(), 1e-8);
        assertEquals(materialCost, thirdOrder.getMaterialCost(), 1e-8);
        assertEquals(laborCost, thirdOrder.getLaborCost(), 1e-8);
        assertEquals(tax, thirdOrder.getTax(), 1e-8);
        assertEquals(total, thirdOrder.getTotal(), 1e-8);

        // Delete the test order.
        secondOrderDao.delete(thirdOrder);

        // Load a third instance of the Dao and verify that 
        // the order was deleted from the file.
        ProductDao thirdProductDao = ctx.getBean("productDao", ProductDao.class);
        StateDao thirdStateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao thirdOrderDao = ctx.getBean("orderDao", OrderDao.class);

        //OrderDao thirdDao = new OrderDao(true);
        assertEquals(thirdOrderDao.get(id), null);

    }

    @Test
    public void testEncodeAndDecodeWithCommas() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        // The true parameter in the Order Dao constructor signifies a test.
        //OrderDao orderDao = new OrderDao(true);
        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("OH");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Wood");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Acme, INC.";
        double taxRate = 6.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 61.88;
        double total = 1051.88;

        // Set the above values to the appropriate attributes.
        order.setName(name);
        order.setState(ohio);
        order.setTaxRate(taxRate);
        order.setProduct(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);

        // Use the update method to save this new text to file.
        orderDao.update(order);

        // Load a new instance of the OrderDao.
        //OrderDao secondDao = new OrderDao(true);
        ProductDao secondProductDao = ctx.getBean("productDao", ProductDao.class);
        StateDao secondStateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao secondOrderDao = ctx.getBean("orderDao", OrderDao.class);

        // Pull a order  using the id number recorded earlier.
        Order thirdOrder = secondOrderDao.get(id);

        assertTrue(thirdOrder != null);

        // Check that the update method saved the new text.
        assertEquals(name, thirdOrder.getName());
        assertEquals(ohio.getState(), thirdOrder.getState().getState());
        assertEquals(taxRate, thirdOrder.getTaxRate(), 1e-8);
        assertEquals(product.getType(), thirdOrder.getProduct().getType());
        assertEquals(area, thirdOrder.getArea(), 1e-8);
        assertEquals(costPerSquareFoot, thirdOrder.getCostPerSquareFoot(), 1e-8);
        assertEquals(laborCostPerSquareFoot, thirdOrder.getLaborCostPerSquareFoot(), 1e-8);
        assertEquals(materialCost, thirdOrder.getMaterialCost(), 1e-8);
        assertEquals(laborCost, thirdOrder.getLaborCost(), 1e-8);
        assertEquals(tax, thirdOrder.getTax(), 1e-8);
        assertEquals(total, thirdOrder.getTotal(), 1e-8);

        // Delete the test order.
        secondOrderDao.delete(thirdOrder);

        // Load a third instance of the Dao and verify that 
        // the order was deleted from the file.
        ProductDao thirdProductDao = ctx.getBean("productDao", ProductDao.class);
        StateDao thirdStateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao thirdOrderDao = ctx.getBean("orderDao", OrderDao.class);

        assertEquals(thirdOrderDao.get(id), null);
    }

    @Test
    public void testEncodeAndDecodeWithDate() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("IN");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Steel");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Bob and sons, perfection.";
        double taxRate = 3.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 3061.88;
        double total = 4051.88;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        // Set the above values to the appropriate attributes.
        order.setName(name);
        order.setState(ohio);
        order.setTaxRate(taxRate);
        order.setProduct(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        order.setDate(orderDate);
        // Use the update method to save this new text to file.
        orderDao.update(order);

        // Load a new instance of the OrderDao.
        //OrderDao secondDao = new OrderDao(true);
        ProductDao secondProductDao = ctx.getBean("productDao", ProductDao.class);
        StateDao secondStateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao secondOrderDao = ctx.getBean("orderDao", OrderDao.class);

        // Pull a order  using the id number recorded earlier.
        Order thirdOrder = secondOrderDao.get(id);

        assertTrue(thirdOrder != null);

        // Check that the update method saved the new text.
        assertEquals(name, thirdOrder.getName());
        assertEquals(ohio.getState(), thirdOrder.getState().getState());
        assertEquals(taxRate, thirdOrder.getTaxRate(), 1e-8);
        assertEquals(product.getType(), thirdOrder.getProduct().getType());
        assertEquals(area, thirdOrder.getArea(), 1e-8);
        assertEquals(costPerSquareFoot, thirdOrder.getCostPerSquareFoot(), 1e-8);
        assertEquals(laborCostPerSquareFoot, thirdOrder.getLaborCostPerSquareFoot(), 1e-8);
        assertEquals(materialCost, thirdOrder.getMaterialCost(), 1e-8);
        assertEquals(laborCost, thirdOrder.getLaborCost(), 1e-8);
        assertEquals(tax, thirdOrder.getTax(), 1e-8);
        assertEquals(total, thirdOrder.getTotal(), 1e-8);

        // Delete the test order.
        secondOrderDao.delete(thirdOrder);

        // Load a third instance of the Dao and verify that 
        // the order was deleted from the file.
        ProductDao thirdProductDao = ctx.getBean("productDao", ProductDao.class);
        StateDao thirdStateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao thirdOrderDao = ctx.getBean("orderDao", OrderDao.class);

        assertEquals(thirdOrderDao.get(id), null);
    }

    @Test
    public void testResolverZ() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);
    }

    @Test
    public void testResolverA() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("IN");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Steel");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Bob and sons, perfection.";
        double taxRate = 3.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 3061.88;
        double total = 4051.88;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        // Set the above values to the appropriate attributes.
        order.setName(name);
        order.setState(ohio);
        order.setTaxRate(taxRate);
        order.setProduct(null);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        order.setDate(orderDate);
        // Use the update method to save this new text to file.
        orderDao.update(order);

        OrderCommand basicOrder = orderDao.resolveOrderCommand(order);

        assertNotNull(basicOrder);
    }

    @Test
    public void testResolverB() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("IN");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Steel");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Bob and sons, perfection.";
        double taxRate = 3.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 3061.88;
        double total = 4051.88;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        // Set the above values to the appropriate attributes.
        order.setName(null);
        order.setState(ohio);
        order.setTaxRate(taxRate);
        order.setProduct(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        order.setDate(orderDate);
        // Use the update method to save this new text to file.
        orderDao.update(order);

        OrderCommand basicOrder = orderDao.resolveOrderCommand(order);

        assertNotNull(basicOrder);
    }

    @Test
    public void testResolverC() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("IN");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Steel");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Bob and sons, perfection.";
        double taxRate = 3.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 3061.88;
        double total = 4051.88;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        order.setName(name);
        order.setState(ohio);
        order.setTaxRate(taxRate);
        order.setProduct(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        order.setDate(null);
        // Use the update method to save this new text to file.
        orderDao.update(order);

        OrderCommand basicOrder = orderDao.resolveOrderCommand(order);

        assertNotNull(basicOrder);
    }

    @Test
    public void testResolverD() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("IN");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Steel");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Bob and sons, perfection.";
        double taxRate = 3.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 3061.88;
        double total = 4051.88;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        order.setName(name);
        order.setState(null);
        order.setTaxRate(taxRate);
        order.setProduct(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        order.setDate(orderDate);
        // Use the update method to save this new text to file.
        orderDao.update(order);

        OrderCommand basicOrder = orderDao.resolveOrderCommand(order);

        assertNotNull(basicOrder);
    }

    @Test
    public void testResolverF() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        Order order = OrderTest.orderFactory(ctx);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(order);

        // Record the orders id number.
        int id = order.getId();

        // Verify that the order object that the create method passed back
        // was the same one it was given.
        assertEquals(order, returnedOrder);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("IN");
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Steel");
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "Bob and sons, perfection.";
        double taxRate = 3.25;
        double area = 100.00;
        double costPerSquareFoot = 5.15;
        double laborCostPerSquareFoot = 4.75;
        double materialCost = 515.00;
        double laborCost = 475.00;
        double tax = 3061.88;
        double total = 4051.88;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        order.setName(name);
        order.setState(null);
        order.setTaxRate(taxRate);
        order.setProduct(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        order.setDate(orderDate);
        // Use the update method to save this new text to file.
        orderDao.update(order);

        OrderCommand basicOrder = orderDao.resolveOrderCommand(order);

        assertNotNull(basicOrder);
    }

    @Test
    public void testResolverEBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("TestSteel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        int id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());

        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());

        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);
    }

    @Test
    public void testResolverFBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("TestSteel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(null);
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        Integer id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());

        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());

        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);

    }

    @Test
    public void testResolverGBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("TestSteel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(null);
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        Integer id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);
    }

    @Test
    public void testResolverHBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("TestSteel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(null);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        int id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);

    }

    @Test
    public void testResolverIBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(null);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals(orderCommand.getProduct(), basicOrder.getProduct());
        assertEquals(orderCommand.getState(), basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);
    }

    @Test
    public void testResolverLBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        int id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals(orderCommand.getProduct(), basicOrder.getProduct());
        assertEquals(orderCommand.getState(), basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);
    }

    @Test
    public void testResolverJBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        int id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals(orderCommand.getProduct(), basicOrder.getProduct());
        assertEquals(orderCommand.getState(), basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);
    }

    @Test
    public void testResolverKBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        int id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals(orderCommand.getProduct(), basicOrder.getProduct());
        assertEquals(orderCommand.getState(), basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);

    }

    @Test
    public void testResolverNBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 00.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        int id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals(orderCommand.getProduct(), basicOrder.getProduct());
        assertEquals(orderCommand.getState(), basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);
    }

    @Test
    public void testResolverMBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = null;
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        int id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals(orderCommand.getProduct(), basicOrder.getProduct());
        assertEquals(orderCommand.getState(), basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);

    }

    @Test
    public void testResolverBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(null);
        orderCommand.setState(ohio.getStateName());
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        Integer id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals("", basicOrder.getProduct());
        assertEquals(orderCommand.getState(), basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);

    }

    @Test
    public void testResolverPBackandForth() {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);
        stateDao.create(ohio);

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);
        productDao.create(product);

        // Make some data for the dto.
        // 1,Wise,OH,6.25,Wood,100.00,5.15,4.75,515.00,475.00,61.88,1051.88
        String name = "SWC Guild, Test.";
        double area = 100.00;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 10);

        Date orderDate = calendar.getTime();

        OrderCommand orderCommand = new OrderCommand();

        orderCommand.setName(name);
        orderCommand.setArea(area);
        orderCommand.setProduct(product.getProductName());
        orderCommand.setState(null);
        orderCommand.setDate(orderDate);

        Order builtOrder = orderDao.orderBuilder(orderCommand);

        // Create the file in the Dao.
        Order returnedOrder = orderDao.create(builtOrder);

        // Record the orders id number.
        Integer id = builtOrder.getId();

        OrderCommand basicOrder = orderDao.resolveOrderCommand(builtOrder);

        assertNotNull(basicOrder);

        assertEquals(orderCommand.getName(), basicOrder.getName());
        assertEquals(orderCommand.getArea(), basicOrder.getArea(), 0.0005);
        assertEquals(orderCommand.getDate(), basicOrder.getDate());
        assertEquals(orderCommand.getProduct(), basicOrder.getProduct());
        assertEquals("", basicOrder.getState());

        Order unresolvedOrder = orderDao.orderBuilder(basicOrder);

        assertNotNull(unresolvedOrder);

        assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0005);
        assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
        assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getDate(), unresolvedOrder.getDate());
        assertEquals(builtOrder.getId(), unresolvedOrder.getId());
        assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.0005);
        assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.0005);
        assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.0005);
        assertEquals(builtOrder.getName(), unresolvedOrder.getName());
        assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
        assertEquals(builtOrder.getState(), unresolvedOrder.getState());
        assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.0005);
        assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0005);
        assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.0005);

    }

    @Test
    public void newCrudTest() {
        Random random = new Random();

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        List<Product> allProducts = productDao.getListOfProducts();
        Product randomProduct = allProducts.get(random.nextInt(allProducts.size()));

        List<State> allStates = stateDao.getListOfStates();
        State randomState = allStates.get(random.nextInt(allStates.size()));

        OrderCommand orderCommand = new OrderCommand();

        Calendar postgresSupportedCalendar = Calendar.getInstance();
        postgresSupportedCalendar.setTimeInMillis(0);

        int year = -4713 + random.nextInt(294276);
        int month = random.nextInt(12);
        int date = random.nextInt(32);

        postgresSupportedCalendar.set(year, month, date);

        Date postgresSupportedDate = postgresSupportedCalendar.getTime();

        orderCommand.setName(UUID.randomUUID().toString());
        orderCommand.setArea(random.nextDouble() + random.nextInt(1000));
        orderCommand.setDate(postgresSupportedDate);
        orderCommand.setProduct(randomProduct.getProductName());
        orderCommand.setState(randomState.getStateName());

        Order testOrder = orderDao.orderBuilder(orderCommand);

        testOrder = orderDao.create(testOrder);

        assertNotNull(testOrder);

        Integer id = testOrder.getId();

        assertNotNull(id);
        assertTrue(id > 1);

        Order premodifiedOrder = orderDao.get(id);
        Order returnedOrder = orderDao.get(id);

        double area = testOrder.getArea();
        area = ProductUtilities.roundToDecimalPlace(area, 4);
        testOrder.setArea(area);

        assertNotNull(returnedOrder);

        Order firstTestOrder = returnedOrder;
        Order secondTestOrder = testOrder;

        assertTrue(Objects.equals(firstTestOrder.getArea(), secondTestOrder.getArea()));
        assertEquals(firstTestOrder.getCostPerSquareFoot(), secondTestOrder.getCostPerSquareFoot(), .01);

        OrderTest.isSameDay(firstTestOrder.getDate(), secondTestOrder.getDate());

        assertTrue(Objects.equals(firstTestOrder.getId(), secondTestOrder.getId()));
        assertEquals(firstTestOrder.getLaborCost(), secondTestOrder.getLaborCost(), 0.01);
        assertEquals(firstTestOrder.getLaborCostPerSquareFoot(), secondTestOrder.getLaborCostPerSquareFoot(), 0.01);
        assertEquals(firstTestOrder.getMaterialCost(), secondTestOrder.getMaterialCost(), 0.01);
        assertTrue(Objects.equals(firstTestOrder.getName(), secondTestOrder.getName()));
        assertTrue(Objects.equals(firstTestOrder.getState(), secondTestOrder.getState()));
        assertEquals(firstTestOrder.getTax(), secondTestOrder.getTax(), 0.01);
        assertEquals(firstTestOrder.getTaxRate(), secondTestOrder.getTaxRate(), 0.01);
        assertEquals(firstTestOrder.getTotal(), secondTestOrder.getTotal(), 0.01);
        assertEquals(firstTestOrder.getProduct(), secondTestOrder.getProduct());

        assertEquals(returnedOrder, premodifiedOrder);

        returnedOrder.setArea(random.nextDouble() * 100);
        returnedOrder.setTotal(random.nextDouble() * 100);
        returnedOrder.setCostPerSquareFoot(random.nextDouble() * 100);
        returnedOrder.setLaborCost(random.nextDouble() * 100);
        returnedOrder.setLaborCostPerSquareFoot(random.nextDouble() * 100);
        returnedOrder.setMaterialCost(random.nextDouble() * 100);
        returnedOrder.setTax(random.nextDouble() * 100);
        returnedOrder.setTaxRate(random.nextDouble() * 100);

        Order updatedOrder = orderDao.update(returnedOrder);

        assertNotNull(updatedOrder);

        firstTestOrder = updatedOrder;
        secondTestOrder = returnedOrder;

        assertEquals(firstTestOrder.getArea(), secondTestOrder.getArea(), 0.01);
        assertEquals(firstTestOrder.getCostPerSquareFoot(), secondTestOrder.getCostPerSquareFoot(), .01);
        OrderTest.isSameDay(firstTestOrder.getDate(), secondTestOrder.getDate());
        assertTrue(Objects.equals(firstTestOrder.getId(), secondTestOrder.getId()));
        assertEquals(firstTestOrder.getLaborCost(), secondTestOrder.getLaborCost(), 0.01);
        assertEquals(firstTestOrder.getLaborCostPerSquareFoot(), secondTestOrder.getLaborCostPerSquareFoot(), 0.01);
        assertEquals(firstTestOrder.getMaterialCost(), secondTestOrder.getMaterialCost(), 0.01);
        assertTrue(Objects.equals(firstTestOrder.getName(), secondTestOrder.getName()));
        assertTrue(Objects.equals(firstTestOrder.getState(), secondTestOrder.getState()));
        assertEquals(firstTestOrder.getTax(), secondTestOrder.getTax(), 0.01);
        assertEquals(firstTestOrder.getTaxRate(), secondTestOrder.getTaxRate(), 0.01);
        assertEquals(firstTestOrder.getTotal(), secondTestOrder.getTotal(), 0.01);
        assertEquals(firstTestOrder.getProduct(), secondTestOrder.getProduct());

        assertNotEquals(premodifiedOrder, updatedOrder);

        firstTestOrder = premodifiedOrder;
        secondTestOrder = updatedOrder;

        assertTrue(!Objects.equals(firstTestOrder.getArea(), secondTestOrder.getArea()));
        assertNotEquals(firstTestOrder.getCostPerSquareFoot(), secondTestOrder.getCostPerSquareFoot(), .01);
        OrderTest.isSameDay(firstTestOrder.getDate(), secondTestOrder.getDate());
        assertTrue(Objects.equals(firstTestOrder.getId(), secondTestOrder.getId()));
        assertNotEquals(firstTestOrder.getLaborCost(), secondTestOrder.getLaborCost(), 0.01);
        assertNotEquals(firstTestOrder.getLaborCostPerSquareFoot(), secondTestOrder.getLaborCostPerSquareFoot(), 0.01);
        assertNotEquals(firstTestOrder.getMaterialCost(), secondTestOrder.getMaterialCost(), 0.01);
        assertTrue(Objects.equals(firstTestOrder.getName(), secondTestOrder.getName()));
        assertTrue(Objects.equals(firstTestOrder.getState(), secondTestOrder.getState()));
        assertNotEquals(firstTestOrder.getTax(), secondTestOrder.getTax(), 0.01);
        assertNotEquals(firstTestOrder.getTaxRate(), secondTestOrder.getTaxRate(), 0.01);
        assertNotEquals(firstTestOrder.getTotal(), secondTestOrder.getTotal(), 0.01);
        assertEquals(firstTestOrder.getProduct(), secondTestOrder.getProduct());

        Order postUpdateOrder = orderDao.get(id);

        assertNotNull(postUpdateOrder);
        assertEquals(postUpdateOrder, updatedOrder);

        Order deletedOrder = orderDao.delete(updatedOrder);

        assertNotNull(deletedOrder);
        assertEquals(deletedOrder, updatedOrder);

        Order alsoNullOrder = orderDao.delete(updatedOrder);
        assertNull(alsoNullOrder);

        Order nullOrder = orderDao.get(id);
        assertNull(nullOrder);
    }

    @Test
    public void listTest() {
        Random random = new Random();

        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        for (OrderSortByEnum sortByEnum : OrderSortByEnum.values()) {
            OrderResultSegment resultSegment = new OrderResultSegment(sortByEnum, 0, Integer.MAX_VALUE);

            List<Order> orders = orderDao.list(resultSegment);
            assertNotNull(orders);

            assertTrue(orders.size() > 0);
            assertEquals(orders.size(), orderDao.size());
        }

        for (OrderSortByEnum sortByEnum : OrderSortByEnum.values()) {

            int size = random.nextInt(orderDao.size());

            OrderResultSegment resultSegment = new OrderResultSegment(sortByEnum, 0, size);

            List<Order> orders = orderDao.list(resultSegment);
            assertNotNull(orders);

            assertEquals(orders.size(), size);
        }

        int size = random.nextInt(orderDao.size());
        OrderResultSegment resultSegment = new OrderResultSegment(null, 0, size);

        List<Order> orders = orderDao.list(resultSegment);
        assertNotNull(orders);

        assertEquals(orders.size(), size);

        orders = orderDao.list(null);
        assertNotNull(orders);

        assertEquals(orders.size(), orderDao.size());
    }

    @Test
    public void searchTest() {
        System.out.println("Test Search For Orders By Everything");

        Random random = new Random();

        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        List<Order> allOrders = orderDao.list(null);
        int size = allOrders.size();

        Order randomValidOrder = null;

        for (OrderSearchByOptionEnum searchByEnum : OrderSearchByOptionEnum.values()) {
            for (OrderSortByEnum sortByEnum : OrderSortByEnum.values()) {

                String searchText = null;

                switch (searchByEnum) {
                    case DATE:
                        Date randomValidDate = null;
                        while (randomValidDate == null) {
                            randomValidOrder = allOrders.get(random.nextInt(size));
                            randomValidDate = randomValidOrder.getDate();
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(randomValidDate);

                        searchText = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
                        break;
                    case NAME:
                        randomValidOrder = null;
                        while (randomValidOrder == null || randomValidOrder.getName() == null) {
                            randomValidOrder = allOrders.get(random.nextInt(size));
                            searchText = randomValidOrder.getName();
                        }

                        int subStringLength = random.nextInt(searchText.length()) + 5;

                        System.out.println("String Length: " + searchText.length());
                        System.out.println("Sub Length: " + subStringLength);
                        System.out.println("");

                        searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
                        break;
                    case ORDER_NUMBER:
                        randomValidOrder = allOrders.get(random.nextInt(size));
                        searchText = Integer.toString(randomValidOrder.getId());
                        break;
                    case PRODUCT:
                        randomValidOrder = null;
                        while (randomValidOrder == null || randomValidOrder.getProduct() == null || randomValidOrder.getProduct().getProductName() == null) {
                            randomValidOrder = allOrders.get(random.nextInt(size));
                        }

                        searchText = randomValidOrder.getProduct().getProductName();

                        subStringLength = random.nextInt(searchText.length()) + 5;

                        System.out.println("String Length: " + searchText.length());
                        System.out.println("Sub Length: " + subStringLength);
                        System.out.println("");

                        searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
                        break;
                    case STATE:
                        randomValidOrder = null;
                        while (randomValidOrder == null || randomValidOrder.getState() == null) {
                            randomValidOrder = allOrders.get(random.nextInt(size));
                        }
                        searchText = randomValidOrder.getState().getStateName();
                        break;
                    case EVERYTHING:
                        switch (random.nextInt(5)) {
                            case 0:
                                Date randomValidDate2 = null;
                                while (randomValidDate2 == null) {
                                    randomValidOrder = allOrders.get(random.nextInt(size));
                                    randomValidDate2 = randomValidOrder.getDate();
                                }

                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(randomValidDate2);

                                searchText = (calendar2.get(Calendar.MONTH) + 1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH) + "/" + calendar2.get(Calendar.YEAR);
                                break;
                            case 1:
                                randomValidOrder = null;
                                while (randomValidOrder == null || randomValidOrder.getName() == null) {
                                    randomValidOrder = allOrders.get(random.nextInt(size));
                                    searchText = randomValidOrder.getName();
                                }

                                subStringLength = random.nextInt(searchText.length()) + 5;

                                System.out.println("String Length: " + searchText.length());
                                System.out.println("Sub Length: " + subStringLength);
                                System.out.println("");

                                searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
                                break;
                            case 2:
                                randomValidOrder = allOrders.get(random.nextInt(size));
                                searchText = Integer.toString(randomValidOrder.getId());
                                break;
                            case 3:
                                randomValidOrder = null;
                                while (randomValidOrder == null || randomValidOrder.getProduct() == null || randomValidOrder.getProduct().getProductName() == null) {
                                    randomValidOrder = allOrders.get(random.nextInt(size));
                                }

                                searchText = randomValidOrder.getProduct().getProductName();

                                subStringLength = random.nextInt(searchText.length()) + 5;

                                System.out.println("String Length: " + searchText.length());
                                System.out.println("Sub Length: " + subStringLength);
                                System.out.println("");

                                searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
                                break;
                            case 4:
                            default:
                                randomValidOrder = null;
                                while (randomValidOrder == null || randomValidOrder.getState() == null) {
                                    randomValidOrder = allOrders.get(random.nextInt(size));
                                }
                                searchText = randomValidOrder.getState().getStateName();
                                break;
                        }
                        break;
                    default:
                        fail("This should not get here.");
                }

                OrderSearchRequest searchRequest = new OrderSearchRequest(searchText, searchByEnum);
                OrderResultSegment resultSegment = new OrderResultSegment(sortByEnum, 0, Integer.MAX_VALUE);

                List<Order> orders = orderDao.search(searchRequest, resultSegment);
                assertNotNull(orders);

                assertTrue("SearchBy: " + searchByEnum.toString() + ", SortBy: " + sortByEnum.toString() + ", Count: " + orders.size() + ", Search Text: " + searchText + ", ID: " + randomValidOrder.getId(),
                        orders.size() > 0);

                assertTrue("SearchBy: " + searchByEnum.toString() + ", SortBy: " + sortByEnum.toString() + ", Count: " + orders.size() + ", Search Text: " + searchText + ", ID: " + randomValidOrder.getId(),
                        orders.contains(randomValidOrder));
            }

            OrderSearchRequest searchRequest = new OrderSearchRequest("", searchByEnum);

            List<Order> orders = orderDao.search(searchRequest, null);
            assertNotNull(orders);

            searchRequest = new OrderSearchRequest("", null);

            orders = orderDao.search(searchRequest, null);
            assertNotNull(orders);

            orders = orderDao.search(null, null);
            assertNotNull(orders);
            assertTrue(orders.isEmpty());
        }
    }

    @Test
    public void searchForPatTest() {
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        OrderSearchRequest searchRequest = new OrderSearchRequest("pat", OrderSearchByOptionEnum.NAME);
        OrderResultSegment resultSegment = new OrderResultSegment(OrderSortByEnum.SORT_BY_NAME, 0, Integer.MAX_VALUE);

        List<Order> orders = orderDao.search(searchRequest, resultSegment);

        assertNotNull(orders);

        assertTrue(orders.size() > 0);

        Order aPatOrder = orderDao.get(idOfAPatOrder);

        assertTrue(orders.contains(aPatOrder));
    }

    @Test
    public void searchForPatByEverythingTest() {
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        OrderSearchRequest searchRequest = new OrderSearchRequest("pat", OrderSearchByOptionEnum.EVERYTHING);
        OrderResultSegment resultSegment = new OrderResultSegment(OrderSortByEnum.SORT_BY_NAME, 0, Integer.MAX_VALUE);

        List<Order> orders = orderDao.search(searchRequest, resultSegment);

        assertNotNull(orders);

        assertTrue(orders.size() > 0);

        Order aPatOrder = orderDao.get(idOfAPatOrder);

        assertTrue(orders.contains(aPatOrder));
    }
}
