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
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.OrderSortByEnum;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.State;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Node;

/**
 * @author ATeg
 */
public class OrdersControllerIT {

    ApplicationContext ctx;
    URI uriToTest;
    Random random;

    Integer orderCountFromIndex = null;

    public OrdersControllerIT() {
        ctx = new ClassPathXmlApplicationContext("integrationTest-Context.xml");
        random = new Random();
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
        System.out.println("List Test");

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
    }

    @Test
    public void searchListTest() throws MalformedURLException, IOException {
        System.out.println("Search List Test");

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getOrdersUrlBuilder()
                .addPathSegment("search")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
    }

    @Test
    public void listWithZeroTest() throws MalformedURLException, IOException {
        System.out.println("List Test");

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .addQueryParameter("results", Integer.toString(0))
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
    }

    @Test
    public void orderFormContainsPopulatedStateAndProductDropdownsTest() {
        fail("This is not implemented yet.");
    }

    @Test
    public void loadIndexPage() throws IOException {
        System.out.println("Load Index Page");

        int minimumOrders = 200;
        Gson gson = new GsonBuilder().create();

        // Get Size of Database before creation.
        HttpUrl sizeUrl = getOrdersUrlBuilder()
                .addPathSegment("size")
                .build();

        WebClient sizeWebClient = new WebClient();
        sizeWebClient.addRequestHeader("Accept", "application/json");

        Page sizePage = sizeWebClient.getPage(sizeUrl.url());
        WebResponse sizeResponse = sizePage.getWebResponse();
        assertEquals(sizeResponse.getStatusCode(), 200);
        assertTrue(sizeResponse.getContentLength() < 50);

        Integer currentSize = null;

        if (sizeResponse.getContentType().equals("application/json")) {
            String json = sizeResponse.getContentAsString();
            currentSize = gson.fromJson(json, Integer.class);

            Assert.assertNotNull(currentSize);
        } else {
            fail("Should have been JSON.");
        }

        for (int i = currentSize; i < minimumOrders; i++) {
            Order order = orderGenerator();
            Assert.assertNotNull(order);
            Assert.assertNull(order.getId());

            HttpUrl createUrl = getOrdersUrlBuilder()
                    .addPathSegment("")
                    .build();

            WebClient createOrderWebClient = new WebClient();

            //Gson gson = new GsonBuilder().create();
            String orderJson = gson.toJson(order);

            WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
            createRequest.setRequestBody(orderJson);
            createRequest.setAdditionalHeader("Content-type", "application/json");

            Page createPage = createOrderWebClient.getPage(createRequest);

        }

        HttpUrl httpUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .addQueryParameter("page", Integer.toString(0))
                .addQueryParameter("results", Integer.toString(Integer.MAX_VALUE))
                .build();

        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());
        WebResponse webResponse = htmlPage.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        if (webResponse.getContentType().equals("application/json")) {
            fail("Should have been HTML.");
        }

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
        DomElement orderTable = htmlPage.getElementById("order-table");

        DomNodeList<HtmlElement> orderRows = orderTable.getElementsByTagName("tr");

        assertTrue(orderRows.size() > minimumOrders);

        if (orderCountFromIndex == null) {
            orderCountFromIndex = orderRows.size();
        } else {
            assertEquals(orderCountFromIndex.intValue(), orderRows.size());
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

        webClient.getPage(firstNameUrl);

        Set<Cookie> firstNameSortedCookies = webClient.getCookies(firstNameUrl);

        String host = httpUrl.url().getHost();

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
    public void loadIndexPageWithPagination() throws IOException {
        HttpUrl httpUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());
        WebResponse webResponse = htmlPage.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        if (webResponse.getContentType().equals("application/json")) {
            fail("Should have been HTML.");
        }

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
        DomElement orderTable = htmlPage.getElementById("order-table");

        DomNodeList<HtmlElement> orderRows = orderTable.getElementsByTagName("tr");

        assertTrue(orderRows.size() < 200);

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

        assertTrue(lastHref.contains("page="));

        HtmlAnchor sortByFirstName = htmlPage.getAnchorByHref("?sort_by=first_name");
        String linkText = sortByFirstName.getTextContent();
        assertEquals(linkText, "First Name");

        Node classNode = sortByFirstName.getAttributes().getNamedItem("class");
        String classValue = classNode.getNodeValue();
        assertEquals(classValue, "mask-link");
    }

    @Test
    public void loadIndexJson() throws IOException {
        HttpUrl httpUrl = getOrdersUrlBuilder()
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
            Order[] orders = gson.fromJson(json, Order[].class);

            assertTrue(orders.length > 20);

            if (orderCountFromIndex == null) {
                orderCountFromIndex = orders.length;
            } else {
                assertEquals(orderCountFromIndex.intValue(), orders.length);
            }

            assertTrue("An Id Should be Greater Than 4.", Arrays.asList(orders).stream().anyMatch(order -> order.getId() > 4));
        } else {
            fail("Should have been JSON.");
        }
    }

    @Test
    public void verifyJsonAndHtmlIndexHaveSameOrders() throws IOException {
        System.out.println("Verify Json And Html Have Same Orders");

        HttpUrl httpUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .addQueryParameter("page", Integer.toString(0))
                .addQueryParameter("results", Integer.toString(Integer.MAX_VALUE))
                .build();

        WebRequest jsonRequest = new WebRequest(httpUrl.url());
        jsonRequest.setAdditionalHeader("Accept", "application/json");

        WebRequest htmlRequest = new WebRequest(httpUrl.url());

        WebClient webClient = new WebClient();
        WebClient jsonClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);

        Page jsonPage = jsonClient.getPage(jsonRequest);
        WebResponse jsonResponse = jsonPage.getWebResponse();
        assertEquals(jsonResponse.getStatusCode(), 200);

        HtmlPage htmlPage = webClient.getPage(htmlRequest);
        WebResponse htmlResponse = htmlPage.getWebResponse();
        assertEquals(htmlResponse.getStatusCode(), 200);

        Order[] orders = null;

        if (jsonResponse.getContentType().equals("application/json")) {
            String json = jsonResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            orders = gson.fromJson(json, Order[].class);

            assertTrue(orders.length > 20);
        } else {
            fail("Should have been JSON.");
        }

        DomElement orderTable = htmlPage.getElementById("order-table");

        DomNodeList<HtmlElement> orderRows = orderTable.getElementsByTagName("tr");

        assertNotNull(orders);
        assertEquals(orderRows.size(), orders.length + 1);

        final String htmlText = htmlPage.asText();

        Arrays.stream(orders)
                .parallel()
                .forEach((order) -> {
                    String orderName = order.getName();
                    if (Objects.nonNull(orderName)) {
                        assertTrue(htmlText.contains(orderName));
                    }

                    int id = order.getId();
                    assertTrue(htmlText.contains(Integer.toString(id)));
                });
    }

    @Test
    public void getTest() throws IOException {
        System.out.println("Get Test");

        HttpUrl httpUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient webClient = new WebClient();
        webClient.addRequestHeader("Accept", "application/json");

        Page page = webClient.getPage(httpUrl.url());
        WebResponse webResponse = page.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        Order[] orders = null;

        if (webResponse.getContentType().equals("application/json")) {
            String json = webResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            orders = gson.fromJson(json, Order[].class);

            assertTrue(orders.length > 20);
        } else {
            fail("Should have been JSON.");
        }

        Random random = new Random();
        assertNotNull(orders);
        int randomOrderPlace = random.nextInt(orders.length);

        Order randomOrder = orders[randomOrderPlace];
        Assert.assertNotNull(randomOrder);

        final int randomOrderId = randomOrder.getId();

        HttpUrl showUrl = getOrdersUrlBuilder()
                .addPathSegment(Integer.toString(randomOrderId))
                .build();

        WebClient showOrderWebClient = new WebClient();
        showOrderWebClient.addRequestHeader("Accept", "application/json");

        Page singleOrderPage = showOrderWebClient.getPage(showUrl.url());
        WebResponse jsonSingleOrderResponse = singleOrderPage.getWebResponse();
        assertEquals(jsonSingleOrderResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + jsonSingleOrderResponse.getContentLength(), jsonSingleOrderResponse.getContentLength() > 50);

        Order specificOrder = null;

        if (jsonSingleOrderResponse.getContentType().equals("application/json")) {
            String json = jsonSingleOrderResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            specificOrder = gson.fromJson(json, Order.class);

            Assert.assertNotNull(specificOrder);

            assertTrue(Arrays.asList(orders).stream().anyMatch(order -> order.getId() == randomOrderId));
        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(specificOrder);
        Assert.assertEquals(specificOrder, randomOrder);
    }

    @Test
    public void createTest() throws IOException {
        for (int i = 0; i < 50; i++) {
            System.out.println("Create Test");

            Order order = orderGenerator();
            Assert.assertNotNull(order);
            Assert.assertNull(order.getId());

            OrderCommand orderCommand = OrderCommand.build(order);

            HttpUrl createUrl = getOrdersUrlBuilder()
                    .addPathSegment("")
                    .build();

            WebClient createOrderWebClient = new WebClient();

            Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
            String orderJson = gson.toJson(orderCommand);

            WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
            createRequest.setRequestBody(orderJson);
            createRequest.setAdditionalHeader("Content-type", "application/json");
            createRequest.setAdditionalHeader("Accept", "application/json");

            Page createPage = createOrderWebClient.getPage(createRequest);

            String returnedOrderJson = createPage.getWebResponse().getContentAsString();

            OrderCommand orderReturned = gson.fromJson(returnedOrderJson, OrderCommand.class);

            Assert.assertNotNull(orderReturned);
            Integer returnedOrderId = orderReturned.getId();

            Assert.assertTrue(returnedOrderId > 0);

            orderCommand.setId(returnedOrderId);

            assertEquals(orderReturned.getArea(), orderCommand.getArea(), 0.0001);

            orderReturned.setArea(orderCommand.getArea());

            Gson gsonVerbose = new GsonBuilder().create();

            assertEquals("First Date: " + orderReturned.getDate() +
                            ", Second Date: " + orderCommand.getDate(),
                    orderReturned.getDate(),
                    orderCommand.getDate());

            Assert.assertEquals("OrderReturned: " + gsonVerbose.toJson(orderReturned) +
                            ", \nOrderCommand: " + gsonVerbose.toJson(orderCommand),
                    orderReturned,
                    orderCommand);

            int orderId = orderReturned.getId();

            HttpUrl showUrl = getOrdersUrlBuilder()
                    .addPathSegment(Integer.toString(orderId))
                    .build();

            WebClient showOrderWebClient = new WebClient();
            showOrderWebClient.addRequestHeader("Accept", "application/json");

            Page singleOrderPage = showOrderWebClient.getPage(showUrl.url());
            WebResponse jsonSingleOrderResponse = singleOrderPage.getWebResponse();
            assertEquals(jsonSingleOrderResponse.getStatusCode(), 200);
            assertTrue(jsonSingleOrderResponse.getContentLength() > 50);

            Order specificOrder = null;

            if (jsonSingleOrderResponse.getContentType().equals("application/json")) {
                String json = jsonSingleOrderResponse.getContentAsString();
                specificOrder = gson.fromJson(json, Order.class);

                Assert.assertNotNull(specificOrder);
            } else {
                fail("Should have been JSON.");
            }

            Assert.assertNotNull(specificOrder);

            OrderCommand specificCommandOrder = OrderCommand.build(specificOrder);

            assertEquals(specificCommandOrder.getArea(), orderReturned.getArea(), 0.0001);

            orderReturned.setArea(specificCommandOrder.getArea());

            Assert.assertEquals("SpecificCommandOrder: " + gsonVerbose.toJson(specificCommandOrder) +
                            ", \nOrderCommand: " + gsonVerbose.toJson(orderReturned),
                    specificCommandOrder,
                    orderReturned);

            Order storedOrder = null;

            HttpUrl showUrl2 = getOrdersUrlBuilder()
                    .addPathSegment(Integer.toString(orderId))
                    .build();

            WebClient showOrderWebClient2 = new WebClient();
            showOrderWebClient2.addRequestHeader("Accept", "application/json");

            Page singleOrderPage2 = showOrderWebClient2.getPage(showUrl2.url());
            WebResponse jsonSingleOrderResponse2 = singleOrderPage2.getWebResponse();
            assertEquals(jsonSingleOrderResponse2.getStatusCode(), 200);
            assertTrue(jsonSingleOrderResponse2.getContentLength() > 50);

            if (jsonSingleOrderResponse2.getContentType().equals("application/json")) {
                String json = jsonSingleOrderResponse2.getContentAsString();
                storedOrder = gson.fromJson(json, Order.class);

                Assert.assertNotNull(storedOrder);
            } else {
                fail("Should have been JSON.");
            }

            assertNotNull(storedOrder);
            Assert.assertEquals(OrderCommand.build(storedOrder), orderReturned);
        }
    }

    /**
     * Test of create method, of class OrderDaoPostgresImpl.
     */
    @Test
    public void testCRUD() throws IOException {
        System.out.println("CRUD test");

        Order order = orderGenerator();

        Gson gson = new GsonBuilder().create();

        // Get Size of Database before creation.
        HttpUrl sizeUrl = getOrdersUrlBuilder()
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

        // Create Order
        HttpUrl createUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createOrderWebClient = new WebClient();

        String orderJson = gson.toJson(order);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(orderJson);

        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createPage = createOrderWebClient.getPage(createRequest);

        String returnedOrderJson = createPage.getWebResponse().getContentAsString();

        Order orderReturned = gson.fromJson(returnedOrderJson, Order.class);

        assertNotNull(orderReturned.getId());
        assertTrue(orderReturned.getId() > 0);

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

        assertNotNull(afterCreation);
        assertEquals(beforeCreation + 1, afterCreation.intValue());

        assertNotNull(orderReturned);
        assertNotNull(orderReturned.getId());

        assertTrue(orderReturned.getId() > 0);

        // Check that created order is in the Database.
        HttpUrl showUrl = getOrdersUrlBuilder()
                .addPathSegment(Integer.toString(orderReturned.getId()))
                .build();

        WebClient showOrderWebClient = new WebClient();
        showOrderWebClient.addRequestHeader("Accept", "application/json");

        Page singleOrderPage = showOrderWebClient.getPage(showUrl.url());
        WebResponse jsonSingleOrderResponse = singleOrderPage.getWebResponse();
        assertEquals(jsonSingleOrderResponse.getStatusCode(), 200);
        assertTrue(jsonSingleOrderResponse.getContentLength() > 50);

        Order specificOrder = null;

        if (jsonSingleOrderResponse.getContentType().equals("application/json")) {
            String json = jsonSingleOrderResponse.getContentAsString();
            specificOrder = gson.fromJson(json, Order.class);

            Assert.assertNotNull(specificOrder);

        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(specificOrder);

        assertEquals(orderReturned, specificOrder);

        Order newOrder = orderGenerator();

        Integer id = orderReturned.getId();
        newOrder.setId(id);

        // Update Order With Service PUT endpoint
        HttpUrl updateUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient updateOrderWebClient = new WebClient();

        String updatedOrderJson = gson.toJson(newOrder);

        WebRequest updateRequest = new WebRequest(updateUrl.url(), HttpMethod.PUT);
        updateRequest.setRequestBody(updatedOrderJson);

        updateRequest.setAdditionalHeader("Accept", "application/json");
        updateRequest.setAdditionalHeader("Content-type", "application/json");

        Page updatePage = updateOrderWebClient.getPage(updateRequest);

        String returnedUpdatedOrderJson = updatePage.getWebResponse().getContentAsString();

        Order returnedUpdatedOrder = gson.fromJson(returnedUpdatedOrderJson, Order.class);

        assertEquals(newOrder, returnedUpdatedOrder);

        // Get Order By Company
        HttpUrl searchUrl = getOrdersUrlBuilder()
                .addPathSegment("search")
                .build();

        // Check search using json object.
        WebClient jsonSearchWebClient = new WebClient();

        String orderSearchRequestJson = "{\"searchBy\":\"searchByEverything\",\"searchText\":\"" + newOrder.getName() + "\"}";

        WebRequest searchByCityRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCityRequest.setRequestBody(orderSearchRequestJson);

        searchByCityRequest.setAdditionalHeader("Accept", "application/json");
        searchByCityRequest.setAdditionalHeader("Content-type", "application/json");

        Page citySearchPage = jsonSearchWebClient.getPage(searchByCityRequest);

        assertEquals(citySearchPage.getWebResponse().getStatusCode(), 200);

        String citySearchOrderJson = citySearchPage.getWebResponse().getContentAsString();

        Order[] returnedCitySearchOrders = gson.fromJson(citySearchOrderJson, Order[].class);

        assertEquals(returnedCitySearchOrders.length, 1);

        Order returnedCitySearchOrder = returnedCitySearchOrders[0];

        assertEquals(returnedUpdatedOrder, returnedCitySearchOrder);
        assertEquals(orderReturned, returnedCitySearchOrder);
        assertNotEquals(specificOrder, returnedCitySearchOrder);

        // Check search using json object built with my purspective api.
        WebClient jsonSearchWebClient2 = new WebClient();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newOrder.getDate());

        String searchText = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);

        OrderSearchRequest orderSearchRequest = new OrderSearchRequest(searchText, OrderSearchByOptionEnum.DATE);

        String orderSearchRequestJson2 = gson.toJson(orderSearchRequest);

        WebRequest searchByCityRequest2 = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCityRequest2.setRequestBody(orderSearchRequestJson2);

        searchByCityRequest2.setAdditionalHeader("Accept", "application/json");
        searchByCityRequest2.setAdditionalHeader("Content-type", "application/json");

        Page citySearchPage2 = jsonSearchWebClient2.getPage(searchByCityRequest2);

        assertEquals(citySearchPage2.getWebResponse().getStatusCode(), 200);

        String citySearchOrderJson2 = citySearchPage2.getWebResponse().getContentAsString();

        Order[] returnedCitySearchOrders2 = gson.fromJson(citySearchOrderJson2, Order[].class);

        assertEquals(returnedCitySearchOrders2.length, 1);

        Order returnedCitySearchOrder2 = returnedCitySearchOrders2[0];

        assertEquals(returnedUpdatedOrder, returnedCitySearchOrder2);
        assertEquals(orderReturned, returnedCitySearchOrder2);
        assertNotEquals(specificOrder, returnedCitySearchOrder2);

        assertEquals(returnedCitySearchOrder, returnedCitySearchOrder2);

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

        assertNotNull(afterUpdate);
        assertEquals(afterUpdate.intValue(), afterCreation.intValue());

        // Delete the Created Order
        String orderIdString = Integer.toString(orderReturned.getId());

        HttpUrl deleteUrl = getOrdersUrlBuilder()
                .addPathSegment(orderIdString)
                .build();

        WebClient deleteOrderWebClient = new WebClient();

        WebRequest deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        Page deletePage = deleteOrderWebClient.getPage(deleteRequest);

        String returnedDeleteOrderJson = deletePage.getWebResponse().getContentAsString();

        Order returnedDeleteOrder = gson.fromJson(returnedDeleteOrderJson, Order.class);

        assertEquals(returnedDeleteOrder, returnedUpdatedOrder);

        // Delete The Created Order A Second Time
        deleteOrderWebClient = new WebClient();

        deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        deletePage = deleteOrderWebClient.getPage(deleteRequest);

        returnedDeleteOrderJson = deletePage.getWebResponse().getContentAsString();
        assertEquals(returnedDeleteOrderJson, "");

        Order returnedDeleteOrder2 = gson.fromJson(returnedDeleteOrderJson, Order.class);
        assertNull(returnedDeleteOrder2);

        assertNotEquals(returnedDeleteOrder, returnedDeleteOrder2);

        // Verify Order Database Size Has Shrunk By One After Deletion
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

        assertNotNull(beforeCreation);
        assertNotNull(afterDeletion);
        assertEquals(beforeCreation.intValue(), afterDeletion.intValue());
        assertEquals(afterCreation - 1, afterDeletion.intValue());

        // Try to Get Deleted Order
        HttpUrl showUrl2 = getOrdersUrlBuilder()
                .addPathSegment(Integer.toString(orderReturned.getId()))
                .build();

        WebClient showOrderWebClient2 = new WebClient();
        showOrderWebClient2.addRequestHeader("Accept", "application/json");

        Page singleOrderPage2 = showOrderWebClient2.getPage(showUrl.url());
        WebResponse jsonSingleOrderResponse2 = singleOrderPage2.getWebResponse();
        assertEquals(jsonSingleOrderResponse2.getStatusCode(), 200);
        assertTrue(jsonSingleOrderResponse2.getContentLength() < 50);

        String contentType = jsonSingleOrderResponse2.getContentType();
        assertEquals(contentType, "");

        String contentString = jsonSingleOrderResponse2.getContentAsString();

        assertEquals(contentString, "");

        // Get Deleted Order By Company
        WebClient searchWebClient3 = new WebClient();
        WebRequest searchByCityRequest3 = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByCityRequest3.setRequestBody(orderSearchRequestJson2);

        searchByCityRequest3.setAdditionalHeader("Accept", "application/json");
        searchByCityRequest3.setAdditionalHeader("Content-type", "application/json");

        Page citySearchPage3 = searchWebClient3.getPage(searchByCityRequest3);

        assertEquals(citySearchPage3.getWebResponse().getStatusCode(), 200);

        String citySearchOrderJson3 = citySearchPage3.getWebResponse().getContentAsString();
        assertEquals(citySearchOrderJson3, "[]");

        Order[] returnedCitySearchOrders3 = gson.fromJson(citySearchOrderJson3, Order[].class);

        assertEquals(returnedCitySearchOrders3.length, 0);

        WebClient showOrderWebClient3 = new WebClient();
        showOrderWebClient3.addRequestHeader("Accept", "application/json");

        Page singleOrderPage3 = showOrderWebClient3.getPage(showUrl.url());
        WebResponse jsonSingleOrderResponse3 = singleOrderPage3.getWebResponse();
        assertEquals(jsonSingleOrderResponse3.getStatusCode(), 200);
        assertTrue(jsonSingleOrderResponse3.getContentLength() < 50);

        String contentType2 = jsonSingleOrderResponse3.getContentType();
        assertEquals(contentType2, "");

        String json = jsonSingleOrderResponse3.getContentAsString();
        assertEquals(json, "");

        Order alsoDeleted2 = gson.fromJson(json, Order.class);

        Assert.assertNull(alsoDeleted2);
    }

    private Order orderBuilder(String name,
                               double area,
                               double costPerFoot,
                               double laborCost,
                               double laborCostPerFoot,
                               double materialCost,
                               double tax,
                               double taxRate,
                               double total,
                               Date date,
                               Product product,
                               State state) {

        Order order = new Order();
        order.setArea(area);
        order.setCostPerSquareFoot(costPerFoot);
        order.setDate(date);
        order.setLaborCost(laborCost);
        order.setLaborCostPerSquareFoot(laborCostPerFoot);
        order.setMaterialCost(materialCost);
        order.setName(name);
        order.setProduct(product);
        order.setState(state);
        order.setTax(tax);
        order.setTaxRate(taxRate);
        order.setTotal(total);

        return order;
    }

    /**
     * Test of list method, of class OrderDaoPostgresImpl.
     */
    @Test
    public void testList() throws IOException {
        System.out.println("list");

        Order order = orderGenerator();

        // Create Generated Order
        HttpUrl createUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createOrderWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String orderJson = gson.toJson(order);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(orderJson);
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createdOrderPage = createOrderWebClient.getPage(createRequest);

        WebResponse createOrderWebResponse = createdOrderPage.getWebResponse();
        assertEquals(createOrderWebResponse.getStatusCode(), 200);
        assertTrue(createOrderWebResponse.getContentLength() > 100);

        Order createdOrder = null;

        if (createOrderWebResponse.getContentType().equals("application/json")) {
            String json = createOrderWebResponse.getContentAsString();
            createdOrder = gson.fromJson(json, Order.class);

            assertNotNull(createdOrder);
        } else {
            fail("Should have been JSON.");
        }

        // Get The List Of Orders
        HttpUrl getListUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .addQueryParameter("results", Integer.toString(Integer.MAX_VALUE))
                .build();

        WebClient getListWebClient = new WebClient();
        getListWebClient.addRequestHeader("Accept", "application/json");

        Page getListPage = getListWebClient.getPage(getListUrl.url());
        WebResponse getListWebResponse = getListPage.getWebResponse();
        assertEquals(getListWebResponse.getStatusCode(), 200);
        assertTrue(getListWebResponse.getContentLength() > 100);

        List<Order> list = null;

        if (getListWebResponse.getContentType().equals("application/json")) {
            String json = getListWebResponse.getContentAsString();
            Order[] orders = gson.fromJson(json, Order[].class);

            assertTrue(orders.length > 20);

            list = Arrays.asList(orders);
        } else {
            fail("Should have been JSON.");
        }

        // Get Database Size
        HttpUrl sizeUrl = getOrdersUrlBuilder()
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

        Assert.assertNotNull(list);
        Assert.assertNotNull(databaseSize);
        assertEquals(list.size(), databaseSize.intValue());

        assertTrue(list.contains(createdOrder));
        assertNotEquals(order, createdOrder);

        assertNotNull(createdOrder);
        Integer createdOrderId = createdOrder.getId();
        order.setId(createdOrderId);

        assertNotNull(createdOrderId);
        assertEquals(order, createdOrder);
    }

    /**
     * Test of searchByLastName method, of class OrderDaoPostgresImpl.
     */
    @Test
    public void testSearchByName() throws IOException {
        System.out.println("searchByLastName");
        String lastName = UUID.randomUUID().toString();

        Order order = orderGenerator();
        order.setName(lastName);

        // Create a Order Using the POST endpoint
        HttpUrl createUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createOrderWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String orderJson = gson.toJson(order);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(orderJson);
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createdOrderPage = createOrderWebClient.getPage(createRequest);

        WebResponse createOrderWebResponse = createdOrderPage.getWebResponse();
        assertEquals(createOrderWebResponse.getStatusCode(), 200);
        assertTrue(createOrderWebResponse.getContentLength() > 100);

        Order createdOrder = null;

        if (createOrderWebResponse.getContentType().equals("application/json")) {
            String json = createOrderWebResponse.getContentAsString();
            createdOrder = gson.fromJson(json, Order.class);

            assertNotNull(createdOrder);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(createdOrder);

        // Search for created Order.
        WebClient searchWebClient = new WebClient();

        List<NameValuePair> paramsList = new ArrayList();

        paramsList.add(new NameValuePair("searchText", lastName));
        paramsList.add(new NameValuePair("searchBy", "searchByLastName"));

        HttpUrl searchUrl = getOrdersUrlBuilder()
                .addPathSegment("search")
                .build();

        WebRequest searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByLastNameRequest.setRequestParameters(paramsList);
        searchByLastNameRequest.setAdditionalHeader("Accept", "application/json");

        Page lastNameSearchPage = searchWebClient.getPage(searchByLastNameRequest);

        assertEquals(lastNameSearchPage.getWebResponse().getStatusCode(), 200);

        String lastNameSearchJson = lastNameSearchPage.getWebResponse().getContentAsString();

        Order[] returnedOrderList = gson.fromJson(lastNameSearchJson, Order[].class);

        assertEquals(returnedOrderList.length, 1);

        List<Order> result = Arrays.asList(returnedOrderList);

        assertNotNull(result);
        assertTrue(result.contains(createdOrder));
        assertEquals(result.size(), 1);

        List<Order> resultb = searchForOrderByLastNameUsingXForm(lastName, gson, searchUrl.url(), "searchByLastName");

        assertNotNull(resultb);
        assertTrue(resultb.contains(createdOrder));
        assertEquals(resultb.size(), 1);

        List<Order> resultc = searchForOrderByUsingJson(lastName, gson, OrderSearchByOptionEnum.NAME, searchUrl.url());

        assertNotNull(resultc);
        assertTrue(resultc.contains(createdOrder));
        assertEquals(resultc.size(), 1);

        result = searchForOrderByUsingJson(lastName.toLowerCase(), gson, OrderSearchByOptionEnum.NAME, searchUrl.url());
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByLastNameUsingXForm(lastName.toLowerCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByUsingJson(lastName.toUpperCase(), gson, OrderSearchByOptionEnum.NAME, searchUrl.url());
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByLastNameUsingXForm(lastName.toUpperCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByUsingJson(lastName.substring(5), gson, OrderSearchByOptionEnum.NAME, searchUrl.url());
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByLastNameUsingXForm(lastName.substring(5), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByUsingJson(lastName.substring(5, 20), gson, OrderSearchByOptionEnum.NAME, searchUrl.url());
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByLastNameUsingXForm(lastName.substring(5, 20), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByUsingJson(lastName.substring(5, 20).toLowerCase(), gson, OrderSearchByOptionEnum.NAME, searchUrl.url());
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByLastNameUsingXForm(lastName.substring(5, 20).toLowerCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByUsingJson(lastName.substring(5, 20).toUpperCase(), gson, OrderSearchByOptionEnum.NAME, searchUrl.url());
        assertTrue(result.contains(createdOrder));

        result = searchForOrderByLastNameUsingXForm(lastName.substring(5, 20).toUpperCase(), gson, searchUrl.url(), "searchByLastName");
        assertTrue(result.contains(createdOrder));

        searchWebClient = new WebClient();

        searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
        searchByLastNameRequest.setRequestParameters(paramsList);

        HtmlPage lastNameSearchHtmlPage = searchWebClient.getPage(searchByLastNameRequest);

        assertEquals(lastNameSearchHtmlPage.getWebResponse().getStatusCode(), 200);

        String title = lastNameSearchHtmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
        DomElement orderTable = lastNameSearchHtmlPage.getElementById("order-table");

        DomNodeList<HtmlElement> tableRows = orderTable.getElementsByTagName("tr");

        assertTrue(tableRows.size() > 1);

        java.util.Iterator<HtmlElement> tableRowIterator = tableRows.iterator();

        Integer createdOrderRowNumber = null;
        while (tableRowIterator.hasNext()) {
            HtmlElement htmlElement = tableRowIterator.next();
            String htmlText = htmlElement.asText();
            if (htmlText.contains(Integer.toString(createdOrder.getId()))) {
                createdOrderRowNumber = tableRows.indexOf(htmlElement);
                break;
            }
        }

        assertNotNull(createdOrderRowNumber);

        HtmlElement specificRow = tableRows.get(createdOrderRowNumber);

        String xml = specificRow.asXml();

        assertTrue(xml.contains(Integer.toString(createdOrder.getId())));
        assertTrue(xml.contains(createdOrder.getName()));
        assertTrue(xml.contains(Integer.toString(createdOrder.getId())));
        assertTrue(xml.contains("Edit"));
        assertTrue(xml.contains("Delete"));

    }

    private List<Order> searchForOrderByLastNameUsingXForm(String lastName, Gson gson, URL searchUrl, String searchBy) throws JsonSyntaxException, IOException, FailingHttpStatusCodeException, RuntimeException {
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
        Order[] returnedOrderList = gson.fromJson(lastNameSearchJson, Order[].class);
        assertEquals(returnedOrderList.length, 1);
        List<Order> result = Arrays.asList(returnedOrderList);
        return result;
    }

    private List<Order> searchForOrderByUsingJson(String lastName, Gson gson, OrderSearchByOptionEnum searchOptionEnum, URL searchUrl) throws JsonSyntaxException, IOException, FailingHttpStatusCodeException, RuntimeException {
        WebClient searchWebClient = new WebClient();

        OrderSearchRequest searchRequest = new OrderSearchRequest(lastName, searchOptionEnum);

        String searchRequestJson = gson.toJson(searchRequest);

        WebRequest searchByLastNameRequest = new WebRequest(searchUrl, HttpMethod.POST);
        searchByLastNameRequest.setRequestBody(searchRequestJson);
        searchByLastNameRequest.setAdditionalHeader("Content-type", "application/json");

        Page lastNameSearchPage = searchWebClient.getPage(searchByLastNameRequest);

        String lastNameSearchJson = lastNameSearchPage.getWebResponse().getContentAsString();
        Order[] returnedOrderList = gson.fromJson(lastNameSearchJson, Order[].class);

        List<Order> result = Arrays.asList(returnedOrderList);
        return result;
    }
//
//    /**
//     * Test of searchByLastName method, of class OrderDaoPostgresImpl.
//     */
//    @Test
//    public void testSearchByEverything() throws IOException {
//        System.out.println("searchByEverything");
//
//        Order randomValidOrder = null;
//
//        for (OrderSearchByOptionEnum searchByEnum : OrderSearchByOptionEnum.values()) {
//            for (OrderSortByEnum sortByEnum : OrderSortByEnum.values()) {
//
//                String searchText = null;
//
//                switch (searchByEnum) {
//                    case DATE:
//                        Date randomValidDate = null;
//                        while (randomValidDate == null) {
//                            randomValidOrder = getRandomOrder();
//                            randomValidDate = randomValidOrder.getDate();
//                        }
//
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTime(randomValidDate);
//
//                        searchText = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
//                        break;
//                    case NAME:
//                        randomValidOrder = null;
//                        while (randomValidOrder == null || randomValidOrder.getName() == null) {
//                            randomValidOrder = getRandomOrder();
//                            searchText = randomValidOrder.getName();
//                        }
//
//                        int subStringLength = random.nextInt(searchText.length()) + 5;
//
//                        System.out.println("String Length: " + searchText.length());
//                        System.out.println("Sub Length: " + subStringLength);
//                        System.out.println("");
//
//                        searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
//                        break;
//                    case ORDER_NUMBER:
//                        randomValidOrder = getRandomOrder();
//                        searchText = Integer.toString(randomValidOrder.getId());
//                        break;
//                    case PRODUCT:
//                        randomValidOrder = null;
//                        while (randomValidOrder == null || randomValidOrder.getProduct() == null || randomValidOrder.getProduct().getProductName() == null) {
//                            randomValidOrder = getRandomOrder();
//                        }
//
//                        searchText = randomValidOrder.getProduct().getProductName();
//
//                        subStringLength = random.nextInt(searchText.length()) + 5;
//
//                        System.out.println("String Length: " + searchText.length());
//                        System.out.println("Sub Length: " + subStringLength);
//                        System.out.println("");
//
//                        searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
//                        break;
//                    case STATE:
//                        randomValidOrder = null;
//                        while (randomValidOrder == null || randomValidOrder.getState() == null) {
//                            randomValidOrder = getRandomOrder();
//                        }
//                        searchText = randomValidOrder.getState().getStateName();
//                        break;
//                    case EVERYTHING:
//                        switch (random.nextInt(5)) {
//                            case 0:
//                                Date randomValidDate2 = null;
//                                while (randomValidDate2 == null) {
//                                    randomValidOrder = getRandomOrder();
//                                    randomValidDate2 = randomValidOrder.getDate();
//                                }
//
//                                Calendar calendar2 = Calendar.getInstance();
//                                calendar2.setTime(randomValidDate2);
//
//                                searchText = (calendar2.get(Calendar.MONTH) + 1) + "/" + calendar2.get(Calendar.DAY_OF_MONTH) + "/" + calendar2.get(Calendar.YEAR);
//                                break;
//                            case 1:
//                                randomValidOrder = null;
//                                while (randomValidOrder == null || randomValidOrder.getName() == null) {
//                                    randomValidOrder = getRandomOrder();
//                                    searchText = randomValidOrder.getName();
//                                }
//
//                                subStringLength = random.nextInt(searchText.length()) + 5;
//
//                                System.out.println("String Length: " + searchText.length());
//                                System.out.println("Sub Length: " + subStringLength);
//                                System.out.println("");
//
//                                searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
//                                break;
//                            case 2:
//                                randomValidOrder = getRandomOrder();
//                                searchText = Integer.toString(randomValidOrder.getId());
//                                break;
//                            case 3:
//                                randomValidOrder = null;
//                                while (randomValidOrder == null || randomValidOrder.getProduct() == null || randomValidOrder.getProduct().getProductName() == null) {
//                                    randomValidOrder = getRandomOrder();
//                                }
//
//                                searchText = randomValidOrder.getProduct().getProductName();
//
//                                subStringLength = random.nextInt(searchText.length()) + 5;
//
//                                System.out.println("String Length: " + searchText.length());
//                                System.out.println("Sub Length: " + subStringLength);
//                                System.out.println("");
//
//                                searchText = searchText.length() - 2 < 5 || searchText.length() - 2 < subStringLength ? searchText.substring(2) : searchText.substring(2, subStringLength);
//                                break;
//                            case 4:
//                            default:
//                                randomValidOrder = null;
//                                while (randomValidOrder == null || randomValidOrder.getState() == null) {
//                                    randomValidOrder = getRandomOrder();
//                                }
//                                searchText = randomValidOrder.getState().getStateName();
//                                break;
//                        }
//                        break;
//                    default:
//                        fail("This should not get here.");
//                }
//
//                OrderSearchRequest searchRequest = new OrderSearchRequest(searchText, searchByEnum);
//                OrderResultSegment resultSegment = new OrderResultSegment(sortByEnum, Integer.MAX_VALUE, 0);
//
//                List<Order> orders = orderDao.search(searchRequest, resultSegment);
//                assertNotNull(orders);
//
//                assertTrue("SearchBy: " + searchByEnum.toString() + ", SortBy: " + sortByEnum.toString() + ", Count: " + orders.size() + ", Search Text: " + searchText + ", ID: " + randomValidOrder.getId(),
//                        orders.size() > 0);
//
//                assertTrue("SearchBy: " + searchByEnum.toString() + ", SortBy: " + sortByEnum.toString() + ", Count: " + orders.size() + ", Search Text: " + searchText + ", ID: " + randomValidOrder.getId(),
//                        orders.contains(randomValidOrder));
//            }
//
//            OrderSearchRequest searchRequest = new OrderSearchRequest("", searchByEnum);
//
//            List<Order> orders = orderDao.search(searchRequest, null);
//            assertNotNull(orders);
//
//            searchRequest = new OrderSearchRequest("", null);
//
//            orders = orderDao.search(searchRequest, null);
//            assertNotNull(orders);
//
//            orders = orderDao.search(null, null);
//            assertNotNull(orders);
//            assertEquals(orders.size(), orderDao.size());
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        OrderSearchByOptionEnum[] searchOptions = OrderSearchByOptionEnum.values();
//
//        assertTrue(searchOptions.length > 5);
//
//        Integer databaseSizeBeforeTest = getDatabaseSize();
//
//        //for (int i = 0; searchOptions.length > i; i++) {
//        for (OrderSearchByOptionEnum searchByEnum : OrderSearchByOptionEnum.values()){
//
//            //String searchingBy = searchOptions[i].value();
//            String randomString = UUID.randomUUID().toString();
//
//            Order order = orderGenerator();
//
//            switch (searchByEnum) {
//                case DATE:
//                    order.setLastName(randomString);
//                    break;
//                case "searchByFirstName":
//                    order.setFirstName(randomString);
//                    break;
//                case "searchByFullName":
//                    order.setFirstName(randomString);
//                    randomString = order.getFullName();
//                    break;
//                case "searchByCity":
//                    order.setCity(randomString);
//                    break;
//                case "searchByState":
//                    order.setState(randomString);
//                    break;
//                case "searchByZip":
//                    order.setZip(randomString);
//                    break;
//                case "searchByCompany":
//                    order.setCompany(randomString);
//                    break;
//                case "searchByStreet":
//                    order.setStreetName(randomString);
//                    break;
//                case "searchByStreetNumber":
//                    order.setStreetNumber(randomString);
//                    break;
//                case "searchByStreetName":
//                    order.setStreetName(randomString);
//                    break;
//                case "searchByName":
//                    order.setFirstName(randomString);
//                    break;
//                case "searchByNameOrCompany":
//                    order.setCompany(randomString);
//                    break;
//                case "searchByAll":
//                    order.setStreetName(randomString);
//                    break;
//                case "searchByAny":
//
//                    switch (new Random().nextInt(8)) {
//                        case 0:
//                            order.setZip(randomString);
//                            break;
//                        case 1:
//                            order.setCity(randomString);
//                            break;
//                        case 2:
//                            order.setCompany(randomString);
//                            break;
//                        case 3:
//                            order.setFirstName(randomString);
//                            break;
//                        case 4:
//                            order.setLastName(randomString);
//                            break;
//                        case 5:
//                            order.setState(randomString);
//                            break;
//                        case 6:
//                            order.setStreetName(randomString);
//                            break;
//                        case 7:
//                            order.setStreetNumber(randomString);
//                            break;
//                        default:
//                            fail("Wrong random number generated.");
//                    }
//                    break;
//                default:
//                    fail("This should never happen.\n" + searchingBy + " is not checked.");
//            }
//
//            // Create a Order Using the POST endpoint
//            HttpUrl createUrl = getOrdersUrlBuilder()
//                    .addPathSegment("")
//                    .build();
//
//            WebClient createOrderWebClient = new WebClient();
//
//            Gson gson = new GsonBuilder().create();
//            String orderJson = gson.toJson(order);
//
//            WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
//            createRequest.setRequestBody(orderJson);
//            createRequest.setAdditionalHeader("Accept", "application/json");
//            createRequest.setAdditionalHeader("Content-type", "application/json");
//
//            Page createdOrderPage = createOrderWebClient.getPage(createRequest);
//
//            WebResponse createOrderWebResponse = createdOrderPage.getWebResponse();
//            assertEquals(createOrderWebResponse.getStatusCode(), 200);
//            assertTrue(createOrderWebResponse.getContentLength() > 100);
//
//            Order createdOrder = null;
//
//            if (createOrderWebResponse.getContentType().equals("application/json")) {
//                String json = createOrderWebResponse.getContentAsString();
//                createdOrder = gson.fromJson(json, Order.class);
//
//                assertNotNull(createdOrder);
//            } else {
//                fail("Should have been JSON.");
//            }
//
//            assertNotNull(createdOrder);
//
//            // Search for created Order.
//            WebClient searchWebClient = new WebClient();
//
//            List<NameValuePair> paramsList = new ArrayList();
//
//            paramsList.add(new NameValuePair("searchText", randomString));
//            paramsList.add(new NameValuePair("searchBy", searchingBy));
//
//            HttpUrl searchUrl = getOrdersUrlBuilder()
//                    .addPathSegment("search")
//                    .build();
//
//            WebRequest searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
//            searchByLastNameRequest.setRequestParameters(paramsList);
//            searchByLastNameRequest.setAdditionalHeader("Accept", "application/json");
//
//            Page lastNameSearchPage = searchWebClient.getPage(searchByLastNameRequest);
//
//            assertEquals(lastNameSearchPage.getWebResponse().getStatusCode(), 200);
//
//            String lastNameSearchJson = lastNameSearchPage.getWebResponse().getContentAsString();
//            assertNotEquals("Failed while performing " + searchingBy, lastNameSearchJson, "");
//
//            Order[] returnedOrderList = gson.fromJson(lastNameSearchJson, Order[].class);
//
//            assertEquals(searchingBy + " gave: " + lastNameSearchJson, returnedOrderList.length, 1);
//
//            List<Order> result = Arrays.asList(returnedOrderList);
//
//            assertNotNull(result);
//            assertTrue(result.contains(createdOrder));
//            assertEquals(result.size(), 1);
//
//            List<Order> resultb = searchForOrderByLastNameUsingXForm(randomString, gson, searchUrl.url(), searchingBy);
//
//            assertNotNull(resultb);
//            assertTrue(resultb.contains(createdOrder));
//            assertEquals(resultb.size(), 1);
//
//            List<Order> resultc = searchForOrderByUsingJson(randomString, gson, searchOptions[i], searchUrl.url());
//
//            assertNotNull(resultc);
//            assertTrue(resultc.contains(createdOrder));
//            assertEquals(resultc.size(), 1);
//
//            result = searchForOrderByUsingJson(randomString.toLowerCase(), gson, searchOptions[i], searchUrl.url());
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByLastNameUsingXForm(randomString.toLowerCase(), gson, searchUrl.url(), searchingBy);
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByUsingJson(randomString.toUpperCase(), gson, searchOptions[i], searchUrl.url());
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByLastNameUsingXForm(randomString.toUpperCase(), gson, searchUrl.url(), searchingBy);
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByUsingJson(randomString.substring(5), gson, searchOptions[i], searchUrl.url());
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByLastNameUsingXForm(randomString.substring(5), gson, searchUrl.url(), searchingBy);
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByUsingJson(randomString.substring(5, 20), gson, searchOptions[i], searchUrl.url());
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByLastNameUsingXForm(randomString.substring(5, 20), gson, searchUrl.url(), searchingBy);
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByUsingJson(randomString.substring(5, 20).toLowerCase(), gson, searchOptions[i], searchUrl.url());
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByLastNameUsingXForm(randomString.substring(5, 20).toLowerCase(), gson, searchUrl.url(), searchingBy);
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByUsingJson(randomString.substring(5, 20).toUpperCase(), gson, searchOptions[i], searchUrl.url());
//            assertTrue(result.contains(createdOrder));
//
//            result = searchForOrderByLastNameUsingXForm(randomString.substring(5, 20).toUpperCase(), gson, searchUrl.url(), searchingBy);
//            assertTrue(result.contains(createdOrder));
//
//            searchWebClient = new WebClient();
//
//            searchByLastNameRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
//            searchByLastNameRequest.setRequestParameters(paramsList);
//
//            HtmlPage randomStringSearchHtmlPage = searchWebClient.getPage(searchByLastNameRequest);
//
//            assertEquals(randomStringSearchHtmlPage.getWebResponse().getStatusCode(), 200);
//
//            String title = randomStringSearchHtmlPage.getTitleText();
//            assertEquals(title, "Flooring Master");
//            DomElement orderTable = randomStringSearchHtmlPage.getElementById("order-table");
//
//            DomNodeList<HtmlElement> tableRows = orderTable.getElementsByTagName("tr");
//
//            assertTrue(tableRows.size() > 1);
//
//            java.util.Iterator<HtmlElement> tableRowIterator = tableRows.iterator();
//
//            Integer createdOrderRowNumber = null;
//            while (tableRowIterator.hasNext()) {
//                HtmlElement htmlElement = tableRowIterator.next();
//                String htmlText = htmlElement.asText();
//                if (htmlText.contains(Integer.toString(createdOrder.getId()))) {
//                    createdOrderRowNumber = tableRows.indexOf(htmlElement);
//                    break;
//                }
//            }
//
//            assertNotNull(createdOrderRowNumber);
//
//            HtmlElement specificRow = tableRows.get(createdOrderRowNumber);
//
//            String xml = specificRow.asXml();
//
//            assertTrue(xml.contains(createdOrder.getFirstName()));
//            assertTrue(xml.contains(createdOrder.getLastName()));
//            assertTrue(xml.contains(Integer.toString(createdOrder.getId())));
//            assertTrue(xml.contains("Edit"));
//            assertTrue(xml.contains("Delete"));
//        }
//
//        Integer databaseSizeAfterTest = getDatabaseSize();
//
//        assertEquals(databaseSizeAfterTest.intValue(), databaseSizeBeforeTest + searchOptions.length);
//    }

    private Integer getDatabaseSize() throws JsonSyntaxException, IOException, FailingHttpStatusCodeException {
        // Get Database size.        
        HttpUrl sizeUrl = getOrdersUrlBuilder()
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

    private Order deleteOrder(int resultId) throws FailingHttpStatusCodeException, JsonSyntaxException, IOException {
        // Delete the Created Order
        String orderIdString = Integer.toString(resultId);
        HttpUrl deleteUrl = getOrdersUrlBuilder()
                .addPathSegment(orderIdString)
                .build();
        Gson gson = new GsonBuilder().create();
        WebClient deleteOrderWebClient = new WebClient();
        WebRequest deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);
        Page deletePage = deleteOrderWebClient.getPage(deleteRequest);
        String returnedDeleteOrderJson = deletePage.getWebResponse().getContentAsString();
        Order returnedDeleteOrder = gson.fromJson(returnedDeleteOrderJson, Order.class);
        return returnedDeleteOrder;
    }

    private Order createOrderUsingJson(Order order) throws IOException, FailingHttpStatusCodeException, JsonSyntaxException, RuntimeException {
        // Create Order
        HttpUrl createUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();
        WebClient createOrderWebClient = new WebClient();
        Gson gson = new GsonBuilder().create();
        String orderJson = gson.toJson(order);
        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.POST);
        createRequest.setRequestBody(orderJson);
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");
        Page createdOrderPage = createOrderWebClient.getPage(createRequest);
        WebResponse createOrderWebResponse = createdOrderPage.getWebResponse();
        assertEquals(createOrderWebResponse.getStatusCode(), 200);
        assertTrue(createOrderWebResponse.getContentLength() > 100);
        Order createdOrder = null;
        if (createOrderWebResponse.getContentType().equals("application/json")) {
            String json = createOrderWebResponse.getContentAsString();
            createdOrder = gson.fromJson(json, Order.class);

            assertNotNull(createdOrder);
        } else {
            fail("Should have been JSON.");
        }
        return createdOrder;
    }

    @Test
    public void databaseSizeIsNotAccessibleFromABrowser() throws IOException {
        System.out.println("Database size can not be accessed for browser");

        HttpUrl sizeUrl = getOrdersUrlBuilder()
                .addPathSegment("size")
                .build();

        WebClient sizeWebClient = new WebClient();
        sizeWebClient.addRequestHeader("Accept", "application/json");

        Page sizePage = sizeWebClient.getPage(sizeUrl.url());
        WebResponse sizeResponse = sizePage.getWebResponse();
        assertEquals(sizeResponse.getStatusCode(), 200);
        assertTrue(sizeResponse.getContentLength() < 50);

        if (sizeResponse.getContentType().equals("application/json")) {
            String json = sizeResponse.getContentAsString();
            Integer numberReturned = Integer.parseInt(json);

            Assert.assertNotNull(numberReturned);
        } else {
            fail("Should have been JSON.");
        }

        // Now try without the Accept Json Header
        sizeWebClient = new WebClient();

        int statusCode = 0;
        String message;
        WebResponse webResponse = null;

        try {
            sizeWebClient.getPage(sizeUrl.url());
            fail("This was supposed to fail with a 404 error.");
        } catch (com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            message = ex.getStatusMessage();
            webResponse = ex.getResponse();
        }

        assertEquals(statusCode, 404);

        assertNotNull(webResponse);
        assertTrue(webResponse.getContentLength() == 0);

        String json = webResponse.getContentAsString();
        assertEquals(json, "");
    }
//
//    @Test
//    public void getSortedByLastName() throws IOException {
//        HttpUrl httpUrl = getOrdersUrlBuilder()
//                .addPathSegment("")
//                .addQueryParameter("sort_by", "last_name")
//                .build();
//
//        WebClient webClient = new WebClient();
//        webClient.addRequestHeader("Accept", "application/json");
//
//        Page page = webClient.getPage(httpUrl.url());
//        WebResponse webResponse = page.getWebResponse();
//        assertEquals(webResponse.getStatusCode(), 200);
//        assertTrue(webResponse.getContentLength() > 100);
//
//        Order[] ordersReturned = null;
//        Order[] ordersReturned2 = null;
//
//        if (webResponse.getContentType().equals("application/json")) {
//            String json = webResponse.getContentAsString();
//            Gson gson = new GsonBuilder().create();
//            ordersReturned = gson.fromJson(json, Order[].class);
//            ordersReturned2 = gson.fromJson(json, Order[].class);
//
//            assertTrue(ordersReturned.length > 20);
//            assertTrue(ordersReturned2.length > 20);
//        } else {
//            fail("Should have been JSON.");
//        }
//
//        List<Order> ordersSortedByComparator = Arrays.asList(ordersReturned);
//        List<Order> ordersSortedByDatabase = Arrays.asList(ordersReturned2);
//
//        ordersSortedByComparator.sort(sortByLastName());
//
//        for (int i = 0; i < ordersSortedByComparator.size(); i++) {
//            assertEquals("IDs: " + ordersSortedByComparator.get(i).getId() + ", " + ordersSortedByDatabase.get(i).getId(), ordersSortedByComparator.get(i), ordersSortedByDatabase.get(i));
//        }
//    }
//
//    @Test
//    public void getSortedByFirstName() throws IOException {
//        System.out.println("Sort By First Name");
//
//        HttpUrl httpUrl = getOrdersUrlBuilder()
//                .addPathSegment("")
//                .addQueryParameter("sort_by", "first_name")
//                .build();
//
//        WebClient webClient = new WebClient();
//        webClient.addRequestHeader("Accept", "application/json");
//
//        Page page = webClient.getPage(httpUrl.url());
//        WebResponse webResponse = page.getWebResponse();
//        assertEquals(webResponse.getStatusCode(), 200);
//        assertTrue(webResponse.getContentLength() > 100);
//
//        Order[] ordersReturned = null;
//        Order[] ordersReturned2 = null;
//
//        if (webResponse.getContentType().equals("application/json")) {
//            String json = webResponse.getContentAsString();
//            Gson gson = new GsonBuilder().create();
//            ordersReturned = gson.fromJson(json, Order[].class);
//            ordersReturned2 = gson.fromJson(json, Order[].class);
//
//            assertTrue(ordersReturned.length > 20);
//            assertTrue(ordersReturned2.length > 20);
//        } else {
//            fail("Should have been JSON.");
//        }
//
//        List<Order> orders = Arrays.asList(ordersReturned);
//        List<Order> ordersFromDb = Arrays.asList(ordersReturned2);
//
//        orders.sort(sortByFirstName());
//
//        for (int i = 0; i < orders.size(); i++) {
//            assertEquals(orders.get(i), ordersFromDb.get(i));
//        }
//    }
//
//    @Test
//    public void getSortedByCompany() throws IOException {
//        System.out.println("Sort by Company");
//
//        HttpUrl httpUrl = getOrdersUrlBuilder()
//                .addPathSegment("")
//                .addQueryParameter("sort_by", "company")
//                .build();
//
//        WebClient webClient = new WebClient();
//        webClient.addRequestHeader("Accept", "application/json");
//
//        Page page = webClient.getPage(httpUrl.url());
//        WebResponse webResponse = page.getWebResponse();
//        assertEquals(webResponse.getStatusCode(), 200);
//        assertTrue(webResponse.getContentLength() > 100);
//
//        Order[] ordersReturned = null;
//        Order[] ordersReturned2 = null;
//
//        if (webResponse.getContentType().equals("application/json")) {
//            String json = webResponse.getContentAsString();
//            Gson gson = new GsonBuilder().create();
//            ordersReturned = gson.fromJson(json, Order[].class);
//            ordersReturned2 = gson.fromJson(json, Order[].class);
//
//            assertTrue(ordersReturned.length > 20);
//            assertTrue(ordersReturned2.length > 20);
//        } else {
//            fail("Should have been JSON.");
//        }
//
//        List<Order> orders = Arrays.asList(ordersReturned);
//        List<Order> ordersFromDb = Arrays.asList(ordersReturned2);
//
//        orders.sort(sortByCompany());
//
//        for (int i = 0; i < orders.size(); i++) {
//            assertEquals("Made it " + i + " into the list. " + orders.get(i).getId() + ", " + ordersFromDb.get(i).getId(), orders.get(i), ordersFromDb.get(i));
//        }
//    }
//
//    @Test
//    public void getSortedById() throws IOException {
//        System.out.println("Sorted By ID");
//
//        HttpUrl httpUrl = getOrdersUrlBuilder()
//                .addPathSegment("")
//                .addQueryParameter("sort_by", "id")
//                .build();
//
//        WebClient webClient = new WebClient();
//        webClient.addRequestHeader("Accept", "application/json");
//
//        Page page = webClient.getPage(httpUrl.url());
//        WebResponse webResponse = page.getWebResponse();
//        assertEquals(webResponse.getStatusCode(), 200);
//        assertTrue(webResponse.getContentLength() > 100);
//
//        Order[] ordersReturned = null;
//        Order[] ordersReturned2 = null;
//
//        if (webResponse.getContentType().equals("application/json")) {
//            String json = webResponse.getContentAsString();
//            Gson gson = new GsonBuilder().create();
//            ordersReturned = gson.fromJson(json, Order[].class);
//            ordersReturned2 = gson.fromJson(json, Order[].class);
//
//            assertTrue(ordersReturned.length > 20);
//            assertTrue(ordersReturned2.length > 20);
//        } else {
//            fail("Should have been JSON.");
//        }
//
//        List<Order> orders = Arrays.asList(ordersReturned);
//        List<Order> ordersFromDb = Arrays.asList(ordersReturned2);
//
//        orders.sort(new Comparator<Order>() {
//            @Override
//            public int compare(Order order1, Order order2) {
//                return order2.getId().compareTo(order1.getId());
//            }
//        });
//
//        for (int i = 0; i < orders.size(); i++) {
//            assertEquals(orders.get(i), ordersFromDb.get(i));
//        }
//    }
//
//    @Test
//    public void getSortedByDefault() throws IOException {
//        System.out.println("Sort by Default");
//
//        HttpUrl httpUrl = getOrdersUrlBuilder()
//                .addPathSegment("")
//                .build();
//
//        WebClient webClient = new WebClient();
//        webClient.addRequestHeader("Accept", "application/json");
//
//        Page page = webClient.getPage(httpUrl.url());
//        WebResponse webResponse = page.getWebResponse();
//        assertEquals(webResponse.getStatusCode(), 200);
//        assertTrue(webResponse.getContentLength() > 100);
//
//        Order[] ordersReturned = null;
//        Order[] ordersReturned2 = null;
//
//        if (webResponse.getContentType().equals("application/json")) {
//            String json = webResponse.getContentAsString();
//            Gson gson = new GsonBuilder().create();
//            ordersReturned = gson.fromJson(json, Order[].class);
//            ordersReturned2 = gson.fromJson(json, Order[].class);
//
//            assertTrue(ordersReturned.length > 20);
//            assertTrue(ordersReturned2.length > 20);
//        } else {
//            fail("Should have been JSON.");
//        }
//
//        List<Order> orders = Arrays.asList(ordersReturned);
//        List<Order> ordersFromDb = Arrays.asList(ordersReturned2);
//
//        orders.sort(new Comparator<Order>() {
//            @Override
//            public int compare(Order order1, Order order2) {
//                return order1.getId().compareTo(order2.getId());
//            }
//        });
//
//        for (int i = 0; i < orders.size(); i++) {
//            assertEquals(orders.get(i), ordersFromDb.get(i));
//        }
//    }
//
//    @Test
//    public void getPaginatedList() throws IOException {
//        System.out.println("list by pagination");
//
//        List<Order> createdOrders = new ArrayList();
//
//        Gson gson = new GsonBuilder().create();
//
//        Order order = orderGenerator();
//
//        order.setFirstName("Doug");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Doug Jr.");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Doug III");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Other Doug");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Steve");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Dave");
//        Order daveOrder = this.createOrderUsingJson(order);
//        createdOrders.add(daveOrder);
//
//        order.setFirstName("Phil");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Stephen");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Steven");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        order.setFirstName("Steven");
//        createdOrders.add(this.createOrderUsingJson(order));
//
//        // Check search using json object built with my purspective api.
//        // Get The List Of Orders
//        HttpUrl searchUrl = getOrdersUrlBuilder()
//                .addPathSegment("search")
//                .addQueryParameter("sort_by", "last_name")
//                .addQueryParameter("page", Integer.toString(0))
//                .addQueryParameter("results", Integer.toString(5))
//                .build();
//
//        WebClient webClient = new WebClient();
//
//        OrderSearchRequest orderSearchRequest = new OrderSearchRequest(order.getLastName(), OrderSearchByOptionEnum.LAST_NAME);
//
//        String orderSearchRequestJson2 = gson.toJson(orderSearchRequest);
//
//        WebRequest searchByLastNameWebRequest = new WebRequest(searchUrl.url(), HttpMethod.POST);
//        searchByLastNameWebRequest.setRequestBody(orderSearchRequestJson2);
//
//        searchByLastNameWebRequest.setAdditionalHeader("Accept", "application/json");
//        searchByLastNameWebRequest.setAdditionalHeader("Content-type", "application/json");
//
//        Page lastNameSearchPage = webClient.getPage(searchByLastNameWebRequest);
//
//        assertEquals(lastNameSearchPage.getWebResponse().getStatusCode(), 200);
//
//        String searchOrderJson = lastNameSearchPage.getWebResponse().getContentAsString();
//
//        Order[] returnedLastNameSearchOrders = gson.fromJson(searchOrderJson, Order[].class);
//
//        assertEquals(returnedLastNameSearchOrders.length, 5);
//
//        Order firstReturnedLastNameSearchOrder = returnedLastNameSearchOrders[0];
//
//        assertEquals(daveOrder, firstReturnedLastNameSearchOrder);
//
//        webClient = new WebClient();
//
//        URL searchUrl2 = HttpUrl.get(searchByLastNameWebRequest.getUrl()).newBuilder()
//                .removeAllQueryParameters("page")
//                .addQueryParameter("page", Integer.toString(1))
//                .build()
//                .url();
//
//        searchByLastNameWebRequest.setUrl(searchUrl2);
//        Page lastNameSearchPage2 = webClient.getPage(searchByLastNameWebRequest);
//
//        assertEquals(lastNameSearchPage2.getWebResponse().getStatusCode(), 200);
//
//        Order[] returnedLastNameSearchOrders2
//                = gson.fromJson(lastNameSearchPage2.getWebResponse().getContentAsString(), Order[].class);
//
//        assertEquals(returnedLastNameSearchOrders2.length, 5);
//
//        Set<Order> orderSet = new HashSet();
//
//        orderSet.addAll(Arrays.asList(returnedLastNameSearchOrders));
//        orderSet.addAll(Arrays.asList(returnedLastNameSearchOrders2));
//
//        assertEquals(orderSet.size(), 10);
//
//        for (Order orderToCheck : createdOrders) {
//            assertTrue(orderSet.contains(orderToCheck));
//        }
//
//        assertTrue(orderSet.containsAll(createdOrders));
//
//        for (Order orderToDelete : createdOrders) {
//            deleteOrder(orderToDelete.getId());
//        }
//
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

    private Order orderGenerator() throws IOException {

        State state = getRandomState();
        Product product = getRandomProduct();

        Calendar postgresSupportedCalendar = Calendar.getInstance();
        postgresSupportedCalendar.setTimeInMillis(0);

        // Postgres Supports back to 4713 BC but Gson and Spring have
        // trouble figuring out what 4000 BC should be serialized as.
        int year = random.nextInt(9000);
        int month = random.nextInt(12);
        int date = random.nextInt(32);

        postgresSupportedCalendar.set(year, month, date, 0, 0, 0);

        Date postgresSupportedDate = postgresSupportedCalendar.getTime();

        Order order = orderBuilder(UUID.randomUUID().toString(), random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble(), postgresSupportedDate, product, state);
        return order;
    }

    private State getRandomState() throws IOException {
        Gson gson = new GsonBuilder().create();

        // Get The List Of States
        HttpUrl getListUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("state")
                .addPathSegment("")
                .build();

        WebClient getListWebClient = new WebClient();
        getListWebClient.addRequestHeader("Accept", "application/json");

        Page getListPage = getListWebClient.getPage(getListUrl.url());
        WebResponse getListWebResponse = getListPage.getWebResponse();
        assertEquals(getListWebResponse.getStatusCode(), 200);
        assertTrue(getListWebResponse.getContentLength() > 100);

        State[] list = null;

        if (getListWebResponse.getContentType().equals("application/json")) {
            String json = getListWebResponse.getContentAsString();
            State[] states = gson.fromJson(json, State[].class);

            assertTrue(states.length > 0);

            list = states;
        } else {
            fail("Should have been JSON.");
        }

        return list[random.nextInt(list.length)];
    }

    private Order getRandomOrder() throws IOException {
        Gson gson = new GsonBuilder().create();

        // Get The List Of States
        HttpUrl getListUrl = getOrdersUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient getListWebClient = new WebClient();
        getListWebClient.addRequestHeader("Accept", "application/json");

        Page getListPage = getListWebClient.getPage(getListUrl.url());
        WebResponse getListWebResponse = getListPage.getWebResponse();
        assertEquals(getListWebResponse.getStatusCode(), 200);
        assertTrue(getListWebResponse.getContentLength() > 100);

        Order[] list = null;

        if (getListWebResponse.getContentType().equals("application/json")) {
            String json = getListWebResponse.getContentAsString();
            Order[] orders = gson.fromJson(json, Order[].class);

            assertTrue(orders.length > 0);

            list = orders;
        } else {
            fail("Should have been JSON.");
        }

        return list[random.nextInt(list.length)];
    }

    private HttpUrl.Builder getOrdersUrlBuilder() {
        return HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("orders");
    }

    private Product getRandomProduct() throws IOException {
        HttpUrl httpUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("product")
                .addPathSegment("")
                .build();

        WebClient webClient = new WebClient();
        webClient.addRequestHeader("Accept", "application/json");

        Page page = webClient.getPage(httpUrl.url());
        WebResponse webResponse = page.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 100);

        Product[] products = null;

        if (webResponse.getContentType().equals("application/json")) {
            String json = webResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            products = gson.fromJson(json, Product[].class);

            assertTrue(products.length > 2);

        } else {
            fail("Should have been JSON.");
        }

        return products[random.nextInt(products.length)];
    }

    private Product productGenerator() {
        Random random = new Random();

        Product product = productBuilder(UUID.randomUUID().toString(), random.nextDouble(), random.nextDouble());
        return product;
    }

    private Product productBuilder(String name, double cost, double labor) {
        Product product = new Product();
        product.setProductName(name);
        product.setCost(cost);
        product.setLaborCost(labor);
        return product;
    }
}
