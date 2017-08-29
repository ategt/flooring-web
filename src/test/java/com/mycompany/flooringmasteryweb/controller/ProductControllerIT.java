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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dto.Product;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Random;
import java.util.UUID;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ATeg
 */
public class ProductControllerIT {

    ApplicationContext ctx;
    URI uriToTest;

    Integer productCountFromIndex = null;

    public ProductControllerIT() {
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
        System.out.println("List Test");

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
    }

    @Test
    public void createTest() throws IOException {
        System.out.println("Create Test");

        Product product = productGenerator();
        Assert.assertNotNull(product);
        Assert.assertNull(product.getId());

        HttpUrl createUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createProductWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String productJson = gson.toJson(product);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.PUT);
        createRequest.setRequestBody(productJson);
        createRequest.setAdditionalHeader("Content-type", "application/json");
        createRequest.setAdditionalHeader("Accept", "application/json");

        Page createPage = createProductWebClient.getPage(createRequest);

        String returnedProductJson = createPage.getWebResponse().getContentAsString();

        Product productReturned = gson.fromJson(returnedProductJson, Product.class);

        Assert.assertNotNull(productReturned);
        Integer returnedProductId = productReturned.getId();

        Assert.assertTrue(returnedProductId > 0);

        product.setId(returnedProductId);

        Assert.assertEquals("Product Returned: " + productReturned.getId() + ", " + productReturned.getProductName() + ", " + productReturned.getCost() + ", " + productReturned.getLaborCost() + "\n" + 
                "Product Started: " + product.getId() + ", " + product.getProductName() + ", " + product.getCost() + ", " + product.getLaborCost()
                ,productReturned, product);

        HttpUrl showUrl = getProductUrlBuilder()
                .addPathSegment(productReturned.getProductName())
                .build();

        WebClient showProductWebClient = new WebClient();
        showProductWebClient.addRequestHeader("Accept", "application/json");

        Page singleProductPage = showProductWebClient.getPage(showUrl.url());
        WebResponse jsonSingleProductResponse = singleProductPage.getWebResponse();
        assertEquals(jsonSingleProductResponse.getStatusCode(), 200);
        assertTrue(jsonSingleProductResponse.getContentLength() > 50);

        Product specificProduct = null;

        if (jsonSingleProductResponse.getContentType().equals("application/json")) {
            String json = jsonSingleProductResponse.getContentAsString();
            specificProduct = gson.fromJson(json, Product.class);

            Assert.assertNotNull(specificProduct);
        } else {
            fail("Should have been JSON.");
        }
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

    private HttpUrl.Builder getProductUrlBuilder() {
        return HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("product");
    }
}
