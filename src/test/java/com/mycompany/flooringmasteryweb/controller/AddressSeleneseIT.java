/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
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
import com.google.gson.JsonSyntaxException;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
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
        createRequest.setAdditionalHeader("Content-type", "application/json");

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

        assertNotNull(addressReturned.getId());
        assertTrue(addressReturned.getId() > 0);

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
        paramsList.add(new NameValuePair("searchBy", "searchByCity"));

        WebRequest searchByCompanyRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCompanyRequest.setRequestParameters(paramsList);

        searchByCompanyRequest.setAdditionalHeader("Accept", "application/json");

        Page companySearchPage = searchWebClient.getPage(searchByCompanyRequest);

        assertEquals(companySearchPage.getWebResponse().getStatusCode(), 200);

        String companySearchAddressJson = companySearchPage.getWebResponse().getContentAsString();

        Address[] returnedCompanySearchAddresses = gson.fromJson(companySearchAddressJson, Address[].class);

        assertEquals(returnedCompanySearchAddresses.length, 1);

        Address returnedCompanySearchAddress = returnedCompanySearchAddresses[0];

        assertEquals(returnedUpdatedAddress, returnedCompanySearchAddress);
        assertEquals(addressReturned, returnedCompanySearchAddress);
        assertNotEquals(specificAddress, returnedCompanySearchAddress);

        // Check search using json object.
        WebClient jsonSearchWebClient = new WebClient();

        //AddressSearchRequest addressSearchRequest = new AddressSearchRequest(updatedCity, AddressSearchByOptionEnum.CITY);
        //String addressSearchRequestJson = gson.toJson(addressSearchRequest);
        String addressSearchRequestJson = "{\"searchBy\":\"searchByCity\",\"searchText\":\"" + updatedCity + "\"}";

        WebRequest searchByCityRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCityRequest.setRequestBody(addressSearchRequestJson);

        searchByCityRequest.setAdditionalHeader("Accept", "application/json");
        searchByCityRequest.setAdditionalHeader("Content-type", "application/json");

        Page citySearchPage = jsonSearchWebClient.getPage(searchByCityRequest);

        assertEquals(citySearchPage.getWebResponse().getStatusCode(), 200);

        String citySearchAddressJson = citySearchPage.getWebResponse().getContentAsString();

        Address[] returnedCitySearchAddresses = gson.fromJson(citySearchAddressJson, Address[].class);

        assertEquals(returnedCitySearchAddresses.length, 1);

        Address returnedCitySearchAddress = returnedCitySearchAddresses[0];

        assertEquals(returnedUpdatedAddress, returnedCitySearchAddress);
        assertEquals(addressReturned, returnedCitySearchAddress);
        assertNotEquals(specificAddress, returnedCitySearchAddress);

        // Check search using json object built with my purspective api.
        WebClient jsonSearchWebClient2 = new WebClient();

        AddressSearchRequest addressSearchRequest = new AddressSearchRequest(updatedCity, AddressSearchByOptionEnum.CITY);

        String addressSearchRequestJson2 = gson.toJson(addressSearchRequest);
        //String addressSearchRequestJson = "{\"searchBy\":\"searchByCity\",\"searchText\":\"" + updatedCity + "\"}";

        WebRequest searchByCityRequest2 = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCityRequest2.setRequestBody(addressSearchRequestJson2);

        searchByCityRequest2.setAdditionalHeader("Accept", "application/json");
        searchByCityRequest2.setAdditionalHeader("Content-type", "application/json");

        Page citySearchPage2 = jsonSearchWebClient2.getPage(searchByCityRequest2);

        assertEquals(citySearchPage2.getWebResponse().getStatusCode(), 200);

        String citySearchAddressJson2 = citySearchPage2.getWebResponse().getContentAsString();

        Address[] returnedCitySearchAddresses2 = gson.fromJson(citySearchAddressJson2, Address[].class);

        assertEquals(returnedCitySearchAddresses2.length, 1);

        Address returnedCitySearchAddress2 = returnedCitySearchAddresses2[0];

        assertEquals(returnedUpdatedAddress, returnedCitySearchAddress2);
        assertEquals(addressReturned, returnedCitySearchAddress2);
        assertNotEquals(specificAddress, returnedCitySearchAddress2);

        assertEquals(returnedCitySearchAddress, returnedCitySearchAddress2);

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
        String addressIdString = Integer.toString(addressReturned.getId());

        HttpUrl deleteUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(addressIdString)
                .build();

        WebClient deleteAddressWebClient = new WebClient();

        WebRequest deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        Page deletePage = deleteAddressWebClient.getPage(deleteRequest);

        String returnedDeleteAddressJson = deletePage.getWebResponse().getContentAsString();

        Address returnedDeleteAddress = gson.fromJson(returnedDeleteAddressJson, Address.class);

        assertEquals(returnedDeleteAddress, returnedUpdatedAddress);

        // Delete The Created Address A Second Time
        deleteAddressWebClient = new WebClient();

        deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        deletePage = deleteAddressWebClient.getPage(deleteRequest);

        returnedDeleteAddressJson = deletePage.getWebResponse().getContentAsString();
        assertEquals(returnedDeleteAddressJson, "");

        Address returnedDeleteAddress2 = gson.fromJson(returnedDeleteAddressJson, Address.class);
        assertNull(returnedDeleteAddress2);

        assertNotEquals(returnedDeleteAddress, returnedDeleteAddress2);

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
        assertTrue(jsonSingleAddressResponse2.getContentLength() < 50);

        String contentType = jsonSingleAddressResponse2.getContentType();
        assertEquals(contentType, "");

        String contentString = jsonSingleAddressResponse2.getContentAsString();

        assertEquals(contentString, "");

        // Get Deleted Address By Company
        WebClient searchWebClient2 = new WebClient();

        List<NameValuePair> paramsList2 = new ArrayList();

        paramsList2.add(new NameValuePair("searchText", updatedCity));
        paramsList2.add(new NameValuePair("searchBy", "searchByCity"));

        WebRequest searchByCityRequest3 = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCityRequest3.setRequestParameters(paramsList2);
        searchByCityRequest3.setAdditionalHeader("Accept", "application/json");

        Page citySearchPage3 = searchWebClient2.getPage(searchByCityRequest3);

        assertEquals(citySearchPage3.getWebResponse().getStatusCode(), 200);

        String citySearchAddressJson3 = citySearchPage3.getWebResponse().getContentAsString();
        assertEquals(citySearchAddressJson3, "[]");

        Address[] returnedCitySearchAddresses3 = gson.fromJson(citySearchAddressJson3, Address[].class);

        assertEquals(returnedCitySearchAddresses3.length, 0);

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
        assertTrue(jsonSingleAddressResponse3.getContentLength() < 50);

        String contentType2 = jsonSingleAddressResponse3.getContentType();
        assertEquals(contentType2, "");

        String json = jsonSingleAddressResponse3.getContentAsString();
        assertEquals(json, "");

        Address alsoDeleted2 = gson.fromJson(json, Address.class);

        Assert.assertNull(alsoDeleted2);
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
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createdAddressPage = createAddressWebClient.getPage(createRequest);

        WebResponse createAddressWebResponse = createdAddressPage.getWebResponse();
        assertEquals(createAddressWebResponse.getStatusCode(), 200);
        assertTrue(createAddressWebResponse.getContentLength() > 100);

        Address createdAddress = null;

        if (createAddressWebResponse.getContentType().equals("application/json")) {
            String json = createAddressWebResponse.getContentAsString();
            createdAddress = gson.fromJson(json, Address.class);

            assertNotNull(createdAddress);
        } else {
            fail("Should have been JSON.");
        }

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

        assertTrue(list.contains(createdAddress));
        assertNotEquals(address, createdAddress);

        Integer createdAddressId = createdAddress.getId();
        address.setId(createdAddressId);

        assertNotNull(createdAddressId);
        assertEquals(address, createdAddress);
    }

    /**
     * Test of searchByLastName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByLastName() throws IOException {
        System.out.println("searchByLastName");
        String lastName = UUID.randomUUID().toString();

        Address address = addressGenerator();
        address.setLastName(lastName);

        // Create a Address Using the POST endpoint
        HttpUrl createUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();

        WebClient createAddressWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String addressJson = gson.toJson(address);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(addressJson);
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createdAddressPage = createAddressWebClient.getPage(createRequest);

        WebResponse createAddressWebResponse = createdAddressPage.getWebResponse();
        assertEquals(createAddressWebResponse.getStatusCode(), 200);
        assertTrue(createAddressWebResponse.getContentLength() > 100);

        Address createdAddress = null;

        if (createAddressWebResponse.getContentType().equals("application/json")) {
            String json = createAddressWebResponse.getContentAsString();
            createdAddress = gson.fromJson(json, Address.class);

            assertNotNull(createdAddress);
        } else {
            fail("Should have been JSON.");
        }

        // Search for created Address.
        WebClient searchWebClient = new WebClient();

        List<NameValuePair> paramsList = new ArrayList();

        paramsList.add(new NameValuePair("searchText", lastName));
        paramsList.add(new NameValuePair("searchBy", "searchByLastName"));

        HttpUrl searchUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("search")
                .build();

        WebRequest searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByLastNameRequest.setRequestParameters(paramsList);
        searchByLastNameRequest.setAdditionalHeader("Accept", "application/json");

        Page lastNameSearchPage = searchWebClient.getPage(searchByLastNameRequest);

        assertEquals(lastNameSearchPage.getWebResponse().getStatusCode(), 200);

        String lastNameSearchJson = lastNameSearchPage.getWebResponse().getContentAsString();

        Address[] returnedAddressList = gson.fromJson(lastNameSearchJson, Address[].class);

        assertEquals(returnedAddressList.length, 1);

        List<Address> result = Arrays.asList(returnedAddressList);

        assertNotNull(result);
        assertTrue(result.contains(createdAddress));
        assertEquals(result.size(), 1);

        List<Address> resultb = searchForAddressByLastNameUsingXForm(lastName, gson, searchUrl.url(), "searchByLastName");

        assertNotNull(resultb);
        assertTrue(resultb.contains(createdAddress));
        assertEquals(resultb.size(), 1);

        List<Address> resultc = searchForAddressByUsingJson(lastName, gson, AddressSearchByOptionEnum.LAST_NAME, searchUrl.url());

        assertNotNull(resultc);
        assertTrue(resultc.contains(createdAddress));
        assertEquals(resultc.size(), 1);

        result = searchForAddressByUsingJson(lastName.toLowerCase(), gson, AddressSearchByOptionEnum.LAST_NAME, searchUrl.url());
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByLastNameUsingXForm(lastName.toLowerCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByUsingJson(lastName.toUpperCase(), gson, AddressSearchByOptionEnum.LAST_NAME, searchUrl.url());
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByLastNameUsingXForm(lastName.toUpperCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByUsingJson(lastName.substring(5), gson, AddressSearchByOptionEnum.LAST_NAME, searchUrl.url());
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByLastNameUsingXForm(lastName.substring(5), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByUsingJson(lastName.substring(5, 20), gson, AddressSearchByOptionEnum.LAST_NAME, searchUrl.url());
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByLastNameUsingXForm(lastName.substring(5, 20), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByUsingJson(lastName.substring(5, 20).toLowerCase(), gson, AddressSearchByOptionEnum.LAST_NAME, searchUrl.url());
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByLastNameUsingXForm(lastName.substring(5, 20).toLowerCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByUsingJson(lastName.substring(5, 20).toUpperCase(), gson, AddressSearchByOptionEnum.LAST_NAME, searchUrl.url());
        assertTrue(result.contains(createdAddress));

        result = searchForAddressByLastNameUsingXForm(lastName.substring(5, 20).toUpperCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdAddress));

        searchWebClient = new WebClient();

        searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByLastNameRequest.setRequestParameters(paramsList);

        HtmlPage lastNameSearchHtmlPage = searchWebClient.getPage(searchByLastNameRequest);

        assertEquals(lastNameSearchHtmlPage.getWebResponse().getStatusCode(), 200);

        String title = lastNameSearchHtmlPage.getTitleText();
        assertEquals(title, "Address Book");
        DomElement addressTable = lastNameSearchHtmlPage.getElementById("address-table");

        DomNodeList<HtmlElement> tableRows = addressTable.getElementsByTagName("tr");

        assertTrue(tableRows.size() > 1);

        java.util.Iterator<HtmlElement> tableRowIterator = tableRows.iterator();

        Integer createdAddressRowNumber = null;
        while (tableRowIterator.hasNext()) {
            HtmlElement htmlElement = tableRowIterator.next();
            String htmlText = htmlElement.asText();
            if (htmlText.contains(Integer.toString(createdAddress.getId()))) {
                createdAddressRowNumber = tableRows.indexOf(htmlElement);
                break;
            }
        }

        assertNotNull(createdAddressRowNumber);

        HtmlElement specificRow = tableRows.get(createdAddressRowNumber);

        String xml = specificRow.asXml();

        assertTrue(xml.contains(createdAddress.getFirstName()));
        assertTrue(xml.contains(createdAddress.getLastName()));
        assertTrue(xml.contains(Integer.toString(createdAddress.getId())));
        assertTrue(xml.contains("Edit"));
        assertTrue(xml.contains("Delete"));

    }

    private List<Address> searchForAddressByLastNameUsingXForm(String lastName, Gson gson, URL searchUrl, String searchBy) throws JsonSyntaxException, IOException, FailingHttpStatusCodeException, RuntimeException {
        WebClient searchWebClient = new WebClient();
        List<NameValuePair> paramsList = new ArrayList();
        paramsList.add(new NameValuePair("searchText", lastName));
        paramsList.add(new NameValuePair("searchBy", searchBy));

        WebRequest searchByLastNameRequest = new WebRequest(searchUrl, HttpMethod.POST);
        searchByLastNameRequest.setRequestParameters(paramsList);
        searchByLastNameRequest.setAdditionalHeader("Accept", "application/json");
        Page lastNameSearchPage = searchWebClient.getPage(searchByLastNameRequest);
        assertEquals(lastNameSearchPage.getWebResponse().getStatusCode(), 200);
        String lastNameSearchJson = lastNameSearchPage.getWebResponse().getContentAsString();
        Address[] returnedAddressList = gson.fromJson(lastNameSearchJson, Address[].class);
        assertEquals(returnedAddressList.length, 1);
        List<Address> result = Arrays.asList(returnedAddressList);
        return result;
    }

    private List<Address> searchForAddressByUsingJson(String lastName, Gson gson, AddressSearchByOptionEnum searchOptionEnum, URL searchUrl) throws JsonSyntaxException, IOException, FailingHttpStatusCodeException, RuntimeException {
        WebClient searchWebClient = new WebClient();

        AddressSearchRequest searchRequest = new AddressSearchRequest(lastName, searchOptionEnum);

        String searchRequestJson = gson.toJson(searchRequest);

        WebRequest searchByLastNameRequest = new WebRequest(searchUrl, HttpMethod.POST);
        searchByLastNameRequest.setRequestBody(searchRequestJson);
        searchByLastNameRequest.setAdditionalHeader("Content-type", "application/json");

        Page lastNameSearchPage = searchWebClient.getPage(searchByLastNameRequest);

        String lastNameSearchJson = lastNameSearchPage.getWebResponse().getContentAsString();
        Address[] returnedAddressList = gson.fromJson(lastNameSearchJson, Address[].class);

        List<Address> result = Arrays.asList(returnedAddressList);
        return result;
    }

    /**
     * Test of searchByLastName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByEverything() throws IOException {
        System.out.println("searchByEverything");

        AddressSearchByOptionEnum[] searchOptions = AddressSearchByOptionEnum.values();

        assertTrue(searchOptions.length > 5);

        Integer databaseSizeBeforeTest = getDatabaseSize();

        for (int i = 0; searchOptions.length > i; i++) {

            String searchingBy = searchOptions[i].value();
            String randomString = UUID.randomUUID().toString();

            Address address = addressGenerator();

            switch (searchingBy) {
                case "searchByLastName":
                    address.setLastName(randomString);
                    break;
                case "searchByFirstName":
                    address.setFirstName(randomString);
                    break;
                case "searchByCity":
                    address.setCity(randomString);
                    break;
                case "searchByState":
                    address.setState(randomString);
                    break;
                case "searchByZip":
                    address.setZip(randomString);
                    break;
                case "searchByCompany":
                    address.setCompany(randomString);
                    break;
                case "searchByStreet":
                    address.setStreetName(randomString);
                    break;
                case "searchByStreetNumber":
                    address.setStreetNumber(randomString);
                    break;
                case "searchByStreetName":
                    address.setStreetName(randomString);
                    break;
                case "searchByName":
                    address.setFirstName(randomString);
                    break;
                case "searchByNameOrCompany":
                    address.setCompany(randomString);
                    break;
                case "searchByAll":
                    address.setStreetName(randomString);
                    break;
                default:
                    fail("This should never happen.");
            }

            // Create a Address Using the POST endpoint
            HttpUrl createUrl = HttpUrl.get(uriToTest).newBuilder()
                    .addPathSegment("address")
                    .addPathSegment("")
                    .build();

            WebClient createAddressWebClient = new WebClient();

            Gson gson = new GsonBuilder().create();
            String addressJson = gson.toJson(address);

            WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
            createRequest.setRequestBody(addressJson);
            createRequest.setAdditionalHeader("Accept", "application/json");
            createRequest.setAdditionalHeader("Content-type", "application/json");

            Page createdAddressPage = createAddressWebClient.getPage(createRequest);

            WebResponse createAddressWebResponse = createdAddressPage.getWebResponse();
            assertEquals(createAddressWebResponse.getStatusCode(), 200);
            assertTrue(createAddressWebResponse.getContentLength() > 100);

            Address createdAddress = null;

            if (createAddressWebResponse.getContentType().equals("application/json")) {
                String json = createAddressWebResponse.getContentAsString();
                createdAddress = gson.fromJson(json, Address.class);

                assertNotNull(createdAddress);
            } else {
                fail("Should have been JSON.");
            }

            // Search for created Address.
            WebClient searchWebClient = new WebClient();

            List<NameValuePair> paramsList = new ArrayList();

            paramsList.add(new NameValuePair("searchText", randomString));
            paramsList.add(new NameValuePair("searchBy", searchingBy));

            HttpUrl searchUrl = HttpUrl.get(uriToTest).newBuilder()
                    .addPathSegment("address")
                    .addPathSegment("search")
                    .build();

            WebRequest searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
            searchByLastNameRequest.setRequestParameters(paramsList);
            searchByLastNameRequest.setAdditionalHeader("Accept", "application/json");

            Page lastNameSearchPage = searchWebClient.getPage(searchByLastNameRequest);

            assertEquals(lastNameSearchPage.getWebResponse().getStatusCode(), 200);

            String lastNameSearchJson = lastNameSearchPage.getWebResponse().getContentAsString();
            assertNotEquals("Failed while performing " + searchingBy, lastNameSearchJson, "");
            
            Address[] returnedAddressList = gson.fromJson(lastNameSearchJson, Address[].class);

            assertEquals(searchingBy + " gave: " + lastNameSearchJson, returnedAddressList.length, 1);

            List<Address> result = Arrays.asList(returnedAddressList);

            assertNotNull(result);
            assertTrue(result.contains(createdAddress));
            assertEquals(result.size(), 1);

            List<Address> resultb = searchForAddressByLastNameUsingXForm(randomString, gson, searchUrl.url(), searchingBy);

            assertNotNull(resultb);
            assertTrue(resultb.contains(createdAddress));
            assertEquals(resultb.size(), 1);

            List<Address> resultc = searchForAddressByUsingJson(randomString, gson, searchOptions[i], searchUrl.url());

            assertNotNull(resultc);
            assertTrue(resultc.contains(createdAddress));
            assertEquals(resultc.size(), 1);

            result = searchForAddressByUsingJson(randomString.toLowerCase(), gson, searchOptions[i], searchUrl.url());
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByLastNameUsingXForm(randomString.toLowerCase(), gson, searchUrl.url(), searchingBy);
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByUsingJson(randomString.toUpperCase(), gson, searchOptions[i], searchUrl.url());
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByLastNameUsingXForm(randomString.toUpperCase(), gson, searchUrl.url(), searchingBy);
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByUsingJson(randomString.substring(5), gson, searchOptions[i], searchUrl.url());
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByLastNameUsingXForm(randomString.substring(5), gson, searchUrl.url(), searchingBy);
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByUsingJson(randomString.substring(5, 20), gson, searchOptions[i], searchUrl.url());
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByLastNameUsingXForm(randomString.substring(5, 20), gson, searchUrl.url(), searchingBy);
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByUsingJson(randomString.substring(5, 20).toLowerCase(), gson, searchOptions[i], searchUrl.url());
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByLastNameUsingXForm(randomString.substring(5, 20).toLowerCase(), gson, searchUrl.url(), searchingBy);
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByUsingJson(randomString.substring(5, 20).toUpperCase(), gson, searchOptions[i], searchUrl.url());
            assertTrue(result.contains(createdAddress));

            result = searchForAddressByLastNameUsingXForm(randomString.substring(5, 20).toUpperCase(), gson, searchUrl.url(), searchingBy);
            assertTrue(result.contains(createdAddress));

            searchWebClient = new WebClient();

            searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
            searchByLastNameRequest.setRequestParameters(paramsList);

            HtmlPage randomStringSearchHtmlPage = searchWebClient.getPage(searchByLastNameRequest);

            assertEquals(randomStringSearchHtmlPage.getWebResponse().getStatusCode(), 200);

            String title = randomStringSearchHtmlPage.getTitleText();
            assertEquals(title, "Address Book");
            DomElement addressTable = randomStringSearchHtmlPage.getElementById("address-table");

            DomNodeList<HtmlElement> tableRows = addressTable.getElementsByTagName("tr");

            assertTrue(tableRows.size() > 1);

            java.util.Iterator<HtmlElement> tableRowIterator = tableRows.iterator();

            Integer createdAddressRowNumber = null;
            while (tableRowIterator.hasNext()) {
                HtmlElement htmlElement = tableRowIterator.next();
                String htmlText = htmlElement.asText();
                if (htmlText.contains(Integer.toString(createdAddress.getId()))) {
                    createdAddressRowNumber = tableRows.indexOf(htmlElement);
                    break;
                }
            }

            assertNotNull(createdAddressRowNumber);

            HtmlElement specificRow = tableRows.get(createdAddressRowNumber);

            String xml = specificRow.asXml();

            assertTrue(xml.contains(createdAddress.getFirstName()));
            assertTrue(xml.contains(createdAddress.getLastName()));
            assertTrue(xml.contains(Integer.toString(createdAddress.getId())));
            assertTrue(xml.contains("Edit"));
            assertTrue(xml.contains("Delete"));
        }

        Integer databaseSizeAfterTest = getDatabaseSize();

        assertEquals(databaseSizeAfterTest.intValue(), databaseSizeBeforeTest + searchOptions.length);
    }

    private Integer getDatabaseSize() throws JsonSyntaxException, IOException, FailingHttpStatusCodeException {
        // Get Database size.        
        HttpUrl sizeUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("size")
                .build();
        WebClient sizeWebClient = new WebClient();
        sizeWebClient.addRequestHeader("Accept", "application/json");
        Gson gson = new GsonBuilder().create();
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
        return databaseSize;
    }

    @Test
    public void testGetWithString() throws IOException {
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

            Address createdAddress = createAddressUsingJson(address);
            int resultId = createdAddress.getId();

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            Address result = createdAddress;

            Address returnedDeleteAddress = deleteAddress(resultId);
            assertNotNull(returnedDeleteAddress);
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

            address = createAddressUsingJson(address);
            int resultId = address.getId();

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            int minimumStringLength = 10;
            int processLength = searchString.length() - minimumStringLength;
            int startingPostition = random.nextInt(processLength - minimumStringLength);
            int endingPostition = random.nextInt(processLength - startingPostition) + startingPostition + minimumStringLength;

            searchString = searchString.substring(startingPostition, endingPostition);
            searchString = caseRandomizer(random, searchString);

            // Search for Address Using Search GET Endpoint
            HttpUrl getUrl = HttpUrl.get(uriToTest).newBuilder()
                    .addPathSegment("address")
                    .addPathSegment(searchString)
                    .addPathSegment("search")
                    .build();

            WebClient showAddressWebClient = new WebClient();
            showAddressWebClient.addRequestHeader("Accept", "application/json");

            Page singleAddressPage = showAddressWebClient.getPage(getUrl.url());
            WebResponse jsonSingleAddressResponse = singleAddressPage.getWebResponse();
            assertEquals(jsonSingleAddressResponse.getStatusCode(), 200);
            assertTrue("Content Length: " + jsonSingleAddressResponse.getContentLength(), jsonSingleAddressResponse.getContentLength() > 50);

            Address specificAddress = null;

            if (jsonSingleAddressResponse.getContentType().equals("application/json")) {
                String json = jsonSingleAddressResponse.getContentAsString();
                Gson gson = new GsonBuilder().create();
                specificAddress = gson.fromJson(json, Address.class);

                Assert.assertNotNull(specificAddress);
            } else {
                fail("Should have been JSON.");
            }

            assertEquals(specificAddress, address);

            Address returnedDeleteAddress = deleteAddress(resultId);
            assertNotNull(returnedDeleteAddress);
        }
    }

    @Test
    public void testGetSuggestionsForAutoComplete() throws IOException {
        System.out.println("AutoComplete Suggestions");
        final Random random = new Random();

        List<Address> addressesAdded = new ArrayList();

        for (int pass = 0; pass < 25; pass++) {

            Address address = addressGenerator();

            Address createdAddress = createAddressUsingJson(address);

            addressesAdded.add(createdAddress);
        }

        for (int pass = 0; pass < 150; pass++) {

            Address address = addressGenerator();
            address = createAddressUsingJson(address);
            int resultId = address.getId();

            addressesAdded.add(address);

            int size = addressesAdded.size();
            int randomInt = random.nextInt(size);

            Address randomAddress = addressesAdded.get(randomInt);

            String company = randomAddress.getCompany();
            String firstName = randomAddress.getFirstName();
            String lastName = randomAddress.getLastName();

            String[] randomStrings = {company, firstName, lastName};

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            int minimumStringLength = 10;
            int processLength = searchString.length() - minimumStringLength;
            int startingPostition = random.nextInt(processLength - minimumStringLength);
            int endingPostition = random.nextInt(processLength - startingPostition) + startingPostition + minimumStringLength;

            String modifiedSearchString = searchString.substring(startingPostition, endingPostition);
            modifiedSearchString = caseRandomizer(random, modifiedSearchString);

            // Search for Address Using Search GET Endpoint
            HttpUrl getUrl = HttpUrl.get(uriToTest).newBuilder()
                    .addPathSegment("address")
                    .addPathSegment(modifiedSearchString)
                    .addPathSegment("name_completion")
                    .build();

            WebClient showAddressWebClient = new WebClient();
            showAddressWebClient.addRequestHeader("Accept", "application/json");

            Page singleAddressPage = showAddressWebClient.getPage(getUrl.url());
            WebResponse jsonSingleAddressResponse = singleAddressPage.getWebResponse();
            assertEquals(jsonSingleAddressResponse.getStatusCode(), 200);
            assertTrue("Content Length: " + jsonSingleAddressResponse.getContentLength(), jsonSingleAddressResponse.getContentLength() > 35);

            String[] returnedSuggestions = null;

            if (jsonSingleAddressResponse.getContentType().equals("application/json")) {
                String json = jsonSingleAddressResponse.getContentAsString();
                Gson gson = new GsonBuilder().create();

                System.out.println("Json: " + json);
                System.out.println("Search: " + searchString);

                returnedSuggestions = gson.fromJson(json, String[].class);

                Assert.assertNotNull(returnedSuggestions);
            } else {
                fail("Should have been JSON.");
            }

            assertTrue(Arrays.asList(returnedSuggestions).contains(searchString));

            assertTrue(returnedSuggestions.length < 31);

        }

        for (Address address : addressesAdded) {
            Address returnedDeleteAddress = deleteAddress(address.getId());
            assertNotNull(returnedDeleteAddress);
        }
    }

    @Test
    public void testGetSuggestionsForAutoCompleteWithQuery() throws IOException {
        System.out.println("AutoComplete Suggestions 2");
        final Random random = new Random();

        List<Address> addressesAdded = new ArrayList();

        for (int pass = 0; pass < 25; pass++) {

            Address address = addressGenerator();

            Address createdAddress = createAddressUsingJson(address);

            addressesAdded.add(createdAddress);
        }

        for (int pass = 0; pass < 150; pass++) {

            Address address = addressGenerator();
            address = createAddressUsingJson(address);
            int resultId = address.getId();

            addressesAdded.add(address);

            int size = addressesAdded.size();
            int randomInt = random.nextInt(size);

            Address randomAddress = addressesAdded.get(randomInt);

            String company = randomAddress.getCompany();
            String firstName = randomAddress.getFirstName();
            String lastName = randomAddress.getLastName();

            String[] randomStrings = {company, firstName, lastName};

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            int minimumStringLength = 10;
            int processLength = searchString.length() - minimumStringLength;
            int startingPostition = random.nextInt(processLength - minimumStringLength);
            int endingPostition = random.nextInt(processLength - startingPostition) + startingPostition + minimumStringLength;

            String modifiedSearchString = searchString.substring(startingPostition, endingPostition);
            modifiedSearchString = caseRandomizer(random, modifiedSearchString);

            // Search for Address Using Search GET Endpoint
            HttpUrl getUrl = null;
            if (random.nextBoolean()) {
                getUrl = HttpUrl.get(uriToTest).newBuilder()
                        .addPathSegment("address")
                        .addPathSegment("name_completion")
                        .addQueryParameter("term", modifiedSearchString)
                        .build();
            } else {
                getUrl = HttpUrl.get(uriToTest).newBuilder()
                        .addPathSegment("address")
                        .addPathSegment("name_completion")
                        .addQueryParameter("query", modifiedSearchString)
                        .build();
            }

            WebClient showAddressWebClient = new WebClient();
            showAddressWebClient.addRequestHeader("Accept", "application/json");

            Page singleAddressPage = showAddressWebClient.getPage(getUrl.url());
            WebResponse jsonSingleAddressResponse = singleAddressPage.getWebResponse();
            assertEquals(jsonSingleAddressResponse.getStatusCode(), 200);
            assertTrue("Content Length: " + jsonSingleAddressResponse.getContentLength(), jsonSingleAddressResponse.getContentLength() > 35);

            String[] returnedSuggestions = null;

            if (jsonSingleAddressResponse.getContentType().equals("application/json")) {
                String json = jsonSingleAddressResponse.getContentAsString();
                Gson gson = new GsonBuilder().create();

                System.out.println("Json: " + json);
                System.out.println("Search: " + searchString);

                returnedSuggestions = gson.fromJson(json, String[].class);

                Assert.assertNotNull(returnedSuggestions);
            } else {
                fail("Should have been JSON.");
            }

            assertTrue(Arrays.asList(returnedSuggestions).contains(searchString));

            assertTrue(returnedSuggestions.length < 31);

        }

        for (Address address : addressesAdded) {
            Address returnedDeleteAddress = deleteAddress(address.getId());
            assertNotNull(returnedDeleteAddress);
        }
    }

    private Address deleteAddress(int resultId) throws FailingHttpStatusCodeException, JsonSyntaxException, IOException {
        // Delete the Created Address
        String addressIdString = Integer.toString(resultId);
        HttpUrl deleteUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(addressIdString)
                .build();
        Gson gson = new GsonBuilder().create();
        WebClient deleteAddressWebClient = new WebClient();
        WebRequest deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);
        Page deletePage = deleteAddressWebClient.getPage(deleteRequest);
        String returnedDeleteAddressJson = deletePage.getWebResponse().getContentAsString();
        Address returnedDeleteAddress = gson.fromJson(returnedDeleteAddressJson, Address.class);
        return returnedDeleteAddress;
    }

    private Address createAddressUsingJson(Address address) throws IOException, FailingHttpStatusCodeException, JsonSyntaxException, RuntimeException {
        // Create Address
        HttpUrl createUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment("")
                .build();
        WebClient createAddressWebClient = new WebClient();
        Gson gson = new GsonBuilder().create();
        String addressJson = gson.toJson(address);
        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(addressJson);
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");
        Page createdAddressPage = createAddressWebClient.getPage(createRequest);
        WebResponse createAddressWebResponse = createdAddressPage.getWebResponse();
        assertEquals(createAddressWebResponse.getStatusCode(), 200);
        assertTrue(createAddressWebResponse.getContentLength() > 100);
        Address createdAddress = null;
        if (createAddressWebResponse.getContentType().equals("application/json")) {
            String json = createAddressWebResponse.getContentAsString();
            createdAddress = gson.fromJson(json, Address.class);

            assertNotNull(createdAddress);
        } else {
            fail("Should have been JSON.");
        }
        return createdAddress;
    }

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
