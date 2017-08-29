 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author apprentice
 */
public class ProductDaoDbImplTest {

    ApplicationContext ctx;

    private static final String[] fakeProducts = {"Better Flooring",
        "Worlds Best Floor",
        "floor1",
        "Floor5",
        "Floor9",
        "FLOOR5",
        "BEST Product ever",
        "best floor",
        "Good floor",
        "BETTER FLOOR",
        "German Bamboo",
        "Product1",
        "Product2",
        "Product7",
        "Fancy New Product",
        "Fake Product That I Have Not Used Yet",
        "Grass",
        "Teststeel",
        "Steel",
        "Test Steel",
        "Wood",
        "Fake Product That I Have Not Used Yet",
        "Generic Floor",
        "A Unique Product"};

    public ProductDaoDbImplTest() {
        ctx = new ClassPathXmlApplicationContext("testProductDb-DedicatedApplicationContext.xml");
    }

    @Before
    public void setUp() {
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        RemoveFakeProducts(instance);
    }

    @After
    public void tearDown() {
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        RemoveFakeProducts(instance);
    }

    private void RemoveFakeProducts(ProductDao instance) {
        for (String fakeProduct : fakeProducts) {
            if (instance.get(fakeProduct) != null) {
                Product product = new Product();
                product.setProductName(fakeProduct);

                instance.delete(product);
            }
        }
    }

    @Test
    public void testCreate() {
        // Create should not accept a product with no name.
        System.out.println("create");
        Product product = productFactory();
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = null;
        product.setProductName(null);
        Product result = instance.create(product);
        assertNull(result);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateB() {
        // Create should not accept a null product object.
        System.out.println("create");
        Product product = null;
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = null;
        Product result = instance.create(product);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateC() {
        // This one should work, and return that same product back again.
        System.out.println("create");
        Product product = productFactory();
        product.setType("Product1");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        Product result = instance.create(product);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateE() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = productFactory();
        product.setType("Product2");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        Product result = instance.create(product);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateF() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = productFactory();
        product.setType("Product7");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        Product result = instance.create(product);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateWithDifferentCase() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = productFactory();
        product.setType(fakeProducts[3].toUpperCase());
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product result = instance.create(product);
        assertEquals(fakeProducts[3], result.getType());

        instance.delete(product);
    }

    @Test
    public void testCreateThenUpdateWithDifferentCase() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = productFactory();
        product.setType(fakeProducts[4].toUpperCase());
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product result = instance.create(product);
        assertEquals(fakeProducts[4], result.getType());

        product.setType(fakeProducts[4].toUpperCase());
        instance.update(product);

        result = instance.get(product.getProductName());
        assertEquals(fakeProducts[4], result.getType());

        instance.delete(product);
    }

    @Test
    public void testCreateThenGetWithCosts() {
        
        System.out.println("Create Then Get With Costs");
        Product product = productFactory();
        product.setType(fakeProducts[4]);
        product.setCost(05.25d);
        product.setLaborCost(09.509d);
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product result = instance.create(product);
        assertEquals(fakeProducts[4], result.getType());

        assertEquals(result, product);
        
        product.setType(fakeProducts[4].toUpperCase());
        instance.update(product);

        result = instance.get(product.getProductName());
        assertEquals(fakeProducts[4], result.getType());

        instance.delete(product);
    }

    @Test
    public void testGetA() {
        // This test tests the overloaded method.
        System.out.println("get");
        Product product = productFactory();
        String productName = null;
        product.setType("BEST Product ever");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product ignored = instance.create(product);
        Product result = instance.get(productName);

        assertNull(result);
    }

    @Test
    public void testCreateByUpdate() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = productFactory();
        String productName = "Fake Product That I have Not Used Yet";
        product.setType(productName);
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        instance.update(product);

        Product result = instance.get(productName);

        assertNotNull(result);

        assertTrue(productName.equalsIgnoreCase(result.getType()));
    }

    @Test
    public void testCreateByUpdateB() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = productFactory();
        String productName = null;
        product.setType(productName);
        product.setLaborCost(5.0d);
        product.setCost(7.0d);
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        instance.update(product);

        Product result = instance.get(productName);

        assertNull(result);
    }

    @Test
    public void testDelete() {
        System.out.println("create");
        Product product = productFactory();
        product.setType("Fancy New Product");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        Product result = instance.create(product);
        assertEquals(expResult, result);

        // Test get method.
        Product returnedProduct = instance.get(product.getType());
        assertEquals(returnedProduct, result);
        instance.delete(product);

        returnedProduct = instance.get(product.getType());
        assertEquals(returnedProduct, null);
    }

    @Test
    public void testGet() {
        System.out.println("create");
        Product product = productFactory();
        product.setType("Better Flooring");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        Product result = instance.create(product);
        assertEquals(expResult, result);

        // Test get method.
        Product returnedProduct = instance.get(product.getType());

        assertEquals(returnedProduct, result);

        instance.delete(product);

        returnedProduct = instance.get(product.getType());
        assertEquals(returnedProduct, null);
    }

    @Test
    public void testGetWithDifferentCase() {
        System.out.println("create");
        Product product = productFactory();
        product.setType("Worlds Best Floor");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        String productNameLowerCase = "worlds Best FLOOR";
        Product result = instance.create(product);
        assertEquals(expResult, result);

        // Test get method.
        Product returnedProduct = instance.get(productNameLowerCase);
        assertEquals(returnedProduct, result);
        instance.delete(product);

        returnedProduct = instance.get(productNameLowerCase);
        assertEquals(returnedProduct, null);
    }

    @Test
    public void testGet3() {
        System.out.println("create");
        Product product = productFactory();
        product.setType("floor1");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        String productNameLowerCase = "FLOOR1";
        Product result = instance.create(product);
        assertEquals(expResult, result);

        // Test get method.
        Product returnedProduct = instance.get(productNameLowerCase);
        assertEquals(returnedProduct, result);
        instance.delete(product);

        returnedProduct = instance.get(productNameLowerCase);
        assertEquals(returnedProduct, null);
    }

    @Test
    public void testGet4() {
        System.out.println("create");
        Product product = productFactory();
        product.setType("FLOOR5");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);
        Product expResult = product;
        String productNameLowerCase = "floor5";
        Product result = instance.create(product);
        assertEquals(expResult, result);

        // Test get method.
        Product returnedProduct = instance.get(productNameLowerCase);
        assertEquals(returnedProduct, result);
        instance.delete(product);

        returnedProduct = instance.get(productNameLowerCase);
        assertEquals(returnedProduct, null);
    }

    @Test
    public void testSize() {

        Product product = productFactory();
        product.setType("best floor");
        ProductDao instance = ctx.getBean("productDao", ProductDao.class);

        Product secondProduct = productFactory();

        secondProduct.setType("Good floor");

        Product thirdProduct = productFactory();
        thirdProduct.setType("BETTER FLOOR");

        instance.create(product);
        instance.create(secondProduct);
        instance.create(thirdProduct);

        assertTrue(2 < instance.size());

        assertTrue(instance.getList().contains("Good Floor"));
        assertTrue(instance.getList().contains("Better Floor"));
        assertTrue(instance.getList().contains("Best Floor"));
    }

    private Product productFactory() {
        Product secondProduct = new Product();
        secondProduct.setType("Generic floor");
        secondProduct.setCost(0.0d);
        secondProduct.setLaborCost(0.0d);
        return secondProduct;
    }

    @Test
    public void testEncodeAndDecode() {
        // The true parameter in the ProductDao constructor signifies a test.
        ProductDao productDao = ctx.getBean("productDao", ProductDao.class);
        Product testProduct = productFactory();

        String productName = "German Bamboo";
        testProduct.setType(productName);

        // Create the file in the Dao.
        Product returnedProduct = productDao.create(testProduct);

        // Verify that the product object that the create method passed back
        // was the same one it was given.
        assertEquals(testProduct, returnedProduct);

        double cost = 8.25;
        double labor = 12.75;

        returnedProduct.setCost(cost);
        returnedProduct.setLaborCost(labor);

        // Use the update method to save this new text to file.
        productDao.update(testProduct);

        // Load a new instance of the ProductDao.
        ProductDao secondDao = ctx.getBean("productDao", ProductDao.class);

        // Pull a product  using the id number recorded earlier.
        Product thirdProduct = secondDao.get(productName);

        assertTrue(thirdProduct != null);

        // Check that the update method saved the new text.
        assertEquals(cost, thirdProduct.getCost(), 1e-8);
        assertEquals(labor, thirdProduct.getLaborCost(), 1e-8);
        assertEquals(productName, thirdProduct.getType());

        // Delete the test product.
        secondDao.delete(thirdProduct);

        // Load a third instance of the Dao and verify that 
        // the product was deleted from the file.
        ProductDao thirdDao = ctx.getBean("productDao", ProductDao.class);
        assertEquals(thirdDao.get(productName), null);

    }
}
