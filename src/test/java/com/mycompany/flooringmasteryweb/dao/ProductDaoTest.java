/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.exceptions.ConfigurationFileCorruptException;
import com.mycompany.flooringmasteryweb.exceptions.FileCreationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class ProductDaoTest {

    java.io.File testFile = new java.io.File("ProductsTestData.txt");

    ConfigDao configDao;

    public ProductDaoTest() {
    }

    @Before
    public void setUp() {

        boolean isATest = true;
        ConfigDao configDao = null;

        try {
            configDao = new ConfigDao();
            configDao.get().setProductFile(testFile);
        } catch (ConfigurationFileCorruptException | FileCreationException ex) {
            Logger.getLogger(ProductDaoTest.class.getName()).log(Level.SEVERE, null, ex);
            //ex.printStackTrace();
            fail("Throwing This Exception Should Not Be Possible.\n" + ex.getMessage());
        }

        configDao.get().setInTestMode(isATest);
        this.configDao = configDao;
    }

    @After
    public void tearDown() {

        //java.io.File testFile = new java.io.File("/home/apprentice/_repos/adam.tegtmeier.self.work/Labs/FlooringMastery/ProductsTestData.txt");
        testFile.deleteOnExit();

    }

    @Test
    public void testCreate() {
        // Create should not accept a product with no name.
        System.out.println("create");
        Product product = new Product();
        ProductDao instance = new ProductDaoImpl(configDao);
        Product expResult = null;
        Product result = instance.create(product);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateB() {
        // Create should not accept a null product object.
        System.out.println("create");
        Product product = null;
        ProductDao instance = new ProductDaoImpl(configDao);
        Product expResult = null;
        Product result = instance.create(product);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateC() {
        // This one should work, and return that same product back again.
        System.out.println("create");
        Product product = new Product();
        product.setType("Product1");
        ProductDao instance = new ProductDaoImpl(configDao);
        Product expResult = product;
        Product result = instance.create(product);
        assertEquals(expResult, result);
    }

//    @Test
//    public void testCreateD() {
//        System.out.println("create");
//        Product product = new Product();
//        product.setType("Product1");
//        ProductDao instance = new ProductDao(true);
//        Product expResult = null;
//        Product result = instance.create(product);
//        assertEquals(expResult, result);
//    }
    @Test
    public void testCreateE() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = new Product();
        product.setType("Product2");
        ProductDao instance = new ProductDaoImpl(configDao);
        Product expResult = product;
        Product result = instance.create(product, product.getType());
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateF() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = new Product();
        product.setType("Product7");
        ProductDao instance = new ProductDaoImpl(configDao);
        Product expResult = product;
        Product result = instance.create(product, product.getType());
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateH() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = new Product();
        product.setType("BEST Product ever");
        ProductDao instance = new ProductDaoImpl(configDao);
        //Product expResult = product;
        Product result = instance.create(product, product.getType());
        assertEquals("Best Product Ever", result.getType());
    }


    @Test
    public void testGetA() {
        // This test tests the overloaded method.
        System.out.println("get");
        Product product = new Product();
        String productName = null;
        product.setType("BEST Product ever");
        ProductDao instance = new ProductDaoImpl(configDao);
        //Product expResult = product;
        Product ignored = instance.create(product, product.getType());
        Product result = instance.get(productName);
        
        assertNull(result);
    }

    @Test
    public void testCreateByUpdate() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = new Product();
        String productName = "Fake Product That I have Not Used Yet";
        product.setType(productName);
        ProductDao instance = new ProductDaoImpl(configDao);
        //Product expResult = product;
        instance.update(product);

        Product result = instance.get(productName);

        assertNotNull(result);

        assertTrue(productName.equalsIgnoreCase(result.getType()));
    }

    @Test
    public void testCreateByUpdateB() {
        // This test tests the overloaded method.
        System.out.println("create");
        Product product = new Product();
        String productName = null;
        product.setType(productName);
        product.setLaborCost(5.0d);
        product.setCost(7.0d);
        ProductDao instance = new ProductDaoImpl(configDao);
        //Product expResult = product;
        instance.update(product);

        Product result = instance.get(productName);

        assertNull(result);

    }

    @Test
    public void testDelete() {
        System.out.println("create");
        Product product = new Product();
        product.setType("Fancy New Product");
        ProductDao instance = new ProductDaoImpl(configDao);
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
        Product product = new Product();
        product.setType("Better Flooring");
        ProductDao instance = new ProductDaoImpl(configDao);
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
    public void testGet2() {
        System.out.println("create");
        Product product = new Product();
        product.setType("Worlds Best Floor");
        ProductDao instance = new ProductDaoImpl(configDao);
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
        Product product = new Product();
        product.setType("floor1");
        ProductDao instance = new ProductDaoImpl(configDao);
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
        Product product = new Product();
        product.setType("FLOOR5");
        ProductDao instance = new ProductDaoImpl(configDao);
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

        java.io.File tempFile = new java.io.File("ProductsTestData-temp.txt");
        // I change the name so the ProductDao can not find it.
        testFile.renameTo(tempFile);

        Product product = new Product();
        product.setType("best floor");
        ProductDao instance = new ProductDaoImpl(configDao);

        testFile.renameTo(tempFile);

        Product secondProduct = new Product();
        secondProduct.setType("Good floor");

        Product thirdProduct = new Product();
        thirdProduct.setType("BETTER FLOOR");

        instance.create(product);
        instance.create(secondProduct);
        instance.create(thirdProduct);

        assertEquals(3, instance.size());
        // The tests do not always run in a certain order and 
        // the class is loading data from another test.

        // I fixed this problem by renaming the file and then
        // changing the name back after the test was over.
        assertTrue(instance.getList().contains("Good Floor"));
        assertTrue(instance.getList().contains("Better Floor"));
        assertTrue(instance.getList().contains("Best Floor"));

        // Now, I change the name back, so the rest of the tests will 
        // work right.
        testFile.delete();
        tempFile.renameTo(testFile);

    }

    @Test
    public void testEncodeAndDecode() {

        // The true parameter in the ProductDao constructor signifies a test.
        ProductDao productDao = new ProductDaoImpl(configDao);
        Product testProduct = new Product();

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
        ProductDao secondDao = new ProductDaoImpl(configDao);

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
        ProductDao thirdDao = new ProductDaoImpl(configDao);
        assertEquals(thirdDao.get(productName), null);

    }

//
//    /**
//     * Test of create method, of class ProductDao.
//     */
//    @Test
//    public void testCreate_Product_String() {
//        System.out.println("create");
//        Product product = null;
//        String productName = "";
//        ProductDao instance = new ProductDao();
//        Product expResult = null;
//        Product result = instance.create(product, productName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of create method, of class ProductDao.
//     */
//    @Test
//    public void testCreate_String_Product() {
//        System.out.println("create");
//        String productName = "";
//        Product product = null;
//        ProductDao instance = new ProductDao();
//        Product expResult = null;
//        Product result = instance.create(productName, product);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of get method, of class ProductDao.
//     */
//    @Test
//    public void testGet() {
//        System.out.println("get");
//        String name = "";
//        ProductDao instance = new ProductDao();
//        Product expResult = null;
//        Product result = instance.get(name);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of update method, of class ProductDao.
//     */
//    @Test
//    public void testUpdate() {
//        System.out.println("update");
//        Product product = null;
//        ProductDao instance = new ProductDao();
//        instance.update(product);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class ProductDao.
//     */
//    @Test
//    public void testDelete() {
//        System.out.println("delete");
//        Product product = null;
//        ProductDao instance = new ProductDao();
//        instance.delete(product);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getList method, of class ProductDao.
//     */
//    @Test
//    public void testGetList() {
//        System.out.println("getList");
//        ProductDao instance = new ProductDao();
//        List<String> expResult = null;
//        List<String> result = instance.getList();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of size method, of class ProductDao.
//     */
//    @Test
//    public void testSize() {
//        System.out.println("size");
//        ProductDao instance = new ProductDao();
//        int expResult = 0;
//        int result = instance.size();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
}
