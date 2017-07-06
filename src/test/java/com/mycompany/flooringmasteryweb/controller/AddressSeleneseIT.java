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
import java.util.Set;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
}
