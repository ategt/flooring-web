/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mycompany.flooringmasteryweb.dto.Address;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import okhttp3.HttpUrl;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ATeg
 */
public class AddressSearchSeleneseIT {

    ApplicationContext ctx;
    URI uriToTest;

    Integer addressCountFromIndex = null;

    public AddressSearchSeleneseIT() {
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
    public void pagingDoesNotAppearOnSingleResultSearchTest() throws MalformedURLException, IOException {

        Address address = addressGenerator();

        address = createAddressUsingJson(address);

        WebClient webClient = new WebClient();

        URL url = getAddressUrlBuilder()
                .addPathSegment("search")
                .build()
                .url();

        WebRequest webRequest = new WebRequest(url, HttpMethod.POST);

        webRequest.setRequestBody("searchBy=searchByCompany&searchText=" + address.getCompany());

        HtmlPage htmlPage = webClient.getPage(webRequest);

        try {
            HtmlAnchor nextPageLink = htmlPage.getAnchorByText("Next Page >");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        try {
            HtmlAnchor lastPageLink = htmlPage.getAnchorByText("Last Page");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        try {
            HtmlAnchor prevPageLink = htmlPage.getAnchorByText("< Prev Page");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        try {
            HtmlAnchor firstPageLink = htmlPage.getAnchorByText("First Page");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        String title = htmlPage.getTitleText();
        assertEquals(title, "Address Book");
    }

    @Test
    public void pagingDoesAppearOnManyResultSearchTest() throws MalformedURLException, IOException {

        Address address = addressGenerator();

        address = createAddressUsingJson(address);

        WebClient webClient = new WebClient();

        URL url = getAddressUrlBuilder()
                .addPathSegment("search")
                .build()
                .url();

        WebRequest webRequest = new WebRequest(url, HttpMethod.POST);

        webRequest.setRequestBody("searchBy=searchByAll&searchText=a");

        HtmlPage htmlPage = webClient.getPage(webRequest);

        HtmlAnchor nextPageLink = htmlPage.getAnchorByText("Next Page >");
        String nextHref = nextPageLink.getHrefAttribute();

        assertTrue(nextHref.contains("page=1"));

        HtmlAnchor lastPageLink = htmlPage.getAnchorByText("Last Page");
        String lastHref = lastPageLink.getHrefAttribute();

        assertTrue(lastHref.contains("page="));

        try {
            HtmlAnchor prevPageLink = htmlPage.getAnchorByText("< Prev Page");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        try {
            HtmlAnchor firstPageLink = htmlPage.getAnchorByText("First Page");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        String title = htmlPage.getTitleText();
        assertEquals(title, "Address Book");

        Page page = nextPageLink.click();
        assertTrue(page.isHtmlPage());

        HtmlPage nextPage = (HtmlPage) page;

        nextPageLink = nextPage.getAnchorByText("Next Page >");
        nextHref = nextPageLink.getHrefAttribute();

        assertTrue(nextHref.contains("page=2"));

        lastPageLink = nextPage.getAnchorByText("Last Page");
        lastHref = lastPageLink.getHrefAttribute();

        assertTrue(lastHref.contains("page="));

        HtmlAnchor prevPageLink = nextPage.getAnchorByText("< Prev Page");
        String prevHref = prevPageLink.getHrefAttribute();

        assertTrue(prevHref.contains("page=0"));

        HtmlAnchor firstPageLink = nextPage.getAnchorByText("First Page");
        String firstHref = firstPageLink.getHrefAttribute();

        assertTrue(firstHref.contains("page=0"));
        
        HtmlElement htmlElement = nextPage.getHtmlElementById("current-page-number");
        assertTrue(htmlElement.asText().equalsIgnoreCase("1"));

        assertEquals(nextPage.getTitleText(), "Address Book");        

        Page page2 = nextPageLink.click();
        assertTrue(page2.isHtmlPage());

        nextPage = (HtmlPage) page2;

        nextPageLink = nextPage.getAnchorByText("Next Page >");
        nextHref = nextPageLink.getHrefAttribute();

        assertTrue(nextHref.contains("page=3"));

        lastPageLink = nextPage.getAnchorByText("Last Page");
        lastHref = lastPageLink.getHrefAttribute();

        assertTrue(lastHref.contains("page="));

        prevPageLink = nextPage.getAnchorByText("< Prev Page");
        prevHref = prevPageLink.getHrefAttribute();

        assertTrue(prevHref.contains("page=1"));

        firstPageLink = nextPage.getAnchorByText("First Page");
        firstHref = firstPageLink.getHrefAttribute();

        assertTrue(firstHref.contains("page=0"));

        htmlElement = nextPage.getHtmlElementById("current-page-number");
        assertTrue(htmlElement.asText().equalsIgnoreCase("2"));

        assertEquals(nextPage.getTitleText(), "Address Book");

        Page lastPage = lastPageLink.click();

        assertTrue(lastPage.isHtmlPage());

        htmlPage = (HtmlPage) lastPage;

        try {
            nextPageLink = htmlPage.getAnchorByText("Next Page >");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        try {
            lastPageLink = htmlPage.getAnchorByText("Last Page");
            fail("This was supposed to throw an error.");
        } catch (ElementNotFoundException ex) {
            // This what supposed to happen.
        }

        prevPageLink = htmlPage.getAnchorByText("< Prev Page");
        prevHref = prevPageLink.getHrefAttribute();

        assertTrue(prevHref.contains("page="));

        firstPageLink = htmlPage.getAnchorByText("First Page");
        firstHref = firstPageLink.getHrefAttribute();

        assertTrue(firstHref.contains("page=0"));

        assertEquals(htmlPage.getTitleText(), "Address Book");
    }

    private HttpUrl.Builder getAddressUrlBuilder() {
        return HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address");
    }

    private Address createAddressUsingJson(Address address) throws IOException, FailingHttpStatusCodeException, JsonSyntaxException, RuntimeException {
        // Create Address
        HttpUrl createUrl = getAddressUrlBuilder()
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
            createdAddress
                    = gson.fromJson(json, Address.class
                    );

            assertNotNull(createdAddress);
        } else {
            fail("Should have been JSON.");
        }
        return createdAddress;
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
}
