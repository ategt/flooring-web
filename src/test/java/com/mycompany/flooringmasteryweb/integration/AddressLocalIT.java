package com.mycompany.flooringmasteryweb.integration;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.htmlunit.WebRequestMatcher;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class AddressLocalIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private WebClient webClient;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
//        webClient = MockMvcWebClientBuilder.webAppContextSetup(webApplicationContext)
//                .contextPath("")
//                .build();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                //.dispatchOptions(true)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void localDeployTest() throws Exception {

        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
                .contextPath("/bill")
                .useMockMvcForHosts("localhost")
                .useMockMvc(new WebRequestMatcher() {
            @Override
            public boolean matches(WebRequest webRequest) {
                return true;
            }
        }).build();

//        webClient = new WebClient();
//        MockMvcWebConnection mockMvcWebConnection = new MockMvcWebConnection(mockMvc, webClient);
//        //MockMvcWebConnection mockMvcWebConnection = new MockMvcWebConnection(mockMvc, webClient, "/FlooringMasteryWeb");
//        //webClient.setWebConnection(new MockMvcWebConnection(mockMvc, "/FlooringMasteryWeb"));
//        webClient.setWebConnection(mockMvcWebConnection);

        //WebConnectionHtmlUnitDriver driver = new WebConnectionHtmlUnitDriver();

        //driver.

        //HtmlPage page = webClient.getPage("/address/");

        //MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/address/")).andReturn();

        //WebRequest webRequest = WebRequest

        webClient.addRequestHeader("Accept", "application/json");

        Page page = webClient.getPage("http://localhost/bill/address/");
        //HtmlPage page = webClient.getPage("http://localhost/bill/");
        //HtmlPage page = webClient.getPage("http://localhost/FlooringMasteryWeb/address/");

        Assert.assertFalse(page.isHtmlPage());

        WebResponse webResponse = page.getWebResponse();

        assertEquals(webResponse.getStatusCode(), 200);

        System.out.println("Content: " + page.getWebResponse().getContentAsString());

        URL url = page.getUrl();

        System.out.println("URL: " + url.toString());
//        String title = htmlPage.getTitleText();
//
//        String contentType = htmlPage.getContentType();
//        String htmlXml = htmlPage.getXmlVersion();
//
//        URL url = htmlPage.getBaseURL();

    }
}
