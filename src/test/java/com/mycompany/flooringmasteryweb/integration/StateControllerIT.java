/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.ProductUtilities;
import com.mycompany.flooringmasteryweb.utilities.TextUtilities;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 *
 * @author ATeg
 */
public class StateControllerIT {

    ApplicationContext ctx;
    URI uriToTest;

    Integer stateCountFromIndex = null;

    public StateControllerIT() {
        ctx = new ClassPathXmlApplicationContext("integrationTest-Context.xml");
    }

    @Before
    public void setUp() {
        uriToTest = ctx.getBean("baseUrlToTest", URI.class);
        deleteTestState();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void listTest() throws MalformedURLException, IOException {
        System.out.println("List Test");

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getStateUrlBuilder()
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

        HttpUrl httpUrl = getStateUrlBuilder()
                .addPathSegment("")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");

        int beforeCreation = htmlPage.getElementsByTagName("tr").size();

        HtmlInput stateName = (HtmlInput) htmlPage.getElementById("stateName");

        String stateNameFormText = "AA";

        stateName.setValueAttribute(stateNameFormText);

        String stateTaxText = Double.toString(ProductUtilities.roundToDecimalPlace(random.nextDouble(), 4));

        HtmlInput stateTax = (HtmlInput) htmlPage.getElementById("stateTax");
        stateTax.setValueAttribute(stateTaxText);

        DomElement updateButton = htmlPage.getElementById("state-submit-btn");

        Page updatedPage = updateButton.click();

        assertTrue(updatedPage.isHtmlPage());

        HtmlPage updatedHtmlPage = (HtmlPage) updatedPage;

        title = updatedHtmlPage.getTitleText();
        assertEquals(title, "Flooring Master");

        String pageText = updatedHtmlPage.asText();

        stateNameFormText = TextUtilities.toTitleCase(stateNameFormText);

        assertTrue("State Name: \"Armed Forces Americas\" could not be found.", pageText.contains("Armed Forces Americas"));
        assertTrue("Tax: " + stateTaxText + " could not be found.", pageText.contains(stateTaxText));

        htmlPage = updatedHtmlPage;

        title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");

        int afterCreation = htmlPage.getElementsByTagName("tr").size();

        assertEquals(afterCreation, beforeCreation + 1);

        stateTaxText = Double.toString(ProductUtilities.roundToDecimalPlace(random.nextDouble(), 4));

        stateTax = (HtmlInput) htmlPage.getElementById("stateTax");
        stateTax.setValueAttribute(stateTaxText);

        updateButton = htmlPage.getElementById("state-submit-btn");

        updatedPage = updateButton.click();

        assertTrue(updatedPage.isHtmlPage());

        updatedHtmlPage = (HtmlPage) updatedPage;

        title = updatedHtmlPage.getTitleText();
        assertEquals(title, "Flooring Master");

        pageText = updatedHtmlPage.asText();

        assertTrue("State Name: \"Armed Forces Americas\" could not be found.", pageText.contains("Armed Forces Americas"));
        assertTrue("Tax: " + stateTaxText + " could not be found.", pageText.contains(stateTaxText));

        int afterEdit = updatedHtmlPage.getElementsByTagName("tr").size();
        assertEquals(afterEdit, afterCreation);

        HtmlAnchor deleteLink = updatedHtmlPage.getAnchorByHref("/state/delete/AA");
        Page deletedStatePage = deleteLink.click();

        assertTrue(deletedStatePage.isHtmlPage());
        HtmlPage deletePage = (HtmlPage) deletedStatePage;

        pageText = deletePage.asText();

        assertTrue("State Name: \"Armed Forces Americas\" was supposed to be deleted.", !pageText.contains("Armed Forces Americas"));
        assertTrue("Tax: " + stateTaxText + " should be gone.", !pageText.contains(stateTaxText));

        int afterDelete = deletePage.getElementsByTagName("tr").size();
        assertEquals(beforeCreation, afterDelete);

    }

    @Test
    public void createTest() throws IOException {
        System.out.println("Create Test");

        State state = new State();
        state.setStateName("aa");
        state.setStateTax(new Random().nextDouble());

        StateCommand commandState = StateCommand.buildCommandState(state);
        state = State.buildState(commandState);

        Assert.assertNotNull(commandState);
        Assert.assertNull(state.getId());

        HttpUrl createUrl = getStateUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createStateWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String stateJson = gson.toJson(commandState);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.PUT);
        createRequest.setRequestBody(stateJson);
        createRequest.setAdditionalHeader("Content-type", "application/json");
        createRequest.setAdditionalHeader("Accept", "application/json");

        Page createPage = createStateWebClient.getPage(createRequest);

        String returnedStateJson = createPage.getWebResponse().getContentAsString();

        State stateReturned = gson.fromJson(returnedStateJson, State.class);

        Assert.assertNotNull(stateReturned);
        Integer returnedStateId = stateReturned.getId();

        Assert.assertTrue(returnedStateId > 0);

        state.setId(returnedStateId);

        Assert.assertEquals("State Returned: " + stateReturned.getState() + ", " + stateReturned.getStateName() + ", " + stateReturned.getStateTax() + "\n"
                + "State Started: " + state.getState() + ", " + state.getStateName() + ", " + state.getStateTax(),
                stateReturned, state);

        HttpUrl showUrl = getStateUrlBuilder()
                .addPathSegment(stateReturned.getStateName())
                .build();

        WebClient showStateWebClient = new WebClient();
        showStateWebClient.addRequestHeader("Accept", "application/json");

        Page singleStatePage = showStateWebClient.getPage(showUrl.url());
        WebResponse jsonSingleStateResponse = singleStatePage.getWebResponse();
        assertEquals(jsonSingleStateResponse.getStatusCode(), 200);
        assertTrue(jsonSingleStateResponse.getContentLength() > 50);

        State specificState = null;

        if (jsonSingleStateResponse.getContentType().equals("application/json")) {
            String json = jsonSingleStateResponse.getContentAsString();
            specificState = gson.fromJson(json, State.class);

            Assert.assertNotNull(specificState);
        } else {
            fail("Should have been JSON.");
        }
    }

    @Test
    public void loadIndexJson() throws IOException {
        HttpUrl httpUrl = getStateUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient webClient = new WebClient();
        webClient.addRequestHeader("Accept", "application/json");

        Page page = webClient.getPage(httpUrl.url());
        WebResponse webResponse = page.getWebResponse();
        assertEquals(webResponse.getStatusCode(), 200);
        assertTrue(webResponse.getContentLength() > 10);

        if (webResponse.getContentType().equals("application/json")) {
            String json = webResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            State[] states = gson.fromJson(json, State[].class);

            assertTrue(states.length >= 1);

            if (stateCountFromIndex == null) {
                stateCountFromIndex = states.length;
            } else {
                assertEquals(stateCountFromIndex.intValue(), states.length);
            }
        } else {
            fail("Should have been JSON.");
        }
    }

    @Test
    public void verifyJsonAndHtmlIndexHaveSameStates() throws IOException {
        System.out.println("Verify Json And Html Have Same States");

        HttpUrl httpUrl = getStateUrlBuilder()
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

        State[] states = null;

        if (jsonResponse.getContentType().equals("application/json")) {
            String json = jsonResponse.getContentAsString();
            Gson gson = new GsonBuilder().create();
            states = gson.fromJson(json, State[].class);

            assertTrue(states.length >= 1);
        } else {
            fail("Should have been JSON.");
        }

        DomElement stateTable = htmlPage.getElementById("state-table");

        DomNodeList<HtmlElement> stateRows = stateTable.getElementsByTagName("tr");

        assertNotNull(states);
        assertEquals(stateRows.size(), states.length + 1);

        final String htmlText = htmlPage.asText();

        Arrays.stream(states)
                .forEach((state) -> {
                    String taxString = Double.toString(state.getStateTax());

                    assertTrue(htmlText.contains(taxString));
                });
    }

    /**
     * Test of create method, of class StateDaoPostgresImpl.
     */
    @Test
    public void testCRUD() throws IOException {
        System.out.println("CRUD test");

        State state = new State();
        state.setStateName("aa");
        state.setStateTax(ProductUtilities.roundToDecimalPlace(new Random().nextDouble(), 4));

        StateCommand commandState = StateCommand.buildCommandState(state);
        state = State.buildState(commandState);

        Gson gson = new GsonBuilder().create();

        // Get Size of Database before creation.
        HttpUrl sizeUrl = getStateUrlBuilder()
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
            State[] statesBeforeCreation = gson.fromJson(json, State[].class);
            beforeCreation = statesBeforeCreation.length;

            Assert.assertNotNull(beforeCreation);
        } else {
            fail("Should have been JSON.");
        }

        // Create State
        HttpUrl createUrl = getStateUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createStateWebClient = new WebClient();

        String stateJson = gson.toJson(commandState);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.PUT);
        createRequest.setRequestBody(stateJson);

        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createPage = createStateWebClient.getPage(createRequest);

        String returnedStateJson = createPage.getWebResponse().getContentAsString();

        State stateReturned = gson.fromJson(returnedStateJson, State.class);

        assertNotNull(stateReturned.getId());
        assertTrue(stateReturned.getId() > 0);

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
            State[] statesAfterCreation = gson.fromJson(json, State[].class);
            afterCreation = statesAfterCreation.length;

            Assert.assertNotNull(afterCreation);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(afterCreation);
        assertEquals(beforeCreation + 1, afterCreation.intValue());

        assertNotNull(stateReturned);
        assertNotNull(stateReturned.getId());

        assertTrue(stateReturned.getId() > 0);

        // Check that created state is in the Database.
        HttpUrl showUrl = getStateUrlBuilder()
                .addPathSegment(stateReturned.getStateName())
                .build();

        WebClient showStateWebClient = new WebClient();
        showStateWebClient.addRequestHeader("Accept", "application/json");

        Page singleStatePage = showStateWebClient.getPage(showUrl.url());
        WebResponse jsonSingleStateResponse = singleStatePage.getWebResponse();
        assertEquals(jsonSingleStateResponse.getStatusCode(), 200);
        assertTrue(jsonSingleStateResponse.getContentLength() > 50);

        State specificState = null;

        if (jsonSingleStateResponse.getContentType().equals("application/json")) {
            String json = jsonSingleStateResponse.getContentAsString();
            specificState = gson.fromJson(json, State.class);

            Assert.assertNotNull(specificState);

        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(specificState);

        assertEquals(stateReturned, specificState);

        Random random = new Random();

        stateReturned.setStateTax(ProductUtilities.roundToDecimalPlace(random.nextDouble(), 4));

        // Update State With Service PUT endpoint
        HttpUrl updateUrl = getStateUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient updateStateWebClient = new WebClient();

        String updatedStateJson = gson.toJson(StateCommand.buildCommandState(stateReturned));

        WebRequest updateRequest = new WebRequest(updateUrl.url(), HttpMethod.PUT);
        updateRequest.setRequestBody(updatedStateJson);

        updateRequest.setAdditionalHeader("Accept", "application/json");
        updateRequest.setAdditionalHeader("Content-type", "application/json");

        Page updatePage = updateStateWebClient.getPage(updateRequest);

        String returnedUpdatedStateJson = updatePage.getWebResponse().getContentAsString();

        State returnedUpdatedState = gson.fromJson(returnedUpdatedStateJson, State.class);

        assertNotNull(stateReturned);
        assertNotNull(returnedUpdatedState);

        assertEquals(stateReturned.getStateTax(), returnedUpdatedState.getStateTax(), .0001d);

        // Verify Update Did Not Increase the Size of the Database
        WebClient sizeWebClient3 = new WebClient();
        sizeWebClient3.addRequestHeader("Accept", "application/json");

        Page sizePage3 = sizeWebClient3.getPage(sizeUrl.url());
        WebResponse sizeResponse3 = sizePage3.getWebResponse();
        assertEquals(sizeResponse3.getStatusCode(), 200);
        assertTrue("Response3 Length:" + sizeResponse3.getContentLength(), sizeResponse3.getContentLength() > 50);

        Integer afterUpdate = null;

        if (sizeResponse3.getContentType().equals("application/json")) {
            String json = sizeResponse3.getContentAsString();
            State[] statesAfterUpdate = gson.fromJson(json, State[].class);

            afterUpdate = statesAfterUpdate.length;

            Assert.assertNotNull(afterUpdate);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(afterUpdate);
        assertEquals(afterUpdate.intValue(), afterCreation.intValue());

        // Delete the Created State
        String stateIdString = stateReturned.getStateName();

        HttpUrl deleteUrl = getStateUrlBuilder()
                .addPathSegment(stateIdString)
                .build();

        WebClient deleteStateWebClient = new WebClient();

        WebRequest deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        Page deletePage = deleteStateWebClient.getPage(deleteRequest);

        String returnedDeleteStateJson = deletePage.getWebResponse().getContentAsString();

        State returnedDeleteState = gson.fromJson(returnedDeleteStateJson, State.class);

        assertEquals(returnedDeleteState, returnedUpdatedState);

        // Delete The Created State A Second Time
        deleteStateWebClient = new WebClient();

        deleteRequest = new WebRequest(deleteUrl.url(), HttpMethod.DELETE);

        deletePage = deleteStateWebClient.getPage(deleteRequest);

        returnedDeleteStateJson = deletePage.getWebResponse().getContentAsString();
        assertEquals(returnedDeleteStateJson, "");

        State returnedDeleteState2 = gson.fromJson(returnedDeleteStateJson, State.class);
        assertNull(returnedDeleteState2);

        assertNotEquals(returnedDeleteState, returnedDeleteState2);

        // Verify State Database Size Has Shrunk By One After Deletion
        WebClient sizeWebClient4 = new WebClient();
        sizeWebClient4.addRequestHeader("Accept", "application/json");

        Page sizePage4 = sizeWebClient4.getPage(sizeUrl.url());
        WebResponse sizeResponse4 = sizePage4.getWebResponse();
        assertEquals(sizeResponse4.getStatusCode(), 200);
        assertTrue("Size Response4: " + sizeResponse4.getContentLength(), sizeResponse4.getContentLength() > 50);

        Integer afterDeletion = null;

        if (sizeResponse4.getContentType().equals("application/json")) {
            String json = sizeResponse4.getContentAsString();
            State[] statesAfterDeletion = gson.fromJson(json, State[].class);
            afterDeletion = statesAfterDeletion.length;

            Assert.assertNotNull(afterDeletion);
        } else {
            fail("Should have been JSON.");
        }

        assertNotNull(beforeCreation);
        assertNotNull(afterDeletion);
        assertEquals(beforeCreation.intValue(), afterDeletion.intValue());
        assertEquals(afterCreation - 1, afterDeletion.intValue());

        // Try to Get Deleted State
        HttpUrl showUrl2 = getStateUrlBuilder()
                .addPathSegment(stateReturned.getStateName())
                .build();

        WebClient showStateWebClient2 = new WebClient();
        showStateWebClient2.addRequestHeader("Accept", "application/json");

        Page singleStatePage2 = showStateWebClient2.getPage(showUrl.url());
        WebResponse jsonSingleStateResponse2 = singleStatePage2.getWebResponse();
        assertEquals(jsonSingleStateResponse2.getStatusCode(), 200);
        assertTrue("Single State Response2: " + jsonSingleStateResponse2.getContentLength(), jsonSingleStateResponse2.getContentLength() < 50);

        String contentType = jsonSingleStateResponse2.getContentType();
        assertEquals(contentType, "");

        String contentString = jsonSingleStateResponse2.getContentAsString();

        assertEquals(contentString, "");

    }

    /**
     * Test of list method, of class StateDaoPostgresImpl.
     */
    @Test
    public void testList() throws IOException {
        System.out.println("list");

        State state = new State();
        state.setStateName("aa");
        state.setStateTax(ProductUtilities.roundToDecimalPlace(new Random().nextDouble(), 4));

        StateCommand commandState = StateCommand.buildCommandState(state);
        state = State.buildState(commandState);

        // Create Generated State
        HttpUrl createUrl = getStateUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient createStateWebClient = new WebClient();

        Gson gson = new GsonBuilder().create();
        String stateJson = gson.toJson(commandState);

        WebRequest createRequest = new WebRequest(createUrl.url(), HttpMethod.PUT);
        createRequest.setRequestBody(stateJson);
        createRequest.setAdditionalHeader("Accept", "application/json");
        createRequest.setAdditionalHeader("Content-type", "application/json");

        Page createdStatePage = createStateWebClient.getPage(createRequest);

        WebResponse createStateWebResponse = createdStatePage.getWebResponse();
        assertEquals(createStateWebResponse.getStatusCode(), 200);

        State createdState = null;

        if (createStateWebResponse.getContentType().equals("application/json")) {
            String json = createStateWebResponse.getContentAsString();
            createdState = gson.fromJson(json, State.class);

            assertNotNull(createdState);
        } else {
            fail("Should have been JSON.");
        }

        // Get The List Of States
        HttpUrl getListUrl = getStateUrlBuilder()
                .addPathSegment("")
                .build();

        WebClient getListWebClient = new WebClient();
        getListWebClient.addRequestHeader("Accept", "application/json");

        Page getListPage = getListWebClient.getPage(getListUrl.url());
        WebResponse getListWebResponse = getListPage.getWebResponse();
        assertEquals(getListWebResponse.getStatusCode(), 200);
        assertTrue(getListWebResponse.getContentLength() > 100);

        List<State> list = null;

        if (getListWebResponse.getContentType().equals("application/json")) {
            String json = getListWebResponse.getContentAsString();
            State[] states = gson.fromJson(json, State[].class);

            assertTrue(states.length > 0);

            list = Arrays.asList(states);
        } else {
            fail("Should have been JSON.");
        }

        Assert.assertNotNull(list);

        assertTrue(list.contains(createdState));
        assertNotEquals(state, createdState);

        assertNotNull(createdState);
        Integer createdStateId = createdState.getId();
        state.setId(createdStateId);

        assertNotNull(createdStateId);
        assertEquals("State: " + state.getId() + ", " + state.getStateName() + ", " + state.getState() + ", " + state.getStateTax() + "\n"
                + "State Created: " + createdState.getId() + ", " + createdState.getStateName() + ", " + createdState.getState() + ", " + createdState.getStateTax(),
                state, createdState);
    }

    private State stateBuilder(String name, double tax) {
        State state = new State();
        state.setStateName(name);
        state.setStateTax(tax);
        return state;
    }

    private HttpUrl.Builder getStateUrlBuilder() {
        return HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("state");
    }

    private void deleteTestState() {
        try {
            WebClient webClient = new WebClient();

            HttpUrl httpUrl = getStateUrlBuilder()
                    .addPathSegment("")
                    .build();

            HtmlPage htmlPage = webClient.getPage(httpUrl.url());

            HtmlAnchor deleteLink = htmlPage.getAnchorByHref("/state/delete/AA");
            deleteLink.click();
        } catch (IOException | com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
        }
    }
}
