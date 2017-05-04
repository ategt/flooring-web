/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Address;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ATeg
 */
public class AddressDaoPostgresImplTest {

    ApplicationContext ctx;
    AddressDao addressDao;

    public AddressDaoPostgresImplTest() {
        ctx = new ClassPathXmlApplicationContext("testAddressPostgres-ApplicationContext.xml");
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        addressDao = ctx.getBean("addressDao", AddressDao.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of create method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testCRUD() {
        System.out.println("CRUD test");

        String city = UUID.randomUUID().toString();
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String zip = UUID.randomUUID().toString();
        String company = UUID.randomUUID().toString();
        String streetNumber = UUID.randomUUID().toString();
        String streetName = UUID.randomUUID().toString();

        Address address = new Address();
        address.setCity(city);
        address.setCompany(company);
        address.setFirstName(firstName);
        address.setLastName(lastName);
        address.setState(state);
        address.setStreetName(streetName);
        address.setStreetNumber(streetNumber);
        address.setZip(zip);

        int beforeCreation = addressDao.size();
        Address result = addressDao.create(address);
        int afterCreation = addressDao.size();
        
        assertEquals(beforeCreation + 1, afterCreation);
        
        assertNotNull(result);
        assertNotNull(result.getId());

        assertTrue(result.getId() > 0);

        Address retrivedAddress = addressDao.get(result.getId());
        assertEquals(retrivedAddress, result);
        
        retrivedAddress.setCity(UUID.randomUUID().toString());
        
        addressDao.update(retrivedAddress);
        
        Address companyAddress = addressDao.getByCompany(retrivedAddress.getCompany());
        
        assertEquals(companyAddress, retrivedAddress);
        assertNotEquals(result, companyAddress);
        
        assertEquals(afterCreation, addressDao.size());
        
        addressDao.delete(companyAddress.getId());
        
        assertEquals(afterCreation - 1, addressDao.size());
        
        Address deletedAddress = addressDao.get(companyAddress.getId());
        assertNull(deletedAddress);
        
        Address alsoDeleted = addressDao.getByCompany(company);
        assertNull(alsoDeleted);
        
        Address alsoDeleted2 = addressDao.get(company);
        assertNull(alsoDeleted2);
    }

    /**
     * Test of list method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testList() {
        System.out.println("list");
        
        
        
        List<Address> list = addressDao.list();
        
        AddressDaoPostgresImpl instance = null;
        List<Address> expResult = null;
        List<Address> result = instance.list();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByLastName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByLastName() {
        System.out.println("searchByLastName");
        String lastName = "";
        AddressDaoPostgresImpl instance = null;
        List<Address> expResult = null;
        List<Address> result = instance.searchByLastName(lastName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByFirstName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByFirstName() {
        System.out.println("searchByFirstName");
        String firstName = "";
        AddressDaoPostgresImpl instance = null;
        List<Address> expResult = null;
        List<Address> result = instance.searchByFirstName(firstName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByCity method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByCity() {
        System.out.println("searchByCity");
        String city = "";
        AddressDaoPostgresImpl instance = null;
        List<Address> expResult = null;
        List<Address> result = instance.searchByCity(city);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByState method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByState() {
        System.out.println("searchByState");
        String state = "";
        AddressDaoPostgresImpl instance = null;
        List<Address> expResult = null;
        List<Address> result = instance.searchByState(state);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByZip method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByZip() {
        System.out.println("searchByZip");
        String zipcode = "";
        AddressDaoPostgresImpl instance = null;
        List<Address> expResult = null;
        List<Address> result = instance.searchByZip(zipcode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fixNull method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testFixNull() {
        System.out.println("fixNull");
        String input = "";
        AddressDaoPostgresImpl instance = null;
        String expResult = "";
        String result = instance.fixNull(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
