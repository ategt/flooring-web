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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mycompany.flooringmasteryweb.dto.Address;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import okhttp3.HttpUrl;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ATeg
 */
public class SuggestionAndAutoCompleteSeleneseIT {

    ApplicationContext ctx;
    URI uriToTest;

    Integer addressCountFromIndex = null;

    public SuggestionAndAutoCompleteSeleneseIT() {
        ctx = new ClassPathXmlApplicationContext("integrationTest-Context.xml");
    }

    @Before
    public void setUp() {
        uriToTest = ctx.getBean("baseUrlToTest", URI.class);
    }

    @Test
    public void testSimple() throws Exception {

        final Address address = createAddressUsingJson(addressGenerator());

        String searchString = address.getLastName().substring(5, 20).toLowerCase();

        HttpUrl getUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(searchString)
                .addPathSegment("name_completion")
                .build();

        WebClient showAddressWebClient = new WebClient();
        showAddressWebClient.addRequestHeader("Accept", "application/json");

        Page autoCompleteSuggestionsPage = showAddressWebClient.getPage(getUrl.url());
        WebResponse jsonSingleAddressResponse = autoCompleteSuggestionsPage.getWebResponse();
        assertEquals(jsonSingleAddressResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + jsonSingleAddressResponse.getContentLength(), jsonSingleAddressResponse.getContentLength() > 35);

        String[] returnedSuggestions = null;

        if (jsonSingleAddressResponse.getContentType().equals("application/json")) {
            String json = jsonSingleAddressResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();

            returnedSuggestions = gson.fromJson(json, String[].class);

            Assert.assertNotNull(returnedSuggestions);
        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(returnedSuggestions);

        assertTrue(Arrays.asList(returnedSuggestions).stream().anyMatch(suggestionString -> suggestionString.contains(address.getLastName())));

        assertTrue(returnedSuggestions.length < 31);

        Optional<String> optionalNameSuggestion = Arrays.asList(returnedSuggestions).stream().filter(suggestionString -> suggestionString.contains(address.getLastName())).findAny();

        String nameSuggestion = optionalNameSuggestion.orElse("");

        // Search the suggested result.
        HttpUrl searchUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("address")
                .addPathSegment(nameSuggestion)
                .addPathSegment("search")
                .build();
        
        showAddressWebClient = new WebClient();
        
        showAddressWebClient.addRequestHeader("Accept", "application/json");

        Page singleAddressPage = showAddressWebClient.getPage(searchUrl.url());
        WebResponse singleAddressJsonResponse = singleAddressPage.getWebResponse();
        assertEquals(singleAddressJsonResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + singleAddressJsonResponse.getContentLength(), singleAddressJsonResponse.getContentLength() > 35);

        Address returnedAddress = null;

        if (singleAddressJsonResponse.getContentType().equals("application/json")) {
            String json = singleAddressJsonResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();

            returnedAddress = gson.fromJson(json, Address.class);

            Assert.assertNotNull(returnedAddress);
        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(returnedAddress);
        
        assertEquals(returnedAddress, address);
    }

    private Address createAddressUsingJson(Address address) throws IOException, FailingHttpStatusCodeException, JsonSyntaxException, RuntimeException {
        // Create Address
        HttpUrl createUrl = HttpUrl.get(uriToTest)
                .newBuilder()
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
