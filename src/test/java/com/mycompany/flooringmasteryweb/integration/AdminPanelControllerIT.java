/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.integration;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;

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

        WebClient webClient = new WebClient();

        HttpUrl httpUrl = getAdminUrlBuilder()
                .addPathSegment("editProduct")
                .addPathSegment("Tar")
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
                .addPathSegment("editState")
                .addPathSegment("AA")
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
