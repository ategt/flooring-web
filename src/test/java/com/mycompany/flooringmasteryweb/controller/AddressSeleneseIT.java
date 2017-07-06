/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dto.Address;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Node;

/**
 *
 * @author ATeg
 */
public class AddressSeleneseIT {

    ApplicationContext ctx;
    URI uriToTest;

    Integer addressCountFromIndex = null;

    public AddressSeleneseIT() {
        ctx = new ClassPathXmlApplicationContext("integrationTest-Context.xml");
    }

    @Before
    public void setUp() {
        uriToTest = ctx.getBean("baseUrlToTest", URI.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void listTest() throws MalformedURLException, IOException {

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = HttpUrl.get(uriToTest)
                .newBuilder()
                .addPathSegment("address")
                .addPathSegment("search")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Address Book");
    }

    @Test
    public void testSearch() throws IOException {

        HttpUrl httpUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("search")
                .build();

        WebClient webClient = new WebClient();
        webClient.addRequestHeader("Accept", "application/json");

        List<NameValuePair> paramsList = new ArrayList();

        paramsList.add(new NameValuePair("searchText", "bill"));
        paramsList.add(new NameValuePair("searchBy", "searchByLastName"));

        WebRequest webRequest = new WebRequest(httpUrl.url(), HttpMethod.POST);
        webRequest.setRequestParameters(paramsList);

        Page page = webClient.getPage(webRequest);
        WebResponse webResponse = page.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        if (webResponse.getContentType().equals("application/json")) {
            String json = webResponse.getContentAsString();

            assertEquals(webResponse.getStatusCode(), 200);

            Gson gson = new GsonBuilder().create();

            Address[] addresses = gson.fromJson(json, Address[].class);

            assertEquals(addresses.length, 1);
        } else {
            fail("Response was supposed to be json.");
        }
    }

    @Test
    public void loadIndexPage() throws IOException {
        HttpUrl httpUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient webClient = new WebClient();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());
        WebResponse webResponse = htmlPage.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        if (webResponse.getContentType().equals("application/json")) {
            fail("Should have been HTML.");
        }

        String title = htmlPage.getTitleText();
        assertEquals(title, "Address Book");
        DomElement addressTable = htmlPage.getElementById("address-table");

        DomNodeList<HtmlElement> addressRows = addressTable.getElementsByTagName("tr");

        assertTrue(addressRows.size() > 200);

        if (addressCountFromIndex == null) {
            addressCountFromIndex = addressRows.size();
        } else {
            assertEquals(addressCountFromIndex.intValue(), addressRows.size());
        }

        HtmlAnchor sortByFirstName = htmlPage.getAnchorByHref("?sort_by=first_name");
        String linkText = sortByFirstName.getTextContent();
        assertEquals(linkText, "First Name");

        Node classNode = sortByFirstName.getAttributes().getNamedItem("class");
        String classValue = classNode.getNodeValue();
        assertEquals(classValue, "mask-link");

        String tagName = sortByFirstName.getTagName();
        assertEquals(tagName, "a");

        String href = sortByFirstName.getHrefAttribute();

        if (href.contains("?")) {
            href = href.replace("?", "");
        }

        URL currentBaseUrl = htmlPage.getBaseURL();

        URL urlWithQuery = HttpUrl.get(currentBaseUrl).newBuilder()
                .query(href).build().url();

        URL firstNameUrl = urlWithQuery;

        HtmlPage firstNameSortedPage = webClient.getPage(firstNameUrl);

        Set<Cookie> firstNameSortedCookies = webClient.getCookies(firstNameUrl);

        String host = httpUrl.url().getHost();

        Cookie cookie = new Cookie(host, "sort_cookie", "first_name");

        Optional<Cookie> optionalSortCookie = firstNameSortedCookies.stream()
                .filter(thisCookie -> Objects.equals(thisCookie.getName(), "sort_cookie"))
                .findAny();

        if (optionalSortCookie.isPresent()) {
            Cookie sortCookie = optionalSortCookie.get();
            String domain = sortCookie.getDomain();
            String name = sortCookie.getName();
            String value = sortCookie.getValue();

            assertEquals(domain, host);
            assertEquals(name, "sort_cookie");
            assertEquals(value, "first_name");

        } else {
            fail("Sort Cookie Could Not Be Found.");
        }
    }

    @Test
    public void loadIndexJson() throws IOException {
        HttpUrl httpUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient webClient = new WebClient();
        webClient.addRequestHeader("Accept", "application/json");

        Page page = webClient.getPage(httpUrl.url());
        WebResponse webResponse = page.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        if (webResponse.getContentType().equals("application/json")) {
            String json = webResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            Address[] addresses = gson.fromJson(json, Address[].class);

            assertTrue(addresses.length > 20);

            if (addressCountFromIndex == null) {
                addressCountFromIndex = addresses.length;
            } else {
                assertEquals(addressCountFromIndex.intValue(), addresses.length);
            }

            assertTrue(Arrays.asList(addresses).stream().anyMatch(address -> address.getId() == 3));
        } else {
            fail("Should have been JSON.");
        }
    }

    @Test
    public void verifyJsonAndHtmlIndexHaveSameAddresses() throws IOException {
        HttpUrl httpUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebRequest jsonRequest = new WebRequest(httpUrl.url());
        jsonRequest.setAdditionalHeader("Accept", "application/json");

        WebRequest htmlRequest = new WebRequest(httpUrl.url());

        WebClient webClient = new WebClient();
        WebClient jsonClient = new WebClient();

        Page jsonPage = jsonClient.getPage(jsonRequest);
        WebResponse jsonResponse = jsonPage.getWebResponse();
        assertEquals(jsonResponse.getStatusCode(), 200);

        HtmlPage htmlPage = webClient.getPage(htmlRequest);
        WebResponse htmlResponse = htmlPage.getWebResponse();
        assertEquals(htmlResponse.getStatusCode(), 200);

        Address[] addresses = null;

        if (jsonResponse.getContentType().equals("application/json")) {
            String json = jsonResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            addresses = gson.fromJson(json, Address[].class);

            assertTrue(addresses.length > 20);
        } else {
            fail("Should have been JSON.");
        }

        DomElement addressTable = htmlPage.getElementById("address-table");

        DomNodeList<HtmlElement> addressRows = addressTable.getElementsByTagName("tr");

        assertEquals(addressRows.size(), addresses.length + 1);

        String htmlText = htmlPage.asText();

        Arrays.stream(addresses)
                .forEach((address) -> {
                    String firstName = address.getFirstName();
                    assertTrue(htmlText.contains(firstName));

                    String lastName = address.getLastName();
                    assertTrue(htmlText.contains(lastName));

                    int id = address.getId();
                    assertTrue(htmlText.contains(Integer.toString(id)));
                });

    }

    @Test
    public void getTest() throws IOException {

        HttpUrl httpUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient webClient = new WebClient();
        webClient.addRequestHeader("Accept", "application/json");

        Page page = webClient.getPage(httpUrl.url());
        WebResponse webResponse = page.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        Address[] addresses = null;

        if (webResponse.getContentType().equals("application/json")) {
            String json = webResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            addresses = gson.fromJson(json, Address[].class);

            assertTrue(addresses.length > 20);
        } else {
            fail("Should have been JSON.");
        }

        Random random = new Random();
        int randomAddressPlace = random.nextInt(addresses.length);

        Address randomAddress = addresses[randomAddressPlace];
        Assert.assertNotNull(randomAddress);

        final int randomAddressId = randomAddress.getId();

        HttpUrl showUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(Integer.toString(randomAddressId))
                .build();

        WebClient showAddressWebClient = new WebClient();
        showAddressWebClient.addRequestHeader("Accept", "application/json");

        Page singleAddressPage = showAddressWebClient.getPage(showUrl.url());
        WebResponse jsonSingleAddressResponse = singleAddressPage.getWebResponse();
        assertEquals(jsonSingleAddressResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + jsonSingleAddressResponse.getContentLength(), jsonSingleAddressResponse.getContentLength() > 50);

        Address specificAddress = null;

        if (jsonSingleAddressResponse.getContentType().equals("application/json")) {
            String json = jsonSingleAddressResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            specificAddress = gson.fromJson(json, Address.class);

            Assert.assertNotNull(specificAddress);

            assertTrue(Arrays.asList(addresses).stream().anyMatch(address -> address.getId() == randomAddressId));
        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(specificAddress);
        Assert.assertEquals(specificAddress, randomAddress);
    }

    @Test
    public void createTest() throws IOException {

        Address address = addressGenerator();
        Assert.assertNotNull(address);
        Assert.assertNull(address.getId());

        HttpUrl createUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient createAddressWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String addressJson = gson.toJson(address);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(addressJson);

        Page createPage = createAddressWebClient.getPage(createRequest);

        String returnedAddressJson = createPage.getWebResponse().getContentAsString();

        Address addressReturned = gson.fromJson(returnedAddressJson, Address.class);

        Assert.assertNotNull(addressReturned);
        Integer returnedAddressId = addressReturned.getId();

        Assert.assertTrue(returnedAddressId > 0);

        address.setId(returnedAddressId);

        Assert.assertEquals(addressReturned, address);

        int addressId = addressReturned.getId();

        HttpUrl showUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(Integer.toString(addressId))
                .build();

        WebClient showAddressWebClient = new WebClient();
        showAddressWebClient.addRequestHeader("Accept", "application/json");

        Page singleAddressPage = showAddressWebClient.getPage(showUrl.url());
        WebResponse jsonSingleAddressResponse = singleAddressPage.getWebResponse();
        assertEquals(jsonSingleAddressResponse.getStatusCode(), 200);
        assertTrue(jsonSingleAddressResponse.getContentLength() > 50);

        Address specificAddress = null;

        if (jsonSingleAddressResponse.getContentType().equals("application/json")) {
            String json = jsonSingleAddressResponse.getContentAsString();
            specificAddress = gson.fromJson(json, Address.class);

            Assert.assertNotNull(specificAddress);
        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(specificAddress);
        Assert.assertEquals(specificAddress, addressReturned);

        Address storedAddress = null;

        HttpUrl showUrl2 = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(Integer.toString(addressId))
                .build();

        WebClient showAddressWebClient2 = new WebClient();
        showAddressWebClient2.addRequestHeader("Accept", "application/json");

        Page singleAddressPage2 = showAddressWebClient2.getPage(showUrl2.url());
        WebResponse jsonSingleAddressResponse2 = singleAddressPage2.getWebResponse();
        assertEquals(jsonSingleAddressResponse2.getStatusCode(), 200);
        assertTrue(jsonSingleAddressResponse2.getContentLength() > 50);

        if (jsonSingleAddressResponse2.getContentType().equals("application/json")) {
            String json = jsonSingleAddressResponse2.getContentAsString();
            storedAddress = gson.fromJson(json, Address.class);

            Assert.assertNotNull(storedAddress);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(storedAddress);
        Assert.assertEquals(storedAddress, addressReturned);
    }

    /**
     * Test of create method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testCRUD() throws IOException {
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

        Gson gson = new GsonBuilder().create();

        // Get Size of Database before creation.
        HttpUrl sizeUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("size")
                .build();

        WebClient sizeWebClient = new WebClient();
        sizeWebClient.addRequestHeader("Accept", "application/json");

        Page sizePage = sizeWebClient.getPage(sizeUrl.url());
        WebResponse sizeResponse = sizePage.getWebResponse();
        assertEquals(sizeResponse.getStatusCode(), 200);
        assertTrue(sizeResponse.getContentLength() < 50);

        Integer beforeCreation = null;

        if (sizeResponse.getContentType().equals("application/json")) {
            String json = sizeResponse.getContentAsString();
            beforeCreation = gson.fromJson(json, Integer.class);

            Assert.assertNotNull(beforeCreation);
        } else {
            fail("Should have been JSON.");
        }

        // Create Address
        HttpUrl createUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient createAddressWebClient = new WebClient();

        String addressJson = gson.toJson(address);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(addressJson);

        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createPage = createAddressWebClient.getPage(createRequest);

        String returnedAddressJson = createPage.getWebResponse().getContentAsString();

        Address addressReturned = gson.fromJson(returnedAddressJson, Address.class);

        // Get Database Size After Creation
        WebClient sizeWebClient2 = new WebClient();
        sizeWebClient2.addRequestHeader("Accept", "application/json");

        Page sizePage2 = sizeWebClient2.getPage(sizeUrl.url());
        WebResponse sizeResponse2 = sizePage2.getWebResponse();
        assertEquals(sizeResponse2.getStatusCode(), 200);
        assertTrue(sizeResponse2.getContentLength() < 50);

        Integer afterCreation = null;

        if (sizeResponse2.getContentType().equals("application/json")) {
            String json = sizeResponse2.getContentAsString();
            afterCreation = gson.fromJson(json, Integer.class);

            Assert.assertNotNull(afterCreation);
        } else {
            fail("Should have been JSON.");
        }

        assertEquals(beforeCreation.intValue() + 1, afterCreation.intValue());

        assertNotNull(addressReturned);
        assertNotNull(addressReturned.getId());

        assertTrue(addressReturned.getId() > 0);

        // Check that created address is in the Database.
        HttpUrl showUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(Integer.toString(addressReturned.getId()))
                .build();

        WebClient showAddressWebClient = new WebClient();
        showAddressWebClient.addRequestHeader("Accept", "application/json");

        Page singleAddressPage = showAddressWebClient.getPage(showUrl.url());
        WebResponse jsonSingleAddressResponse = singleAddressPage.getWebResponse();
        assertEquals(jsonSingleAddressResponse.getStatusCode(), 200);
        assertTrue(jsonSingleAddressResponse.getContentLength() > 50);

        Address specificAddress = null;

        if (jsonSingleAddressResponse.getContentType().equals("application/json")) {
            String json = jsonSingleAddressResponse.getContentAsString();
            specificAddress = gson.fromJson(json, Address.class);

            Assert.assertNotNull(specificAddress);

        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(specificAddress);

        assertEquals(addressReturned, specificAddress);

        String updatedCity = UUID.randomUUID().toString();
        addressReturned.setCity(updatedCity);

        // Update Address With Service PUT endpoint
        HttpUrl updateUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient updateAddressWebClient = new WebClient();

        String updatedAddressJson = gson.toJson(addressReturned);

        WebRequest updateRequest = new WebRequest(updateUrl.url(), HttpMethod.PUT);
        updateRequest.setRequestBody(updatedAddressJson);

        updateRequest.setAdditionalHeader("Accept", "application/json");
        updateRequest.setAdditionalHeader("Content-type", "application/json");

        Page updatePage = updateAddressWebClient.getPage(updateRequest);

        String returnedUpdatedAddressJson = updatePage.getWebResponse().getContentAsString();

        Address returnedUpdatedAddress = gson.fromJson(returnedUpdatedAddressJson, Address.class);

        assertEquals(updatedCity, returnedUpdatedAddress.getCity());

        // Get Address By Company
        HttpUrl searchUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("search")
                .build();

        WebClient searchWebClient = new WebClient();

        List<NameValuePair> paramsList = new ArrayList();

        paramsList.add(new NameValuePair("searchText", updatedCity));
        paramsList.add(new NameValuePair("searchBy", "searchByCompany"));

        WebRequest searchByCompanyRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCompanyRequest.setRequestParameters(paramsList);

        searchByCompanyRequest.setAdditionalHeader("Accept", "application/json");
        //searchByCompanyRequest.setAdditionalHeader("Content-type", "application/json");

        Page companySearchPage = searchWebClient.getPage(searchByCompanyRequest);

        assertEquals(companySearchPage.getWebResponse().getStatusCode(), 200);

        String companySearchAddressJson = companySearchPage.getWebResponse().getContentAsString();

        Address[] returnedCompanySearchAddresses = gson.fromJson(companySearchAddressJson, Address[].class);

        assertEquals(returnedCompanySearchAddresses.length, 1);

        Address returnedCompanySearchAddress = returnedCompanySearchAddresses[0];

        assertEquals(returnedUpdatedAddress, returnedCompanySearchAddress);
        assertNotEquals(addressReturned, returnedCompanySearchAddress);

        // Verify Update Did Not Increase the Size of the Database
        WebClient sizeWebClient3 = new WebClient();
        sizeWebClient3.addRequestHeader("Accept", "application/json");

        Page sizePage3 = sizeWebClient3.getPage(sizeUrl.url());
        WebResponse sizeResponse3 = sizePage3.getWebResponse();
        assertEquals(sizeResponse3.getStatusCode(), 200);
        assertTrue(sizeResponse3.getContentLength() < 50);

        Integer afterUpdate = null;

        if (sizeResponse3.getContentType().equals("application/json")) {
            String json = sizeResponse3.getContentAsString();
            afterUpdate = gson.fromJson(json, Integer.class);

            Assert.assertNotNull(afterUpdate);
        } else {
            fail("Should have been JSON.");
        }

        assertEquals(afterUpdate.intValue(), afterCreation.intValue());

        // Delete the Created Address
        HttpUrl deleteUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(addressReturned.getId().toString())
                .build();

        WebClient deleteAddressWebClient = new WebClient();

        WebRequest deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        Page deletePage = updateAddressWebClient.getPage(updateRequest);

        String returnedDeleteAddressJson = deletePage.getWebResponse().getContentAsString();

        Address returnedDeleteAddress = gson.fromJson(returnedDeleteAddressJson, Address.class);

        assertEquals(returnedDeleteAddressJson, returnedUpdatedAddress);

        // Verify Address Database Size Has Shrunk By One After Deletion
        WebClient sizeWebClient4 = new WebClient();
        sizeWebClient4.addRequestHeader("Accept", "application/json");

        Page sizePage4 = sizeWebClient4.getPage(sizeUrl.url());
        WebResponse sizeResponse4 = sizePage4.getWebResponse();
        assertEquals(sizeResponse4.getStatusCode(), 200);
        assertTrue(sizeResponse4.getContentLength() < 50);

        Integer afterDeletion = null;

        if (sizeResponse4.getContentType().equals("application/json")) {
            String json = sizeResponse4.getContentAsString();
            afterDeletion = gson.fromJson(json, Integer.class);

            Assert.assertNotNull(afterDeletion);
        } else {
            fail("Should have been JSON.");
        }

        assertEquals(beforeCreation.intValue(), afterDeletion.intValue());
        assertEquals(afterCreation.intValue() - 1, afterDeletion.intValue());

        // Try to Get Deleted Address
        HttpUrl showUrl2 = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(Integer.toString(addressReturned.getId()))
                .build();

        WebClient showAddressWebClient2 = new WebClient();
        showAddressWebClient2.addRequestHeader("Accept", "application/json");

        Page singleAddressPage2 = showAddressWebClient2.getPage(showUrl.url());
        WebResponse jsonSingleAddressResponse2 = singleAddressPage2.getWebResponse();
        assertEquals(jsonSingleAddressResponse2.getStatusCode(), 200);
        assertTrue(jsonSingleAddressResponse2.getContentLength() > 50);

        Address alreadyDeletedAddress = null;

        if (jsonSingleAddressResponse2.getContentType().equals("application/json")) {
            String json = jsonSingleAddressResponse2.getContentAsString();
            alreadyDeletedAddress = gson.fromJson(json, Address.class);

            Assert.assertNull(alreadyDeletedAddress);

        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNull(alreadyDeletedAddress);

        // Get Deleted Address By Company
        WebClient searchWebClient2 = new WebClient();

        List<NameValuePair> paramsList2 = new ArrayList();

        paramsList2.add(new NameValuePair("searchText", updatedCity));
        paramsList2.add(new NameValuePair("searchBy", "searchByCompany"));

        WebRequest searchByCompanyRequest2 = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCompanyRequest2.setRequestParameters(paramsList2);

        Page companySearchPage2 = searchWebClient2.getPage(searchByCompanyRequest2);

        assertEquals(companySearchPage2.getWebResponse().getStatusCode(), 200);

        String companySearchAddressJson2 = companySearchPage2.getWebResponse().getContentAsString();

        Address[] returnedCompanySearchAddresses2 = gson.fromJson(companySearchAddressJson2, Address[].class);

        assertEquals(returnedCompanySearchAddresses.length, 1);

        Address alsoDeleted = returnedCompanySearchAddresses2[0];

        assertNull(alsoDeleted);

        // Search for deleted Company by get Search
        HttpUrl searchUrl2 = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(updatedCity)
                .addPathSegment("search")
                .build();

        WebClient showAddressWebClient3 = new WebClient();
        showAddressWebClient3.addRequestHeader("Accept", "application/json");

        Page singleAddressPage3 = showAddressWebClient3.getPage(showUrl.url());
        WebResponse jsonSingleAddressResponse3 = singleAddressPage3.getWebResponse();
        assertEquals(jsonSingleAddressResponse3.getStatusCode(), 200);
        assertTrue(jsonSingleAddressResponse3.getContentLength() > 50);

        Address alsoDeleted2 = null;

        if (jsonSingleAddressResponse3.getContentType().equals("application/json")) {
            String json = jsonSingleAddressResponse3.getContentAsString();
            alsoDeleted2 = gson.fromJson(json, Address.class);

            Assert.assertNull(alsoDeleted2);

        } else {
            fail("Should have been JSON.");
        }

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
    public void testList() throws IOException {
        System.out.println("list");

        Address address = addressGenerator();

        // Create Generated Address
        HttpUrl createUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient createAddressWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String addressJson = gson.toJson(address);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(addressJson);

        createAddressWebClient.getPage(createRequest);

        // Get The List Of Addresses
        HttpUrl getListUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient getListWebClient = new WebClient();
        getListWebClient.addRequestHeader("Accept", "application/json");

        Page getListPage = getListWebClient.getPage(getListUrl.url());
        WebResponse getListWebResponse = getListPage.getWebResponse();
        assertEquals(getListWebResponse.getStatusCode(), 200);
        assertTrue(getListWebResponse.getContentLength() > 100);

        List<Address> list = null;

        if (getListWebResponse.getContentType().equals("application/json")) {
            String json = getListWebResponse.getContentAsString();
            Address[] addresses = gson.fromJson(json, Address[].class);

            assertTrue(addresses.length > 20);

            list = Arrays.asList(addresses);
        } else {
            fail("Should have been JSON.");
        }

        // Get Database Size
        HttpUrl sizeUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("size")
                .build();

        WebClient sizeWebClient = new WebClient();
        sizeWebClient.addRequestHeader("Accept", "application/json");

        Page sizePage = sizeWebClient.getPage(sizeUrl.url());
        WebResponse sizeResponse = sizePage.getWebResponse();
        assertEquals(sizeResponse.getStatusCode(), 200);
        assertTrue(sizeResponse.getContentLength() < 50);

        Integer databaseSize = null;

        if (sizeResponse.getContentType().equals("application/json")) {
            String json = sizeResponse.getContentAsString();
            databaseSize = gson.fromJson(json, Integer.class);

            Assert.assertNotNull(databaseSize);
        } else {
            fail("Should have been JSON.");
        }

        assertEquals(list.size(), databaseSize.intValue());

        assertTrue(list.contains(address));
    }

//    /**
//     * Test of searchByLastName method, of class AddressDaoPostgresImpl.
//     */
//    @Test
//    public void testSearchByLastName() {
//        System.out.println("searchByLastName");
//        String lastName = UUID.randomUUID().toString();
//        
//        Address address = addressGenerator();
//        address.setLastName(lastName);
//        address = addressDao.create(address);
//        
//        List<Address> result = addressDao.searchByLastName(lastName);
//        
//        assertNotNull(result);
//        assertTrue(result.contains(address));
//        assertEquals(result.size(), 1);
//        
//        result = addressDao.searchByLastName(lastName.toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByLastName(lastName.toUpperCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByLastName(lastName.substring(5));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByLastName(lastName.substring(5, 20));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByLastName(lastName.substring(5, 20).toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByLastName(lastName.substring(5, 20).toUpperCase());
//        assertTrue(result.contains(address));
//        
//    }
//    /**
//     * Test of searchByFirstName method, of class AddressDaoPostgresImpl.
//     */
//    @Test
//    public void testSearchByFirstName() {
//        System.out.println("searchByFirstName");
//        
//        String firstName = UUID.randomUUID().toString();
//        
//        Address address = addressGenerator();
//        address.setFirstName(firstName);
//        addressDao.create(address);
//        
//        List<Address> result = addressDao.searchByFirstName(firstName);
//        assertTrue(result.contains(address));
//        assertEquals(result.size(), 1);
//        
//        result = addressDao.searchByFirstName(firstName.toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByFirstName(firstName.toUpperCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByFirstName(firstName.substring(5));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByFirstName(firstName.substring(5, 20));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByFirstName(firstName.substring(5, 20).toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByFirstName(firstName.substring(5, 20).toUpperCase());
//        assertTrue(result.contains(address));
//        
//    }
//    /**
//     * Test of searchByFirstName method, of class AddressDaoPostgresImpl.
//     */
//    @Test
//    public void testSearchByCompany() {
//        System.out.println("searchByCompany");
//        
//        String company = UUID.randomUUID().toString();
//        
//        Address address = addressGenerator();
//        address.setCompany(company);
//        addressDao.create(address);
//        
//        List<Address> result = addressDao.searchByCompany(company);
//        assertTrue(result.contains(address));
//        assertEquals(result.size(), 1);
//        
//        result = addressDao.searchByCompany(company.toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCompany(company.toUpperCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCompany(company.substring(5));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCompany(company.substring(5, 20));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCompany(company.substring(5, 20).toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCompany(company.substring(5, 20).toUpperCase());
//        assertTrue(result.contains(address));
//        
//    }
//    /**
//     * Test of searchByCity method, of class AddressDaoPostgresImpl.
//     */
//    @Test
//    public void testSearchByCity() {
//        System.out.println("searchByCity");
//        String city = UUID.randomUUID().toString();
//        
//        Address address = addressGenerator();
//        address.setCity(city);
//        addressDao.create(address);
//        
//        List<Address> result = addressDao.searchByCity(city);
//        assertTrue(result.contains(address));
//        assertEquals(result.size(), 1);
//        
//        result = addressDao.searchByCity(city.toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCity(city.toUpperCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCity(city.substring(5));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCity(city.substring(5, 20));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCity(city.substring(5, 20).toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByCity(city.substring(5, 20).toUpperCase());
//        assertTrue(result.contains(address));
//        
//    }
//    /**
//     * Test of searchByState method, of class AddressDaoPostgresImpl.
//     */
//    @Test
//    public void testSearchByState() {
//        System.out.println("searchByState");
//        
//        String state = UUID.randomUUID().toString();
//        
//        Address address = addressGenerator();
//        address.setState(state);
//        addressDao.create(address);
//        
//        List<Address> result = addressDao.searchByState(state);
//        assertTrue(result.contains(address));
//        assertEquals(result.size(), 1);
//        
//        result = addressDao.searchByState(state.toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByState(state.toUpperCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByState(state.substring(5));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByState(state.substring(5, 20));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByState(state.substring(5, 20).toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByState(state.substring(5, 20).toUpperCase());
//        assertTrue(result.contains(address));
//    }
//    /**
//     * Test of searchByZip method, of class AddressDaoPostgresImpl.
//     */
//    @Test
//    public void testSearchByZip() {
//        System.out.println("searchByZip");
//        
//        String zip = UUID.randomUUID().toString();
//        
//        Address address = addressGenerator();
//        address.setZip(zip);
//        addressDao.create(address);
//        
//        List<Address> result = addressDao.searchByZip(zip);
//        assertTrue(result.contains(address));
//        assertEquals(result.size(), 1);
//        
//        result = addressDao.searchByZip(zip.toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByZip(zip.toUpperCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByZip(zip.substring(5));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByZip(zip.substring(5, 20));
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByZip(zip.substring(5, 20).toLowerCase());
//        assertTrue(result.contains(address));
//        
//        result = addressDao.searchByZip(zip.substring(5, 20).toUpperCase());
//        assertTrue(result.contains(address));
//        
//    }
//    @Test
//    public void testGetWithString() {
//        System.out.println("searchWithGet");
//        final Random random = new Random();
//        
//        for (int pass = 0; pass < 25; pass++) {
//            
//            String[] randomStrings = new String[8];
//            
//            for (int i = 0; i < randomStrings.length; i++) {
//                randomStrings[i] = UUID.randomUUID().toString();
//            }
//            
//            Address address = addressBuilder(randomStrings[0],
//                    randomStrings[1],
//                    randomStrings[2],
//                    randomStrings[3],
//                    randomStrings[4],
//                    randomStrings[5],
//                    randomStrings[6],
//                    randomStrings[7]);
//            
//            address = addressDao.create(address);
//            int resultId = address.getId();
//            
//            int position = new Random().nextInt(randomStrings.length);
//            String searchString = randomStrings[position];
//            
//            Address result = addressDao.get(searchString);
//
//            assertEquals(result, address);
//            addressDao.delete(resultId);
//        }
//        
//        for (int pass = 0; pass < 150; pass++) {
//            
//            String[] randomStrings = new String[8];
//            
//            for (int i = 0; i < randomStrings.length; i++) {
//                randomStrings[i] = UUID.randomUUID().toString();
//                randomStrings[i] = caseRandomizer(random, randomStrings[i]);
//            }
//            
//            Address address = addressBuilder(randomStrings[0],
//                    randomStrings[1],
//                    randomStrings[2],
//                    randomStrings[3],
//                    randomStrings[4],
//                    randomStrings[5],
//                    randomStrings[6],
//                    randomStrings[7]);
//            
//            address = addressDao.create(address);
//            int resultId = address.getId();
//            
//            int position = new Random().nextInt(randomStrings.length);
//            String searchString = randomStrings[position];
//            
//            int minimumStringLength = 10;
//            int processLength = searchString.length() - minimumStringLength;
//            int startingPostition = random.nextInt(processLength - minimumStringLength);
//            int endingPostition = random.nextInt(processLength - startingPostition) + startingPostition + minimumStringLength;
//            
//            searchString = searchString.substring(startingPostition, endingPostition);
//            searchString = caseRandomizer(random, searchString);
//            
//            Address result = addressDao.get(searchString);
//            
//            assertEquals(result, address);
//            addressDao.delete(resultId);
//        }
//    }
//    @SuppressWarnings("Since15")
//    @Test
//    public void getSortedByName() {
//        List<Address> addresses = addressDao.list();
//        List<Address> addressesFromDb = addressDao.list();
//        
//        addresses.sort(new Comparator<Address>() {
//            @Override
//            public int compare(Address address1, Address address2) {
//                return address1.getLastName().toLowerCase().compareTo(address2.getLastName().toLowerCase());
//            }
//        });
//        
//        for (int i = 0; i < addresses.size(); i++) {
//            assertEquals(addresses.get(i), addressesFromDb.get(i));
//        }
//    }
//    @Test
//    public void getSortedByNameUsingSortByParam() {
//        List<Address> addresses = addressDao.list();
//        List<Address> addressesFromDb = addressDao.getAddressesSortedByParameter("last_name");
//
//        //noinspection Since15
//        addresses.sort(new Comparator<Address>() {
//            @Override
//            public int compare(Address address1, Address address2) {
//                return address1.getLastName().toLowerCase().compareTo(address2.getLastName().toLowerCase());
//            }
//        });
//        
//        for (int i = 0; i < addresses.size(); i++) {
//            
//            assertEquals(addresses.get(i), addressesFromDb.get(i));
//            
//        }
//    }
//    @Test
//    public void getSortedByIdUsingSortByParam() {
//        List<Address> addresses = addressDao.list(AddressDao.SORT_BY_ID);
//        List<Address> addressesFromDb = addressDao.getAddressesSortedByParameter("id");
//        
//        for (int i = 0; i < addresses.size(); i++) {
//            
//            assertEquals(addresses.get(i), addressesFromDb.get(i));
//            
//        }
//    }
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
