package com.mycompany.flooringmasteryweb.integration;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuditIT {

    ApplicationContext ctx;
    URI uriToTest;

    public AuditIT() {
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
    public void auditTest() throws IOException {
        URL showUrl = HttpUrl.get(uriToTest).newBuilder()
                .addPathSegment("audits")
                .addPathSegment("")
                .build()
                .url();

        WebClient webClient = new WebClient();
        HtmlPage showMessagePage = webClient.getPage(showUrl);
        WebResponse showMessageResponse = showMessagePage.getWebResponse();
        assertEquals(showMessageResponse.getStatusCode(), 200);
        assertTrue("Content Length: " + showMessageResponse.getContentLength(), showMessageResponse.getContentLength() > 2);

        DomNodeList<DomElement> tableRows = showMessagePage.getElementsByTagName("tr");
        int initialSize = tableRows.size();

        assertTrue(initialSize > 0);

        webClient.waitForBackgroundJavaScript(10000);

        final String SCROLL_DOWN_JS = "window.scrollBy(0, window.innerHeight * 4)";
        showMessagePage.executeJavaScript(SCROLL_DOWN_JS);

        //webClient.waitForBackgroundJavaScript(4000);

        tableRows = showMessagePage.getElementsByTagName("tr");
        int firstScrollSize = tableRows.size();

        assertTrue(firstScrollSize > initialSize);

        showMessagePage.executeJavaScript(SCROLL_DOWN_JS);
        tableRows = showMessagePage.getElementsByTagName("tr");

        int secondScroll = tableRows.size();

        assertTrue(secondScroll > firstScrollSize);

        assertEquals(firstScrollSize - initialSize, secondScroll - firstScrollSize);
    }
}