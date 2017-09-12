package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

import static org.junit.Assert.*;

public class MessageControllerIT {
    ApplicationContext ctx;
    URI uriToTest;

    public MessageControllerIT() {
        ctx = new ClassPathXmlApplicationContext("integrationTest-Context.xml");
    }

    @Before
    public void setUp() {
        uriToTest = ctx.getBean("baseUrlToTest", URI.class);
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void show() throws Exception {

        final String MESSAGE_CODE_TO_TEST = "default.message";
        final String MESSAGE_EXPECTED = "No message is defined for this situation.";

        URL showUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("message")
                .addPathSegment(MESSAGE_CODE_TO_TEST)
                .build()
                .url();

        WebClient showMessageWebClient = new WebClient();
        showMessageWebClient.addRequestHeader("Accept", "application/json");

        Page showMessagePage = showMessageWebClient.getPage(showUrl);
        WebResponse showMessageResponse = showMessagePage.getWebResponse();
        assertEquals(showMessageResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + showMessageResponse.getContentLength(), showMessageResponse.getContentLength() > 2);

        String messageResponded = null;

        if (showMessageResponse.getContentType().equals("application/json")) {
           messageResponded = showMessageResponse.getContentAsString();

            Assert.assertNotNull(messageResponded);
            assertFalse(Strings.isNullOrEmpty(messageResponded));
        } else {
            fail("Should have been JSON.");
        }

        assertEquals(messageResponded, MESSAGE_EXPECTED);
    }

    @Test
    public void showValidationMessage() throws Exception {

        final String MESSAGE_CODE_TO_TEST = "product.invalid";
        final String MESSAGE_EXPECTED = "If You Are Seeing This Message, Then We No Longer Carry That Product.";

        URL showUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("message")
                .addPathSegment(MESSAGE_CODE_TO_TEST)
                .build()
                .url();

        WebClient showMessageWebClient = new WebClient();
        showMessageWebClient.addRequestHeader("Accept", "application/json");

        Page showMessagePage = showMessageWebClient.getPage(showUrl);
        WebResponse showMessageResponse = showMessagePage.getWebResponse();
        assertEquals(showMessageResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + showMessageResponse.getContentLength(), showMessageResponse.getContentLength() > 2);

        String messageResponded = null;

        if (showMessageResponse.getContentType().equals("application/json")) {
            messageResponded = showMessageResponse.getContentAsString();

            Assert.assertNotNull(messageResponded);
            assertFalse(Strings.isNullOrEmpty(messageResponded));
        } else {
            fail("Should have been JSON.");
        }

        assertEquals(messageResponded, MESSAGE_EXPECTED);
    }

    @Test
    public void showDefaultMessage() throws Exception {

        final String MESSAGE_CODE_TO_TEST = UUID.randomUUID().toString();
        final String MESSAGE_EXPECTED = "No message is defined for this situation.";

        URL showUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("message")
                .addPathSegment(MESSAGE_CODE_TO_TEST)
                .build()
                .url();

        WebClient showMessageWebClient = new WebClient();
        showMessageWebClient.addRequestHeader("Accept", "application/json");

        Page showMessagePage = showMessageWebClient.getPage(showUrl);
        WebResponse showMessageResponse = showMessagePage.getWebResponse();
        assertEquals(showMessageResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + showMessageResponse.getContentLength(), showMessageResponse.getContentLength() > 2);

        String messageResponded = null;

        if (showMessageResponse.getContentType().equals("application/json")) {
            messageResponded = showMessageResponse.getContentAsString();

            Assert.assertNotNull(messageResponded);
            assertFalse(Strings.isNullOrEmpty(messageResponded));
        } else {
            fail("Should have been JSON.");
        }

        assertEquals(messageResponded, MESSAGE_EXPECTED);
    }
}