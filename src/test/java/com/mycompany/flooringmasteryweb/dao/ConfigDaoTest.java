/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.exceptions.ConfigurationFileCorruptException;
import com.mycompany.flooringmasteryweb.exceptions.FileCreationException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ConfigDaoTest {

    ApplicationContext ctx;

    public ConfigDaoTest() {
        ctx = new ClassPathXmlApplicationContext("testConfigDao-ApplicationContext.xml");
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTheFileConstructorNull() {
        ConfigDao configDao = null;
        //try {
            configDao = ctx.getBean( "configDaoWithNull", ConfigDao.class );
            //configDao = new ConfigDao(null);
//        } catch (ConfigurationFileCorruptException | FileCreationException ex) {
//            fail("The test was not supposed to throw an exception here.");
//            Logger.getLogger(ConfigDaoTest.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println(" Exception Message: " + ex.getMessage());
//        }

        assertEquals(true, configDao.get().isInTestMode());

    }

    @Test
    public void testTheFileConstructor() {

        //java.io.File testConfigFile = new java.io.File("testConfigFile.txt");

        ConfigDao configDao = null;
        try {
            configDao = ctx.getBean( "configDaoWithTestFile", ConfigDao.class );
            //configDao = new ConfigDao(testConfigFile);
            fail("The test was supposed to throw an exception here.");
        } catch ( org.springframework.beans.factory.BeanCreationException | org.springframework.beans.BeanInstantiationException ex ) {        
//} catch (ConfigurationFileCorruptException ex) {
            System.out.println("This Test Checks to see if the exception thrower works right.");
            System.out.println(" Exception Message: " + ex.getMessage());
            //Throwable initThrown = ex.initCause(ex);
            //Throwable causeThrown = ex.getCause();  // class that threw the error
            //Throwable specificThrown = ex.getMostSpecificCause();   // has the message before
            Throwable rootThrown = ex.getRootCause();               // This one looks promising.
            //Throwable[] listOfSuppressedThrown = ex.getSuppressed();
            System.out.println("Root thrown message: " + rootThrown.getMessage());
            //System.out.println(ex instanceof ConfigurationFileCorruptException);
            System.out.println("End Exception Thrower test.");
        //} catch (FileCreationException ex) {
            //fail("The test was not supposed to throw an exception here.");
          //  Logger.getLogger(ConfigDaoTest.class.getName()).log(Level.SEVERE, null, ex);
           // System.out.println(" Exception Message: " + ex.getMessage());
        }

        assertEquals(null, configDao);

    }

    @Test
    public void testTheFileConstructorB() {
        try {

            java.io.File defaultTestDirectory = new java.io.File("Test");
            java.io.File defaultTaxesFile = new java.io.File("Data/Taxes.txt");
            java.io.File defaultProductsFile = new java.io.File("Data/Products.txt");
            java.io.File testConfigFile = new java.io.File("testConfigFile2.txt");
            testConfigFile.deleteOnExit();

            ConfigDao configDao = new ConfigDao(testConfigFile);

            com.mycompany.flooringmasteryweb.dto.Config config = configDao.get();

            String productFile = defaultProductsFile.getPath();

            assertEquals(productFile, config.getProductFile().getPath());

            String taxesFile = defaultTaxesFile.getPath();

            assertEquals(taxesFile, config.getTaxesFile().getPath());

            String testDirectory = defaultTestDirectory.getPath();

            assertEquals(testDirectory, config.getTestDirectory().getPath());
        } catch (ConfigurationFileCorruptException | FileCreationException ex) {
            Logger.getLogger(ConfigDaoTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(" Exception Message: " + ex.getMessage());
            fail("The Test Threw an exception.");
        }

    }

    @Test
    public void testEncodeAndDecode() {
        try {

            java.io.File defaultTestDirectory = new java.io.File("Test");
            java.io.File defaultTaxesFile = new java.io.File("Data/Taxes.txt");
            java.io.File defaultProductsFile = new java.io.File("Data/Products.txt");
            java.io.File testConfigFile = new java.io.File("testConfigFile3.txt");
            testConfigFile.deleteOnExit();

            ConfigDao configDao = new ConfigDao(testConfigFile);

            com.mycompany.flooringmasteryweb.dto.Config config = configDao.get();

            String productFile = defaultProductsFile.getPath();

            assertEquals(productFile, config.getProductFile().getPath());

            String taxesFile = defaultTaxesFile.getPath();

            assertEquals(taxesFile, config.getTaxesFile().getPath());

            String testDirectory = defaultTestDirectory.getPath();

            assertEquals(testDirectory, config.getTestDirectory().getPath());

            // Load a new instance and see if it was able to load the correct values.
            ConfigDao secondConfigDao = new ConfigDao(testConfigFile);

            com.mycompany.flooringmasteryweb.dto.Config secondConfig = secondConfigDao.get();

            assertEquals(productFile, secondConfig.getProductFile().getPath());
            assertEquals(taxesFile, secondConfig.getTaxesFile().getPath());
            assertEquals(testDirectory, secondConfig.getTestDirectory().getPath());

        } catch (ConfigurationFileCorruptException | FileCreationException ex) {
            Logger.getLogger(ConfigDaoTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(" Exception Message: " + ex.getMessage());
            fail("The Test Threw an exception.");
        }

    }

    @Test
    public void testEncodeAndDecodeWithUpdate() {
        try {

            java.io.File defaultTestDirectory = new java.io.File("Test");
            java.io.File defaultTaxesFile = new java.io.File("Data/Taxes.txt");
            java.io.File defaultProductsFile = new java.io.File("Data/Products.txt");
            java.io.File testConfigFile = new java.io.File("testConfigFile4.txt");
            testConfigFile.deleteOnExit();

            ConfigDao configDao = new ConfigDao(testConfigFile);

            com.mycompany.flooringmasteryweb.dto.Config config = configDao.get();

            String productFile = defaultProductsFile.getPath();

            assertEquals(productFile, config.getProductFile().getPath());

            String taxesFile = defaultTaxesFile.getPath();

            assertEquals(taxesFile, config.getTaxesFile().getPath());

            String testDirectory = defaultTestDirectory.getPath();

            assertEquals(testDirectory, config.getTestDirectory().getPath());

            java.io.File differentTaxesFile = new java.io.File("ExtraTestTaxes.txt");

            config.setTaxesFile(differentTaxesFile);
            config.setInTestMode(false);
            configDao.update();

            // Load a new instance and see if it was able to load the correct values.
            ConfigDao secondConfigDao = new ConfigDao(testConfigFile);

            com.mycompany.flooringmasteryweb.dto.Config secondConfig = secondConfigDao.get();

            assertEquals(productFile, secondConfig.getProductFile().getPath());
            assertEquals(differentTaxesFile.getPath(), secondConfig.getTaxesFile().getPath());
            assertEquals(testDirectory, secondConfig.getTestDirectory().getPath());
            assertEquals(false, secondConfig.isInTestMode());

        } catch (ConfigurationFileCorruptException | FileCreationException ex) {
            Logger.getLogger(ConfigDaoTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(" Exception Message: " + ex.getMessage());
            fail("The Test Threw an exception.");
        }

    }

    @Test
    public void testDecode() {
        try {

            java.io.File differentTaxesFile = new java.io.File("Data/ExtraTestTaxes.txt");
            java.io.File defaultTestDirectory = new java.io.File("Test");
            java.io.File defaultProductsFile = new java.io.File("Data/ProductsGarbal.txt");
            java.io.File testConfigFile = new java.io.File("testConfigFile5.txt");
            //java.io.File dataDirectory = new java.io.File("testConfigFile5.txt");

            String productFile = defaultProductsFile.getPath();
            String testDirectory = defaultTestDirectory.getPath();

            // Load a new instance and see if it was able to load the correct values.
            ConfigDao secondConfigDao = new ConfigDao(testConfigFile);

            com.mycompany.flooringmasteryweb.dto.Config secondConfig = secondConfigDao.get();

            assertEquals(productFile, secondConfig.getProductFile().getPath());
            assertEquals(differentTaxesFile.getPath(), secondConfig.getTaxesFile().getPath());
            assertEquals(testDirectory, secondConfig.getTestDirectory().getPath());
            assertEquals(false, secondConfig.isInTestMode());

        } catch (ConfigurationFileCorruptException | FileCreationException ex) {
            Logger.getLogger(ConfigDaoTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(" Exception Message: " + ex.getMessage());
            fail("The Test Threw an exception.");
        }

    }

}
