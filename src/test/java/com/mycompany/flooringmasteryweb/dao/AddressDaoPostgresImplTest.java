/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Address;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
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

        Address address = addressBuilder(city, company, firstName, lastName, state, streetName, streetNumber, zip);

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

    private Address addressBuilder(String city, String company, String firstName, String lastName, String state, String streetName, String streetNumber, String zip) {
        Address address = new Address();
        address.setCity(city);
        address.setCompany(company);
        address.setFirstName(firstName);
        address.setLastName(lastName);
        address.setState(state);
        address.setStreetName(streetName);
        address.setStreetNumber(streetNumber);
        address.setZip(zip);
        return address;
    }

    /**
     * Test of list method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testList() {
        System.out.println("list");

        Address address = addressGenerator();
        addressDao.create(address);

        List<Address> list = addressDao.list();

        assertEquals(list.size(), addressDao.size());

        assertTrue(list.contains(address));
    }

    /**
     * Test of searchByLastName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByLastName() {
        System.out.println("searchByLastName");
        String lastName = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setLastName(lastName);
        addressDao.create(address);

        List<Address> result = addressDao.searchByLastName(lastName);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByLastName(lastName.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByFirstName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByFirstName() {
        System.out.println("searchByFirstName");

        String firstName = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setFirstName(firstName);
        addressDao.create(address);

        List<Address> result = addressDao.searchByFirstName(firstName);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByFirstName(firstName.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByFirstName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByCompany() {
        System.out.println("searchByCompany");

        String company = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setCompany(company);
        addressDao.create(address);

        List<Address> result = addressDao.searchByCompany(company);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByCompany(company.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByCity method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByCity() {
        System.out.println("searchByCity");
        String city = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setCity(city);
        addressDao.create(address);

        List<Address> result = addressDao.searchByCity(city);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByCity(city.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByState method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByState() {
        System.out.println("searchByState");

        String state = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setState(state);
        addressDao.create(address);

        List<Address> result = addressDao.searchByState(state);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByState(state.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));
    }

    /**
     * Test of searchByZip method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByZip() {
        System.out.println("searchByZip");

        String zip = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setZip(zip);
        addressDao.create(address);

        List<Address> result = addressDao.searchByZip(zip);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByZip(zip.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    @Test
    public void testGetWithString() {
        System.out.println("searchWithGet");
        final Random random = new Random();

        for (int pass = 0; pass < 25; pass++) {

            String[] randomStrings = new String[8];

            for (int i = 0; i < randomStrings.length; i++) {
                randomStrings[i] = UUID.randomUUID().toString();
            }

            Address address = addressBuilder(randomStrings[0],
                    randomStrings[1],
                    randomStrings[2],
                    randomStrings[3],
                    randomStrings[4],
                    randomStrings[5],
                    randomStrings[6],
                    randomStrings[7]);

            int resultId = addressDao.create(address).getId();

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            Address result = addressDao.get(searchString);

            if (address.equals(result)) {
                System.out.println("Thing");
            }

            assertEquals(result, address);
            addressDao.delete(resultId);
        }

        for (int pass = 0; pass < 150; pass++) {

            String[] randomStrings = new String[8];

            for (int i = 0; i < randomStrings.length; i++) {
                randomStrings[i] = UUID.randomUUID().toString();
                randomStrings[i] = caseRandomizer(random, randomStrings[i]);
            }

            Address address = addressBuilder(randomStrings[0],
                    randomStrings[1],
                    randomStrings[2],
                    randomStrings[3],
                    randomStrings[4],
                    randomStrings[5],
                    randomStrings[6],
                    randomStrings[7]);

            int resultId = addressDao.create(address).getId();

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            int minimumStringLength = 10;
            int processLength = searchString.length() - minimumStringLength;
            int startingPostition = random.nextInt(processLength - minimumStringLength);
            int endingPostition = random.nextInt(processLength - startingPostition) + startingPostition + minimumStringLength;

            searchString = searchString.substring(startingPostition, endingPostition);
            searchString = caseRandomizer(random, searchString);

            Address result = addressDao.get(searchString);

            if (!address.equals(result)) {
                System.out.println("Thing");
            }

            assertEquals(result, address);
            addressDao.delete(resultId);
        }
    }

    private String caseRandomizer(final Random random, String input) {
        switch (random.nextInt(6)) {

            case 0:
                input = input;
                break;
            case 1:
                input = input.toLowerCase();
                break;
            case 2:
                input = input.toUpperCase();
                break;
            default:
                char[] charArray = input.toCharArray();
                for (int j = 0; j < charArray.length; j++) {
                    switch (random.nextInt(4)) {
                        case 1:
                            charArray[j] = Character.toLowerCase(charArray[j]);
                            break;
                        case 2:
                            charArray[j] = Character.toUpperCase(charArray[j]);
                            break;
                        case 3:
                            charArray[j] = Character.toTitleCase(charArray[j]);
                            break;
                        default:
                            charArray[j] = charArray[j];
                            break;
                    }

                    input = new String(charArray);
                }
        }

        return input;
    }

    private Address addressGenerator() {
        String city = UUID.randomUUID().toString();
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String zip = UUID.randomUUID().toString();
        String company = UUID.randomUUID().toString();
        String streetNumber = UUID.randomUUID().toString();
        String streetName = UUID.randomUUID().toString();

        Address address = addressBuilder(city, company, firstName, lastName, state, streetName, streetNumber, zip);
        return address;
    }
}
