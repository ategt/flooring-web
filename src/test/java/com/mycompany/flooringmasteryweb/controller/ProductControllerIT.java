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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.utilities.ProductUtilities;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    public void formByFormsTest() throws MalformedURLException, IOException {
        System.out.println("Form Test");

        Random random = new Random();
        
        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
 
        int beforeCreation = htmlPage.getElementsByTagName("tr").size();
        
        HtmlInput productName = (HtmlInput) htmlPage.getElementById("productName");        
        
        String productNameFormText = UUID.randomUUID().toString();
        
        productName.setValueAttribute(productNameFormText);
                
        String productCostText = Double.toString(ProductUtilities.roundToDecimalPlace(random.nextDouble(), 4));
        
        HtmlInput productCost = (HtmlInput) htmlPage.getElementById("productCost");
        productCost.setValueAttribute(productCostText);
        
        String productLaborCostText = Double.toString(ProductUtilities.roundToDecimalPlace(random.nextDouble(), 4));
        
        HtmlInput productLaborCost = (HtmlInput) htmlPage.getElementById("laborCost");
        productLaborCost.setValueAttribute(productLaborCostText);
        
        DomElement updateButton = htmlPage.getElementById("product-update-btn");
        
        Page updatedPage = updateButton.click();
        
        assertTrue(updatedPage.isHtmlPage());
        
        HtmlPage updatedHtmlPage = (HtmlPage) updatedPage;
        
        title = updatedHtmlPage.getTitleText();
        assertEquals(title, "Flooring Master");

        String pageText = updatedHtmlPage.asText();
        
        assertTrue("Product Name: " + productNameFormText + " could not be found.", pageText.contains(productNameFormText));
        assertTrue("Labor: " + productLaborCostText + " could not be found.", pageText.contains(productLaborCostText));
        assertTrue("Cost: " + productCostText + " could not be found.", pageText.contains(productCostText));

        htmlPage = updatedHtmlPage;
        
        title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
 
        int afterCreation = htmlPage.getElementsByTagName("tr").size();
        
        assertEquals(afterCreation, beforeCreation + 1);
        
        productName = (HtmlInput) htmlPage.getElementById("productName");        
        String productNameValue = productName.getValueAttribute();
        assertEquals(productNameValue, productNameFormText);
        
        productCostText = Double.toString(ProductUtilities.roundToDecimalPlace(random.nextDouble(), 4));
        
        productCost = (HtmlInput) htmlPage.getElementById("productCost");
        productCost.setValueAttribute(productCostText);
        
        productLaborCostText = Double.toString(ProductUtilities.roundToDecimalPlace(random.nextDouble(), 4));
        
        productLaborCost = (HtmlInput) htmlPage.getElementById("laborCost");
        productLaborCost.setValueAttribute(productLaborCostText);
        
        updateButton = htmlPage.getElementById("product-update-btn");
        
        updatedPage = updateButton.click();
        
        assertTrue(updatedPage.isHtmlPage());
        
        updatedHtmlPage = (HtmlPage) updatedPage;
        
        title = updatedHtmlPage.getTitleText();
        assertEquals(title, "Flooring Master");

        pageText = updatedHtmlPage.asText();
        
        assertTrue("Product Name: " + productNameFormText + " could not be found.", pageText.contains(productNameFormText));
        assertTrue("Labor: " + productLaborCostText + " could not be found.", pageText.contains(productLaborCostText));
        assertTrue("Cost: " + productCostText + " could not be found.", pageText.contains(productCostText));

        int afterEdit = updatedHtmlPage.getElementsByTagName("tr").size();
        assertEquals(afterEdit, afterCreation);
    }

    @Test
    public void createTest() throws IOException {
        System.out.println("Create Test");

        Product product = productGenerator();
        Assert.assertNotNull(product);
        Assert.assertNull(product.getId());
        
        product = ProductUtilities.titleCaseProductName(product);

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

    @Test
    public void createAutomaticUsingTitleCaseTest() throws IOException {
        System.out.println("Create Test");

        Product product = productGenerator();
        Assert.assertNotNull(product);
        Assert.assertNull(product.getId());
        
        char[] letters = "abcdefghigklmnopqrstuvwxyz".toCharArray();
        
        char randomLetter = letters[new Random().nextInt(letters.length)];
        
        product.setProductName(randomLetter + product.getProductName());                
                
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

        product = ProductUtilities.titleCaseProductName(product);

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
    
    @Test
    public void loadIndexJson() throws IOException {
        HttpUrl httpUrl = getProductUrlBuilder()
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
            Product[] products = gson.fromJson(json, Product[].class);

            assertTrue(products.length > 2);

            if (productCountFromIndex == null) {
                productCountFromIndex = products.length;
            } else {
                assertEquals(productCountFromIndex.intValue(), products.length);
            }
        } else {
            fail("Should have been JSON.");
        }
    }

    @Test
    public void verifyJsonAndHtmlIndexHaveSameProducts() throws IOException {
        System.out.println("Verify Json And Html Have Same Products");

        HttpUrl httpUrl = getProductUrlBuilder()
                .addPathSegment("")
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

        Product[] products = null;

        if (jsonResponse.getContentType().equals("application/json")) {
            String json = jsonResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            products = gson.fromJson(json, Product[].class);

            assertTrue(products.length > 2);
        } else {
            fail("Should have been JSON.");
        }

        DomElement productTable = htmlPage.getElementById("product-table");

        DomNodeList<HtmlElement> productRows = productTable.getElementsByTagName("tr");

        assertNotNull(products);
        assertEquals(productRows.size(), products.length + 1);

        final String htmlText = htmlPage.asText();

        Arrays.stream(products)
                .forEach((product) -> {
                    String firstName = product.getProductName();
                    if (Objects.nonNull(firstName)) {
                        assertTrue(htmlText.contains(firstName));
                    }

                    String lastName = product.getProductName();
                    if (Objects.nonNull(lastName)) {
                        assertTrue(htmlText.contains(lastName));
                    }

                    String productName = product.getProductName();
                    assertTrue(htmlText.contains(productName));
                });
    }
    
    /**
     * Test of create method, of class ProductDaoPostgresImpl.
     */
    @Test
    public void testCRUD() throws IOException {
        System.out.println("CRUD test");

        Product product = productGenerator();

        Gson gson = new GsonBuilder().create();

        // Get Size of Database before creation.
        HttpUrl sizeUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient sizeWebClient = new WebClient();
        sizeWebClient.addRequestHeader("Accept", "application/json");

        Page sizePage = sizeWebClient.getPage(sizeUrl.url());
        WebResponse sizeResponse = sizePage.getWebResponse();
        assertEquals(sizeResponse.getStatusCode(), 200);        

        Integer beforeCreation = null;

        if (sizeResponse.getContentType().equals("application/json")) {
            String json = sizeResponse.getContentAsString();
            Product[] productsBeforeCreation = gson.fromJson(json, Product[].class);
            beforeCreation = productsBeforeCreation.length;
            
            Assert.assertNotNull(beforeCreation);
        } else {
            fail("Should have been JSON.");
        }

        // Create Product
        HttpUrl createUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createProductWebClient = new WebClient();

        String productJson = gson.toJson(product);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.PUT);
        createRequest.setRequestBody(productJson);

        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createPage = createProductWebClient.getPage(createRequest);

        String returnedProductJson = createPage.getWebResponse().getContentAsString();

        Product productReturned = gson.fromJson(returnedProductJson, Product.class);

        assertNotNull(productReturned.getId());
        assertTrue(productReturned.getId() > 0);

        // Get Database Size After Creation
        WebClient sizeWebClient2 = new WebClient();
        sizeWebClient2.addRequestHeader("Accept", "application/json");

        Page sizePage2 = sizeWebClient2.getPage(sizeUrl.url());
        WebResponse sizeResponse2 = sizePage2.getWebResponse();
        assertEquals(sizeResponse2.getStatusCode(), 200);
        assertTrue("Response Length: " + sizeResponse2.getContentLength(), sizeResponse2.getContentLength() > 50);

        Integer afterCreation = null;

        if (sizeResponse2.getContentType().equals("application/json")) {
            String json = sizeResponse2.getContentAsString();
            Product[] productsAfterCreation = gson.fromJson(json, Product[].class);
            afterCreation = productsAfterCreation.length;
            
            Assert.assertNotNull(afterCreation);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(afterCreation);
        assertEquals(beforeCreation + 1, afterCreation.intValue());

        assertNotNull(productReturned);
        assertNotNull(productReturned.getId());

        assertTrue(productReturned.getId() > 0);

        // Check that created product is in the Database.
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

        Assert.assertNotNull(specificProduct);

        assertEquals(productReturned, specificProduct);

        Random random = new Random();
        
        productReturned.setCost(random.nextDouble());
        productReturned.setLaborCost(random.nextDouble());

        // Update Product With Service PUT endpoint
        HttpUrl updateUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient updateProductWebClient = new WebClient();

        String updatedProductJson = gson.toJson(productReturned);

        WebRequest updateRequest = new WebRequest(updateUrl.url(), HttpMethod.PUT);
        updateRequest.setRequestBody(updatedProductJson);

        updateRequest.setAdditionalHeader("Accept", "application/json");
        updateRequest.setAdditionalHeader("Content-type", "application/json");

        Page updatePage = updateProductWebClient.getPage(updateRequest);

        String returnedUpdatedProductJson = updatePage.getWebResponse().getContentAsString();

        Product returnedUpdatedProduct = gson.fromJson(returnedUpdatedProductJson, Product.class);

        assertEquals(productReturned.getCost(), returnedUpdatedProduct.getCost(), .0001d);
        assertEquals(productReturned.getLaborCost(), returnedUpdatedProduct.getLaborCost(), .0001d);

        // Verify Update Did Not Increase the Size of the Database
        WebClient sizeWebClient3 = new WebClient();
        sizeWebClient3.addRequestHeader("Accept", "application/json");

        Page sizePage3 = sizeWebClient3.getPage(sizeUrl.url());
        WebResponse sizeResponse3 = sizePage3.getWebResponse();
        assertEquals(sizeResponse3.getStatusCode(), 200);
        assertTrue("Response3 Length:" + sizeResponse3.getContentLength() ,sizeResponse3.getContentLength() > 50);

        Integer afterUpdate = null;

        if (sizeResponse3.getContentType().equals("application/json")) {
            String json = sizeResponse3.getContentAsString();
            Product[] productsAfterUpdate = gson.fromJson(json, Product[].class);

            afterUpdate = productsAfterUpdate.length;
            
            Assert.assertNotNull(afterUpdate);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(afterUpdate);
        assertEquals(afterUpdate.intValue(), afterCreation.intValue());

        // Delete the Created Product
        String productIdString = productReturned.getProductName();

        HttpUrl deleteUrl = getProductUrlBuilder()
                .addPathSegment(productIdString)
                .build();

        WebClient deleteProductWebClient = new WebClient();

        WebRequest deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        Page deletePage = deleteProductWebClient.getPage(deleteRequest);

        String returnedDeleteProductJson = deletePage.getWebResponse().getContentAsString();

        Product returnedDeleteProduct = gson.fromJson(returnedDeleteProductJson, Product.class);

        assertEquals(returnedDeleteProduct, returnedUpdatedProduct);

        // Delete The Created Product A Second Time
        deleteProductWebClient = new WebClient();

        deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        deletePage = deleteProductWebClient.getPage(deleteRequest);

        returnedDeleteProductJson = deletePage.getWebResponse().getContentAsString();
        assertEquals(returnedDeleteProductJson, "");

        Product returnedDeleteProduct2 = gson.fromJson(returnedDeleteProductJson, Product.class);
        assertNull(returnedDeleteProduct2);

        assertNotEquals(returnedDeleteProduct, returnedDeleteProduct2);

        // Verify Product Database Size Has Shrunk By One After Deletion
        WebClient sizeWebClient4 = new WebClient();
        sizeWebClient4.addRequestHeader("Accept", "application/json");

        Page sizePage4 = sizeWebClient4.getPage(sizeUrl.url());
        WebResponse sizeResponse4 = sizePage4.getWebResponse();
        assertEquals(sizeResponse4.getStatusCode(), 200);
        assertTrue("Size Response4: " + sizeResponse4.getContentLength(), sizeResponse4.getContentLength() > 50);

        Integer afterDeletion = null;

        if (sizeResponse4.getContentType().equals("application/json")) {
            String json = sizeResponse4.getContentAsString();
            Product[] productsAfterDeletion = gson.fromJson(json, Product[].class);
            afterDeletion = productsAfterDeletion.length;
            
            Assert.assertNotNull(afterDeletion);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(beforeCreation);
        assertNotNull(afterDeletion);
        assertEquals(beforeCreation.intValue(), afterDeletion.intValue());
        assertEquals(afterCreation - 1, afterDeletion.intValue());

        // Try to Get Deleted Product
        HttpUrl showUrl2 = getProductUrlBuilder()
                .addPathSegment(productReturned.getProductName())
                .build();

        WebClient showProductWebClient2 = new WebClient();
        showProductWebClient2.addRequestHeader("Accept", "application/json");

        Page singleProductPage2 = showProductWebClient2.getPage(showUrl.url());
        WebResponse jsonSingleProductResponse2 = singleProductPage2.getWebResponse();
        assertEquals(jsonSingleProductResponse2.getStatusCode(), 200);
        assertTrue("Single Product Response2: " + jsonSingleProductResponse2.getContentLength(), jsonSingleProductResponse2.getContentLength() < 50);

        String contentType = jsonSingleProductResponse2.getContentType();
        assertEquals(contentType, "");

        String contentString = jsonSingleProductResponse2.getContentAsString();

        assertEquals(contentString, "");

    }
    
    /**
     * Test of list method, of class ProductDaoPostgresImpl.
     */
    @Test
    public void testList() throws IOException {
        System.out.println("list");

        Product product = productGenerator();

        // Create Generated Product
        HttpUrl createUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createProductWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String productJson = gson.toJson(product);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.PUT);
        createRequest.setRequestBody(productJson);
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createdProductPage = createProductWebClient.getPage(createRequest);

        WebResponse createProductWebResponse = createdProductPage.getWebResponse();
        assertEquals(createProductWebResponse.getStatusCode(), 200);
        assertTrue(createProductWebResponse.getContentLength() > 100);

        Product createdProduct = null;

        if (createProductWebResponse.getContentType().equals("application/json")) {
            String json = createProductWebResponse.getContentAsString();
            createdProduct = gson.fromJson(json, Product.class);

            assertNotNull(createdProduct);
        } else {
            fail("Should have been JSON.");
        }

        // Get The List Of Products
        HttpUrl getListUrl = getProductUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient getListWebClient = new WebClient();
        getListWebClient.addRequestHeader("Accept", "application/json");

        Page getListPage = getListWebClient.getPage(getListUrl.url());
        WebResponse getListWebResponse = getListPage.getWebResponse();
        assertEquals(getListWebResponse.getStatusCode(), 200);
        assertTrue(getListWebResponse.getContentLength() > 100);

        List<Product> list = null;

        if (getListWebResponse.getContentType().equals("application/json")) {
            String json = getListWebResponse.getContentAsString();
            Product[] products = gson.fromJson(json, Product[].class);

            assertTrue(products.length > 20);

            list = Arrays.asList(products);
        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(list);

        assertTrue(list.contains(createdProduct));
        assertNotEquals(product, createdProduct);

        assertNotNull(createdProduct);
        Integer createdProductId = createdProduct.getId();
        product.setId(createdProductId);

        assertNotNull(createdProductId);
        assertEquals("Product: " + product.getId() + ", " + product.getProductName() + ", " + product.getCost() + ", " + product.getLaborCost() + "\n" + 
                "Product Created: " + createdProduct.getId() + ", " + createdProduct.getProductName() + ", " + createdProduct.getCost() + ", " + createdProduct.getLaborCost(),
                product, createdProduct);
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
