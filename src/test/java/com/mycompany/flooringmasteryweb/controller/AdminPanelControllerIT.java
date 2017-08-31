/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import java.util.Random;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ATeg
 */
public class AdminPanelControllerIT {

    ApplicationContext ctx;
    URI uriToTest;

    Integer addressCountFromIndex = null;

    public AdminPanelControllerIT() {
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

        HttpUrl httpUrl = getAdminUrlBuilder()
                .addPathSegment("search")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Address Book");
    }

    /**
     * Test of blank method, of class AdminPanelController.
     */
    @Test
    public void testBlank() throws IOException {
        System.out.println("blank");

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getAdminUrlBuilder()
                .addPathSegment("")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
    }

    /**
     * Test of editProduct method, of class AdminPanelController.
     */
    @Test
    public void testEditProduct() throws IOException {
        System.out.println("editProduct");

        Random random = new Random();
        
        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getAdminUrlBuilder()
                .addPathSegment("")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        DomElement stateTable = htmlPage.getElementById("state-table");
        DomNodeList<HtmlElement> stateTableRows = stateTable.getElementsByTagName("tr");
        HtmlElement randomStateRow = stateTableRows.get(random.nextInt(stateTableRows.size()));
        
        randomStateRow.getElementsByTagName("a")

        webClient = new WebClient();

        httpUrl = getAdminUrlBuilder()
                .addPathSegment("editProduct")
                .addPathSegment()
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
    }

    /**
     * Test of edit method, of class AdminPanelController.
     */
    @Test
    public void testEdit() throws IOException {
        System.out.println("edit");
        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getAdminUrlBuilder()
                .addPathSegment("")
                .build();

        HtmlPage htmlPage = webClient.getPage(httpUrl.url());

        String title = htmlPage.getTitleText();
        assertEquals(title, "Flooring Master");
    }

    private HttpUrl.Builder getAdminUrlBuilder() {
        return HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("admin");
    }
}
