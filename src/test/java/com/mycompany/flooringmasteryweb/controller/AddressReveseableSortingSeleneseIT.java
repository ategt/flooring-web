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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mycompany.flooringmasteryweb.dto.Address;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
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
        WebClient webClient = new WebClient();

        URL url = getAddressUrlBuilder()
                .addPathSegment("")
                .build()
                .url();

        WebRequest webRequest = new WebRequest(url, HttpMethod.GET);

        HtmlPage htmlPage = webClient.getPage(webRequest);

        HtmlAnchor idSortingLink = htmlPage.getAnchorByText("ID");

        assertNotNull(idSortingLink);

        Page idSortedPage = idSortingLink.click();

        assertTrue(idSortedPage.isHtmlPage());

        assertTrue(idSortedPage.getUrl().getQuery().contains("sort_by=id"));

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
    }

    private static Comparator<Integer> sortByIdAscending() {
        return (Integer o1, Integer o2) -> o1.compareTo(o2);
    }

    @Test
    public void sortByIdReversesInHtmlWithClickTest() throws IOException {
        WebClient webClient = new WebClient();

        URL url = getAddressUrlBuilder()
                .addPathSegment("")
                .build()
                .url();

        WebRequest webRequest = new WebRequest(url, HttpMethod.GET);

        HtmlPage htmlPage = webClient.getPage(webRequest);

        HtmlAnchor idSortingLink = htmlPage.getAnchorByText("ID");

        assertNotNull(idSortingLink);

        Page idSortedPage = idSortingLink.click();

        assertTrue(idSortedPage.isHtmlPage());

        assertTrue(idSortedPage.getUrl().getQuery().contains("sort_by=id"));

        htmlPage = (HtmlPage) idSortedPage;

        idSortingLink = htmlPage.getAnchorByText("ID");

        assertNotNull(idSortingLink);

        idSortedPage = idSortingLink.click();

        assertTrue(idSortedPage.isHtmlPage());

        assertTrue(idSortedPage.getUrl().getQuery().contains("sort_by=id"));

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
