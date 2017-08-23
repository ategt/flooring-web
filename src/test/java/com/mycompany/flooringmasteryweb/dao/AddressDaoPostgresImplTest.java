/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.google.common.base.Strings;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.Repeat;

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

        List<Address> companyAddresses = addressDao.search(new AddressSearchRequest(retrivedAddress.getCompany(), AddressSearchByOptionEnum.COMPANY), null);
        Address companyAddress = companyAddresses.get(0);

        assertEquals(companyAddress, retrivedAddress);
        assertNotEquals(result, companyAddress);

        assertEquals(afterCreation, addressDao.size());

        addressDao.delete(companyAddress.getId());

        assertEquals(afterCreation - 1, addressDao.size());

        Address deletedAddress = addressDao.get(companyAddress.getId());
        assertNull(deletedAddress);

        List<Address> alsoDeleted = addressDao.search(new AddressSearchRequest(company, AddressSearchByOptionEnum.COMPANY), null);
        assertTrue(alsoDeleted.isEmpty());

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
     * Test of searchByLastName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByLastNameUsingNewMethod() {
        System.out.println("search By Last Name Using New Way");
        String lastName = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setLastName(lastName);
        addressDao.create(address);

        List<Address> result = addressDao.search(new AddressSearchRequest(lastName, AddressSearchByOptionEnum.LAST_NAME), null);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.search(new AddressSearchRequest(lastName.toLowerCase(), AddressSearchByOptionEnum.LAST_NAME), null);
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(lastName.toUpperCase(), AddressSearchByOptionEnum.LAST_NAME), null);
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(lastName.substring(5), AddressSearchByOptionEnum.LAST_NAME), null);
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(lastName.substring(5, 20), AddressSearchByOptionEnum.LAST_NAME), null);
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(lastName.substring(5, 20).toLowerCase(), AddressSearchByOptionEnum.LAST_NAME), null);
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(lastName.substring(5, 20).toUpperCase(), AddressSearchByOptionEnum.LAST_NAME), null);
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
    public void testSearchByFullName() {
        System.out.println("searchByFullName");

        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();

        String fullName = firstName + " " + lastName;

        Address address = addressGenerator();
        address.setFirstName(firstName);
        address.setLastName(lastName);
        address = addressDao.create(address);

        List<Address> result = addressDao.searchByFullName(fullName);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByFullName(fullName.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFullName(fullName.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFullName(fullName.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByFullName(fullName.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByFullName(fullName.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFullName(fullName.substring(5, 20).toUpperCase());
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
    public void testSearchByEverything() {
        System.out.println("search By Everything");

        for (int i = 0; i < 5; i++) {

            for (AddressSearchByOptionEnum addressSearchByOptionEnum : AddressSearchByOptionEnum.values()) {

                String searchString = null;

                Address address = addressGenerator();

                switch (addressSearchByOptionEnum) {
                    case FIRST_NAME:
                        searchString = address.getFirstName();
                        break;
                    case LAST_NAME:
                        searchString = address.getLastName();
                        break;
                    case FULL_NAME:
                        searchString = address.getFullName();
                        break;
                    case COMPANY:
                        searchString = address.getCompany();
                        break;
                    case NAME:
                        String[] nameOptions = {address.getFirstName(), address.getLastName(), address.getFullName()};
                        searchString = nameOptions[new Random().nextInt(nameOptions.length)];
                        break;
                    case NAME_OR_COMPANY:
                        String[] nameOrCompanyOptions = {address.getFirstName(), address.getLastName(), address.getFullName(), address.getCompany()};
                        searchString = nameOrCompanyOptions[new Random().nextInt(nameOrCompanyOptions.length)];
                        break;
                    case STREET_NUMBER:
                        searchString = address.getStreetNumber();
                        break;
                    case STREET_NAME:
                        searchString = address.getStreetName();
                        break;
                    case STREET:
                        searchString = address.getStreetNumber() + " " + address.getStreetName();
                        break;
                    case CITY:
                        searchString = address.getCity();
                        break;
                    case STATE:
                        searchString = address.getState();
                        break;
                    case ZIP:
                        searchString = address.getZip();
                        break;
                    case ALL:
                    case ANY:
                    case DEFAULT:
                        String[] allOptions = {address.getFirstName(),
                            address.getLastName(),
                            address.getFullName(),
                            address.getCompany(),
                            address.getCity(),
                            address.getState(),
                            address.getStreetName(),
                            address.getStreetNumber(),
                            address.getZip()};
                        searchString = allOptions[new Random().nextInt(allOptions.length)];
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException();
                }

                addressDao.create(address);

                List<Address> result = addressDao.search(new AddressSearchRequest(searchString, addressSearchByOptionEnum), null);
                assertTrue("Search String: " + searchString + ", Search Option: " + addressSearchByOptionEnum.value(), result.contains(address));
                assertEquals(result.size(), 1);

                result = addressDao.search(new AddressSearchRequest(searchString.toLowerCase(), addressSearchByOptionEnum), null);
                assertTrue(result.contains(address));

                result = addressDao.search(new AddressSearchRequest(searchString.toUpperCase(), addressSearchByOptionEnum), null);
                assertTrue(result.contains(address));

                result = addressDao.search(new AddressSearchRequest(searchString.substring(5), addressSearchByOptionEnum), null);
                assertTrue(result.contains(address));

                result = addressDao.search(new AddressSearchRequest(searchString.substring(5, 20), addressSearchByOptionEnum), null);
                assertTrue(result.contains(address));

                result = addressDao.search(new AddressSearchRequest(searchString.substring(5, 20).toLowerCase(), addressSearchByOptionEnum), null);
                assertTrue(result.contains(address));

                result = addressDao.search(new AddressSearchRequest(searchString.substring(5, 20).toUpperCase(), addressSearchByOptionEnum), null);
                assertTrue(result.contains(address));
            }
        }
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

            assertEquals(result, address);
            addressDao.delete(resultId);
        }
    }

    @Test
    public void getSortedByName() {
        List<Address> addresses = addressDao.list();
        List<Address> addressesFromDb = addressDao.list();

        List<Address> removables = getLocaleSpecificAddresses(addressesFromDb);

        addressesFromDb.removeAll(removables);
        addresses.removeAll(removables);

        addresses.sort((Object o1, Object o2) -> {

            Address address1 = (Address) o1;
            Address address2 = (Address) o2;

            return address1.getLastName().toLowerCase().compareTo(address2.getLastName().toLowerCase());
        });

        for (int i = 0; i < addresses.size(); i++) {

            assertEquals(addresses.get(i), addressesFromDb.get(i));

        }
    }

    @Test
    public void getSortedByNameUsingSortByParam() {
        List<Address> addresses = addressDao.list();
        List<Address> addressesFromDb = addressDao.getAddressesSortedByParameter("last_name");

        List<Address> removables = getLocaleSpecificAddresses(addressesFromDb);

        addressesFromDb.removeAll(removables);
        addresses.removeAll(removables);

        addresses.sort((Object o1, Object o2) -> {

            Address address1 = (Address) o1;
            Address address2 = (Address) o2;

            return address1.getLastName().toLowerCase().compareTo(address2.getLastName().toLowerCase());
        });

        for (int i = 0; i < addresses.size(); i++) {
            assertEquals(addresses.get(i), addressesFromDb.get(i));
        }
    }

    private List<Address> getLocaleSpecificAddresses(List<Address> addressesFromDb) {
        List<Address> removables = new ArrayList<>();
        for (Address address : addressesFromDb) {
            if (address.getLastName() == null
                    || Strings.isNullOrEmpty(address.getLastName())
                    || address.getLastName().trim().isEmpty()) {
                removables.add(address);
            }
        }
        return removables;
    }

    @Test
    public void getSortedByIdUsingSortByParam() {
        List<Address> addresses = addressDao.list(AddressSortByEnum.ID);
        List<Address> addressesFromDb = addressDao.getAddressesSortedByParameter("id");

        for (int i = 0; i < addresses.size(); i++) {

            assertEquals(addresses.get(i), addressesFromDb.get(i));

        }
    }

    @Test
    public void getSortDoesNotReturnErrors() {

        Address testAddress = addressGenerator();

        for (int i = 0; i < 10; i++) {
            testAddress.setFirstName(UUID.randomUUID().toString());
            addressDao.create(testAddress);
        }

        addressDao.create(testAddress);

        Address nullTestAddress = new Address();
        nullTestAddress.setFirstName("A");
        addressDao.create(nullTestAddress);

        nullTestAddress = new Address();
        nullTestAddress.setFirstName("a");
        addressDao.create(nullTestAddress);

        nullTestAddress = new Address();
        addressDao.create(nullTestAddress);

        nullTestAddress = new Address();
        nullTestAddress.setFirstName("");
        addressDao.create(nullTestAddress);

        nullTestAddress = new Address();
        nullTestAddress.setFirstName(" ");
        addressDao.create(nullTestAddress);

        int size = addressDao.size();

        for (AddressSortByEnum addressSortByEnum : AddressSortByEnum.values()) {
            List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(0, Integer.MAX_VALUE, addressSortByEnum));

            assertNotNull(addressesFromDb);
            assertEquals(addressesFromDb.size(), size);
        }
    }

    @Test
    public void getSortedById() {
        List<Address> addresses = addressDao.list();
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(0, Integer.MAX_VALUE, AddressSortByEnum.ID));

        addresses.sort((Object o1, Object o2) -> {

            Address address1 = (Address) o1;
            Address address2 = (Address) o2;

            return address1.getId().compareTo(address2.getId());
        });

        for (int i = 0; i < addresses.size(); i++) {
            assertEquals(addresses.get(i), addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByIdReverse() {
        List<Address> addresses = addressDao.list();
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(0, Integer.MAX_VALUE, AddressSortByEnum.ID_INVERSE));

        addresses.sort((Object o1, Object o2) -> {

            Address address1 = (Address) o1;
            Address address2 = (Address) o2;

            return -(address1.getId().compareTo(address2.getId()));
        });

        for (int i = 0; i < addresses.size(); i++) {
            assertEquals(addresses.get(i), addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByLastName() {
        List<Address> addresses = addressDao.list();
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(0, Integer.MAX_VALUE, AddressSortByEnum.LAST_NAME));

        List<Address> removables = getLocaleSpecificAddresses(addressesFromDb);

        addressesFromDb.removeAll(removables);
        addresses.removeAll(removables);

        addresses.sort((Object o1, Object o2) -> {

            Address address1 = (Address) o1;
            Address address2 = (Address) o2;

            return address1.getLastName().toLowerCase().compareTo(address2.getLastName().toLowerCase());
        });

        for (int i = 0; i < addresses.size(); i++) {
            assertEquals(addresses.get(i), addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByLastNameReverse() {
        List<Address> addresses = addressDao.list();
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(0, Integer.MAX_VALUE, AddressSortByEnum.LAST_NAME_INVERSE));

        List<Address> removables = getLocaleSpecificAddresses(addressesFromDb);

        addressesFromDb.removeAll(removables);
        addresses.removeAll(removables);

        addresses.sort((Object o1, Object o2) -> {

            Address address1 = (Address) o1;
            Address address2 = (Address) o2;

            int result = 0;
             result = -(address1.getLastName().toLowerCase().compareTo(address2.getLastName().toLowerCase()));
             
             if (result == 0){
                 result = -(address1.getFirstName().toLowerCase().compareTo(address2.getFirstName().toLowerCase()));
             }

             if (result == 0){
                 result = -(address1.getCompany().toLowerCase().compareTo(address2.getCompany().toLowerCase()));
             }

             if (result == 0){
                 result = -(address1.getId().compareTo(address2.getId()));
             }
             
             return result;
        });

        for (int i = 0; i < addresses.size(); i++) {
            assertEquals("Pass:" + i + ", Java-" + addresses.get(i).getId() + ":" + addresses.get(i).getFullName() + ", DB-" + addressesFromDb.get(i).getId() + ":" + addressesFromDb.get(i).getFullName(),
                    addresses.get(i), addressesFromDb.get(i));
        }
    }

    @Test
    public void getFirstPage() {
        AddressResultSegment resultSegment = new AddressResultSegment(0, 50, AddressSortByEnum.LAST_NAME);
        List<Address> addresses = addressDao.list(resultSegment);

        assertNotNull(addresses);
        assertEquals(addresses.size(), 50);
    }

    @Test
    public void getLargeList() {
        AddressResultSegment resultSegment = new AddressResultSegment(0, Integer.MAX_VALUE, AddressSortByEnum.LAST_NAME);
        List<Address> addresses = addressDao.list(resultSegment);

        List<Address> allAddresses = addressDao.list();

        assertEquals(allAddresses.size(), addresses.size());
        assertEquals(addressDao.size(), addresses.size());

        allAddresses.sort((Address o1, Address o2) -> Integer.compare(o1.getId(), o2.getId()));
        addresses.sort((Address o1, Address o2) -> Integer.compare(o1.getId(), o2.getId()));

        assertArrayEquals(addresses.toArray(), allAddresses.toArray());

        for (int i = 0; i < allAddresses.size(); i++) {
            assertEquals(allAddresses.get(i), addresses.get(i));
        }
    }

    @Test
    public void getSeveralSegments() {

        int size = addressDao.size();

        int resultsPerPage = 50;

        List<Address> cumulativeAddress = new ArrayList<>();

        for (int page = 0; page < (size / resultsPerPage) + 1; page++) {
            AddressResultSegment resultSegment = new AddressResultSegment(page, resultsPerPage, AddressSortByEnum.LAST_NAME);
            List<Address> addresses = addressDao.list(resultSegment);

            if (page == (size / resultsPerPage)) {
                // This is the last page, it may contain a fraction of the expected results.
                assertTrue("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size() + ", Results:" + resultsPerPage, addresses.size() <= resultsPerPage);
                assertTrue("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size() + ", Results:" + resultsPerPage, addresses.size() > 0);
                assertFalse(addresses.isEmpty());
            } else {
                // This page should contain exactly the expected number of results.
                assertEquals("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size() + ", Results:" + resultsPerPage, addresses.size(), resultsPerPage);
            }

            for (Address address : addresses) {
                assertFalse(cumulativeAddress.contains(address));
            }

            cumulativeAddress.addAll(addresses);
        }

        List<Address> allAddresses = addressDao.list();

        assertEquals(allAddresses.size(), cumulativeAddress.size());

        allAddresses.sort((Address o1, Address o2) -> Integer.compare(o1.getId(), o2.getId()));
        cumulativeAddress.sort((Address o1, Address o2) -> Integer.compare(o1.getId(), o2.getId()));

        assertArrayEquals(cumulativeAddress.toArray(), allAddresses.toArray());

        for (int i = 0; i < allAddresses.size(); i++) {
            assertEquals(allAddresses.get(i), cumulativeAddress.get(i));
        }
    }

    @Test
    public void getSeveralSegmentsRandomly() {
        Random random = new Random();
        int size = addressDao.size();

        int resultsPerPage = random.nextInt(size);
        int totalPages = (size / resultsPerPage) + 1;

        List<Address> cumulativeAddress = new ArrayList<>();

        List<Integer> pages = new ArrayList<>();

        for (int page = 0; page < totalPages; page++) {
            pages.add(page);
        }

        Collections.shuffle(pages);

        for (Integer page : pages) {

            AddressResultSegment resultSegment = new AddressResultSegment(page, resultsPerPage, AddressSortByEnum.LAST_NAME);
            List<Address> addresses = addressDao.list(resultSegment);

            if (totalPages < page + 1) {
                // This page is greater than the total and should be empty.
                assertTrue("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size(), addresses.isEmpty());
            } else if (totalPages == page + 1) {
                // This is the last page, it may contain a fraction of the expected results.
                assertTrue("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size() + ", Results:" + resultsPerPage, addresses.size() <= resultsPerPage);
                assertTrue("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size() + ", Results:" + resultsPerPage, addresses.size() > 0);
                assertFalse(addresses.isEmpty());
            } else {
                // This page should contain exactly the expected number of results.
                assertEquals("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size() + ", Results:" + resultsPerPage, addresses.size(), resultsPerPage);
            }

            for (Address address : addresses) {
                assertFalse("Address id " + address.getId() + " appears twice.", cumulativeAddress.contains(address));
            }

            int lastIndex = (page + 1) * resultsPerPage;
            if (lastIndex > size) {
                lastIndex = size;
            }

            System.out.println("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", - From " + (resultsPerPage * page) + " to " + lastIndex + " while size is " + size);

            List<Address> matchingList = addressDao.list(AddressSortByEnum.LAST_NAME).subList(resultsPerPage * page, lastIndex);

            assertEquals("Page:" + page + ", ResultsPerPage:" + resultsPerPage + ", Addresses:" + addresses.size() + ", MatchingList:" + matchingList.size(), addresses.size(), matchingList.size());

            for (int i = 0; i < matchingList.size(); i++) {
                assertEquals(matchingList.get(i), addresses.get(i));
            }

            cumulativeAddress.addAll(addresses);
        }

        List<Address> allAddresses = addressDao.list();

        assertEquals(allAddresses.size(), cumulativeAddress.size());

        allAddresses.sort((Address o1, Address o2) -> Integer.compare(o1.getId(), o2.getId()));
        cumulativeAddress.sort((Address o1, Address o2) -> Integer.compare(o1.getId(), o2.getId()));

        assertArrayEquals(cumulativeAddress.toArray(), allAddresses.toArray());

        for (int i = 0; i < allAddresses.size(); i++) {
            assertEquals(allAddresses.get(i), cumulativeAddress.get(i));
        }
    }

    @Test
    public void emptyPageShouldBeEmpty() {
        int size = addressDao.size();

        List<Address> emptyResults = addressDao.list(new AddressResultSegment(1, size, AddressSortByEnum.COMPANY));

        assertTrue(emptyResults.isEmpty());
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
