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
public class ProductTest {
    
    public ProductTest() {
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
     * Test of equals method, of class Product.
     */
    @Test
    public void testEqualsWithNull() {
        System.out.println("equals");
        Object object = null;
        Product instance = new Product();
        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithNew() {
        System.out.println("equals");
        Product object = new Product();
        Product instance = productGenerator();

        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithTwoNew() {
        System.out.println("equals");
        Product object = new Product();
        Product instance = new Product();

        boolean expResult = true;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsWithRandom() {
        System.out.println("equals");
        Product object = new Product();
        for (int i = 0; i < 50; i++) {

            Product instance = productGenerator();

            boolean expResult = false;
            boolean result = instance.equals(object);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithOtherRandom() {
        System.out.println("equals");
        Product object = new Product();
        for (int i = 0; i < 50; i++) {

            Product instance = productGenerator();

            boolean expResult = false;
            boolean result = object.equals(instance);
            assertEquals(expResult, result);
        }
    }

    @Test
    public void testEqualsWithSelf() {
        System.out.println("equals");
        Product object = new Product();

        boolean expResult = true;
        boolean result = object.equals(object);
        assertEquals(expResult, result);
    }

    private Product productGenerator() {
        Product instance = new Product();
        Random random = new Random();
        instance.setCost(random.nextDouble());
        instance.setId(random.nextInt());
        instance.setLaborCost(random.nextDouble());
        instance.setProductName(UUID.randomUUID().toString());
        instance.setType(UUID.randomUUID().toString());
        return instance;
    }

    @Test
    public void testHashCode() {
        Product audit = new Product();
        assertTrue(audit.hashCode() >= 0);
    }

    @Test
    public void testEqualsAnotherWay() {
        System.out.println("Equals Again.");

        Random random = new Random();
        Product ordera = new Product();

        assertNotEquals(ordera, null);

        Product orderb = new Product();

        assertEquals(orderb, orderb);
        assertEquals(ordera, orderb);

        ordera.setCost(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setCost(ordera.getCost());

        assertEquals(ordera, orderb);

        ordera.setId(random.nextInt());

        assertNotEquals(ordera, orderb);

        orderb.setId(ordera.getId());

        assertEquals(ordera, orderb);

        ordera.setLaborCost(random.nextDouble());

        assertNotEquals(ordera, orderb);

        orderb.setLaborCost(ordera.getLaborCost());

        assertEquals(ordera, orderb);

        ordera.setProductName(UUID.randomUUID().toString());

        assertNotEquals(ordera, orderb);

        orderb.setProductName(ordera.getProductName());

        assertEquals(ordera, orderb);

        ordera.setType(UUID.randomUUID().toString());

        assertNotEquals(ordera, orderb);

        orderb.setType(ordera.getType());

        assertEquals(ordera, orderb);
    }
    
    @Test
    public void buildCommandProductTest(){
        Product product = productGenerator();
        
        ProductCommand productCommand = ProductCommand.buildProductCommand(product);
        
        Product returnedProduct = Product.buildProduct(productCommand);
        returnedProduct.setId(product.getId());
        
        assertEquals(product, returnedProduct);        
    }
}