/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.integration;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author ATeg
 */
public class AddressReveseableSortingSeleneseIT {

    ApplicationContext ctx;
    URI uriToTest;

    Integer addressCountFromIndex = null;

    public AddressReveseableSortingSeleneseIT() {
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
    public void sortByIdTestInHtml() throws IOException {
        WebClient webClient = new WebClient();

        URL url = getAddressUrlBuilder()
                .addPathSegment("")
                .build()
                .url();

        WebRequest webRequest = new WebRequest(url, HttpMethod.GET);

        HtmlPage htmlPage = webClient.getPage(webRequest);

        HtmlElement htmlElement = htmlPage.getHtmlElementById("address-table");
        DomNodeList<HtmlElement> tableRows = htmlElement.getElementsByTagName("tr");

        assertTrue(tableRows.size() > 5);

        List<Integer> ids = tableRows.stream().map((tableRow) -> {
            try {
                DomElement domElement = tableRow.getFirstElementChild();
                String idString = domElement.asText();
                Integer id = Integer.parseInt(idString);
                return id;
            } catch (NumberFormatException ex) {
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Integer> clonedIds = new ArrayList<>(ids);

        clonedIds.sort(sortByIdAscending());

        for (int i = 0; i < ids.size(); i++) {
            assertEquals(ids.get(i), clonedIds.get(i));
        }

        StringBuilder sb = new StringBuilder();
        for (Integer clonedId : clonedIds) {
            sb.append(Integer.toString(clonedId));
        }

        String sortedIdString = sb.toString();

        sb = new StringBuilder();
        for (Integer id : ids) {
            sb.append(Integer.toString(id));
        }

        String rawIdString = sb.toString();

        assertEquals(sortedIdString, rawIdString);

        clonedIds.sort(sortByIdDesc());

        sortedIdString = clonedIds.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        assertTrue(!sortedIdString.equals(rawIdString));
    }

    private static Comparator<Integer> sortByIdDesc() {
        return (Integer o1, Integer o2) -> o2.compareTo(o1);
    }

    @Test
    public void sortByIdInHtmlWithClickTest() throws IOException {
        System.out.println("Sort By Id In Html With Click Test");

        WebClient webClient = new WebClient();

        URL url = getAddressUrlBuilder()
                .addPathSegment("")
                .build()
                .url();

        WebRequest webRequest = new WebRequest(url, HttpMethod.GET);

        HtmlPage htmlPage = webClient.getPage(webRequest);

        HtmlAnchor lastNameAnchor = htmlPage.getAnchorByText("Last Name");

        Page lastNameSortedPage = lastNameAnchor.click();

        assertTrue(lastNameSortedPage.isHtmlPage());

        HtmlPage lastNameSortedHtmlPage = (HtmlPage) lastNameSortedPage;

        HtmlAnchor idSortingLink = lastNameSortedHtmlPage.getAnchorByText("ID");

        assertNotNull(idSortingLink);

        Page idSortedPage = idSortingLink.click();

        assertTrue(idSortedPage.isHtmlPage());

        String queryString = idSortedPage.getUrl().getQuery();
        assertTrue(queryString.contains("sort_by=" + AddressSortByEnum.SORT_BY_ID));

        Set<Cookie> cookieSet = webClient.getCookies(idSortedPage.getUrl());
        assertTrue(cookieSet.stream().anyMatch(
                cookie -> cookie.getValue().equalsIgnoreCase(AddressSortByEnum.SORT_BY_ID.toString())
        ));

        htmlPage = (HtmlPage) idSortedPage;


        idSortingLink = htmlPage.getAnchorByText("ID");
        assertNotNull(idSortingLink);
        idSortedPage = idSortingLink.click();

        assertTrue(idSortedPage.isHtmlPage());

        queryString = idSortedPage.getUrl().getQuery();
        assertTrue(queryString.contains("sort_by=SORT_BY_ID"));

        cookieSet = webClient.getCookies(idSortedPage.getUrl());
        assertTrue(cookieSet.stream().anyMatch(
                cookie -> cookie.getValue().equalsIgnoreCase("SORT_BY_ID_INVERSE")
        ));

        htmlPage = (HtmlPage) idSortedPage;

        HtmlElement htmlElement = htmlPage.getHtmlElementById("address-table");
        DomNodeList<HtmlElement> tableRows = htmlElement.getElementsByTagName("tr");

        assertTrue(tableRows.size() > 5);

        List<Integer> ids = tableRows.stream().map((tableRow) -> {
            try {
                DomElement domElement = tableRow.getFirstElementChild();
                String idString = domElement.asText();
                Integer id = Integer.parseInt(idString);
                return id;
            } catch (NumberFormatException ex) {
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Integer> clonedIds = new ArrayList<>(ids);

        clonedIds.sort(sortByIdDesc());

        for (int i = 0; i < ids.size(); i++) {
            assertEquals(ids.get(i), clonedIds.get(i));
        }

        String rawIdString = ids.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        String sortedIdString = clonedIds.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        assertEquals(sortedIdString, rawIdString);

        clonedIds.sort(sortByIdAscending());

        sortedIdString = clonedIds.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        assertTrue(!sortedIdString.equals(rawIdString));

        idSortingLink = htmlPage.getAnchorByText("ID");
        assertNotNull(idSortingLink);
        idSortedPage = idSortingLink.click();

        assertTrue(idSortedPage.isHtmlPage());

        queryString = idSortedPage.getUrl().getQuery();
        assertTrue(queryString.contains("sort_by=SORT_BY_ID"));

        cookieSet = webClient.getCookies(idSortedPage.getUrl());
        assertTrue(cookieSet.stream().anyMatch(
                cookie -> cookie.getValue().equalsIgnoreCase("SORT_BY_ID")
        ));

        htmlPage = (HtmlPage) idSortedPage;

        htmlElement = htmlPage.getHtmlElementById("address-table");
        tableRows = htmlElement.getElementsByTagName("tr");

        assertTrue(tableRows.size() > 5);

        ids = tableRows.stream().map((tableRow) -> {
            try {
                DomElement domElement = tableRow.getFirstElementChild();
                String idString = domElement.asText();
                Integer id = Integer.parseInt(idString);
                return id;
            } catch (NumberFormatException ex) {
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        clonedIds = new ArrayList<>(ids);

        clonedIds.sort(sortByIdAscending());

        for (int i = 0; i < ids.size(); i++) {
            assertEquals(ids.get(i), clonedIds.get(i));
        }

        String rawIdString2 = ids.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        String sortedIdString2 = clonedIds.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        assertEquals(sortedIdString2, rawIdString2);

        clonedIds.sort(sortByIdDesc());

        sortedIdString = clonedIds.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        assertTrue(!sortedIdString.equals(rawIdString));
    }

    private static Comparator<Integer> sortByIdAscending() {
        return (Integer o1, Integer o2) -> o1.compareTo(o2);
    }

    @Test
    public void sortByIdReversesInHtmlWithClickTest() throws IOException {
        System.out.println("Sort By ID Reverses In HTML With Each Click Test");

        WebClient webClient = new WebClient();

        URL url = getAddressUrlBuilder()
                .addPathSegment("")
                .build()
                .url();

        WebRequest webRequest = new WebRequest(url, HttpMethod.GET);

        HtmlPage htmlPage = webClient.getPage(webRequest);

        HtmlAnchor lastNameAnchor = htmlPage.getAnchorByText("Last Name");
        htmlPage.cleanUp();
        htmlPage = null;

        Page lastNameSortedPage = lastNameAnchor.click();

        assertTrue(lastNameSortedPage.isHtmlPage());

        HtmlPage lastNameSortedHtmlPage = (HtmlPage) lastNameSortedPage;

        HtmlAnchor homeLink = lastNameSortedHtmlPage.getAnchorByText("Home");
        HtmlPage homePage = homeLink.click();
        HtmlAnchor addressLink = homePage.getAnchorByText("Address Panel");

        assertTrue(addressLink.getHrefAttribute().contains("/address/"));
        assertFalse(addressLink.getHrefAttribute().contains("sort_by"));

        lastNameSortedHtmlPage = addressLink.click();

        HtmlAnchor idSortingLink = lastNameSortedHtmlPage.getAnchorByText("ID");

        assertNotNull(idSortingLink);

        assertNotNull(idSortingLink);

        Page idSortedPage = idSortingLink.click();

        assertTrue(idSortedPage.isHtmlPage());

        String queryString = idSortedPage.getUrl().getQuery();
        assertTrue(queryString.contains("sort_by=" + AddressSortByEnum.SORT_BY_ID.toString()));

        Set<Cookie> cookieSet = webClient.getCookies(idSortedPage.getUrl());
        assertTrue(cookieSet.stream().anyMatch(
                cookie -> cookie.getValue().equalsIgnoreCase(AddressSortByEnum.SORT_BY_ID.toString())
        ));

        htmlPage = (HtmlPage) idSortedPage;

        HtmlElement htmlElement = htmlPage.getHtmlElementById("address-table");
        DomNodeList<HtmlElement> tableRows = htmlElement.getElementsByTagName("tr");

        assertTrue(tableRows.size() > 5);

        List<Integer> ids = tableRows.stream().map((tableRow) -> {
            try {
                DomElement domElement = tableRow.getFirstElementChild();
                String idString = domElement.asText();
                Integer id = Integer.parseInt(idString);
                return id;
            } catch (NumberFormatException ex) {
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Integer> clonedIds = new ArrayList<>(ids);

        clonedIds.sort(sortByIdAscending());

        for (int i = 0; i < ids.size(); i++) {
            assertEquals(ids.get(i), clonedIds.get(i));
        }

        String rawIdString = ids.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        String sortedIdString = clonedIds.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        assertEquals(sortedIdString, rawIdString);

        clonedIds.sort(sortByIdDesc());

        sortedIdString = clonedIds.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append).toString();

        assertTrue(!sortedIdString.equals(rawIdString));
    }

    private HttpUrl.Builder getAddressUrlBuilder() {
        return HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address");
    }
}
