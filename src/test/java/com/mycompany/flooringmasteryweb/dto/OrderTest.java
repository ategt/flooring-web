/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import org.junit.*;
import org.springframework.context.ApplicationContext;

import java.util.*;

import static org.junit.Assert.*;

/**
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

    public static Order orderGenerator() {
        Order instance = new Order();
        Random random = new Random();
        instance.setArea(random.nextDouble());
        instance.setCostPerSquareFoot(random.nextDouble());
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

        Calendar calendar = Calendar.getInstance();
        calendar.set(random.nextInt(9000), random.nextInt(11), random.nextInt(31), 0, 0, 0);

        instance.setDate(calendar.getTime());

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

    public static Order orderFactory(ApplicationContext ctx) {

        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        OrderDao orderDao = ctx.getBean("orderDao", OrderDao.class);

        com.mycompany.flooringmasteryweb.dto.State ohio = new com.mycompany.flooringmasteryweb.dto.State();
        ohio.setState("CA");
        ohio.setStateTax(6.25);

        if (stateDao.get(ohio.getStateName()) == null) {
            stateDao.create(ohio);
        } else {
            stateDao.update(ohio);
        }

        com.mycompany.flooringmasteryweb.dto.Product product = new com.mycompany.flooringmasteryweb.dto.Product();
        product.setType("Test Steel");
        product.setCost(5);
        product.setLaborCost(3);

        if (productDao.get(product.getProductName()) == null) {
            productDao.create(product);
        } else {
            productDao.update(product);
        }

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

        return builtOrder;
    }

    public static Boolean isOrderInList(Order order, List<Order> orderList) {

        Order testedOrder = getTheOrderFromTheList(order, orderList);

        return verifyOrder(testedOrder, order);
    }

    public static Boolean verifyOrder(Order unresolvedOrder, Order builtOrder) {

        if (unresolvedOrder == null && builtOrder == null) {
            return true;
        }

        if (unresolvedOrder == null || builtOrder == null) {
            return false;
        }

        try {
            assertEquals(unresolvedOrder.getId(), builtOrder.getId());
            assertNotNull(unresolvedOrder);
            assertNotNull(builtOrder);

            assertEquals(builtOrder.getArea(), unresolvedOrder.getArea(), 0.0001);
            assertEquals(builtOrder.getClass(), unresolvedOrder.getClass());
            assertEquals(builtOrder.getCostPerSquareFoot(), unresolvedOrder.getCostPerSquareFoot(), 0.001);

            assertTrue("\nBuildOrder: \t" + builtOrder.getDate().toString() + "\n" +
                            "UnresolvedOrder: \t" + unresolvedOrder.getDate().toString(),
                    isSameDay(builtOrder.getDate(), unresolvedOrder.getDate()));

            assertEquals(builtOrder.getId(), unresolvedOrder.getId());
            assertEquals(builtOrder.getLaborCost(), unresolvedOrder.getLaborCost(), 0.01);
            assertEquals(builtOrder.getLaborCostPerSquareFoot(), unresolvedOrder.getLaborCostPerSquareFoot(), 0.001);
            assertEquals(builtOrder.getMaterialCost(), unresolvedOrder.getMaterialCost(), 0.01);
            assertEquals(builtOrder.getName(), unresolvedOrder.getName());
            assertEquals(builtOrder.getProduct(), unresolvedOrder.getProduct());
            assertEquals(builtOrder.getState(), unresolvedOrder.getState());
            assertEquals(builtOrder.getTax(), unresolvedOrder.getTax(), 0.01);
            assertEquals(builtOrder.getTaxRate(), unresolvedOrder.getTaxRate(), 0.0001);
            assertEquals(builtOrder.getTotal(), unresolvedOrder.getTotal(), 0.01);

        } catch (java.lang.AssertionError ex) {

            Gson gson = new GsonBuilder().create();

            String builtOrderStr = gson.toJson(builtOrder);
            String unresolvedOrderStr = gson.toJson(unresolvedOrder);

            System.out.println(Strings.repeat("-", 45));
            System.out.println();
            System.out.println("Built: \t\t" + builtOrderStr);
            System.out.println("Unresolved:\t" + unresolvedOrderStr);
            throw ex;
        }

//            Method[] methods = unresolvedOrder.getClass().getMethods();
//            Field[] fields = unresolvedOrder.getClass().getFields();
//
//            Field field = fields[0];
//
//            //for (Field field : fields){
//                //field.
//            //}
//
//            try {
//            for ( Method method : methods){
//                if (method.getName().startsWith("get") && ( Objects.equals(method.getReturnType(), String.class)
//                        || Objects.equals(method.getReturnType(), double.class)
//                        || Objects.equals(method.getReturnType(), Integer.class)
//                        || Objects.equals(method.getReturnType(), Date.class))){
//
//                        Object object = method.invoke(builtOrder);
//                    Object object = method.invoke(unresolvedOrder);
//                }
//
//            }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//
//            String orderComparison = System.lineSeparator() +
//                    unresolvedOrder.getId()
//

        return (true);
    }

    public static boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        }
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyyMMdd");

        if (date1 == null || date2 == null) {
            return false;
        }
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static Order getTheOrderFromTheList(Order order, List<Order> orderList) {

        if (order == null) {
            return null;
        }

        Integer id = order.getId();

        Order choosenOrder = null;

        for (Order testOrder : orderList) {
            if (Objects.equals(testOrder.getId(), id)) {
                choosenOrder = testOrder;
                break;
            }

        }

        return choosenOrder;
    }
}