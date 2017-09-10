package com.mycompany.flooringmasteryweb.validation;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import static org.junit.Assert.*;

public class MessageBundleHandlerTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void messageFromBundleTest() {
        final String ERROR_TEST = "Official Test Error Message.";
        ApplicationContext ctx = new ClassPathXmlApplicationContext("messageBundleTests.xml");

        String message = ctx.getMessage("error.test", null, Locale.US);

        assertEquals(ERROR_TEST, message);
    }

    @Test
    public void messageFromBundleWithArgumentsTest() {
        final String ARG_1 = "Bill";
        final String ARG_2 = Integer.toString(new Random().nextInt());

        final String ERROR_TEST = "The First Input Was: " + ARG_1 + ", and the Second Was: " + ARG_2 + ".";
        final String ERROR_TEST_2 = "The First Input Was: " + ARG_1 + ", and the Second Was: ";

        ApplicationContext ctx = new ClassPathXmlApplicationContext("messageBundleTests.xml");

        String message = ctx.getMessage("error.args.test", new String[]{ARG_1, ARG_2}, Locale.US);

        assertNotNull(message);
        assertTrue(message.contains(ARG_1));
        assertTrue(message.contains(ARG_2));
        assertTrue(message.trim().length() > 5);

        assertEquals(ERROR_TEST, message);
        assertTrue(message.startsWith(ERROR_TEST_2));
    }

    @Test
    public void messageFromProductionBundleTest() throws SQLException, MalformedURLException, URISyntaxException {
        final String ERROR_TEST = "You Must Include A State For This Order";
        final String JDBC_PORTION = "jdbc:";

        ApplicationContext testContext = new ClassPathXmlApplicationContext("test-SQLBase-applicationContext.xml");
        BasicDataSource basicDataSource = testContext.getBean("dataSource", org.apache.commons.dbcp.BasicDataSource.class);

        String testDatabaseUrl = basicDataSource.getUrl();

        String password = basicDataSource.getPassword();
        String userName = basicDataSource.getUsername();

        if (testDatabaseUrl.startsWith(JDBC_PORTION))
            testDatabaseUrl = testDatabaseUrl.substring(JDBC_PORTION.length());

        URI uri = new URI(testDatabaseUrl);

        String host = uri.getHost();

        String piecedTogetherUri = testDatabaseUrl.replace(host, userName + ":" + password + "@" + host);

        System.setProperty("DATABASE_URL", piecedTogetherUri);

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-persistence.xml");

        String message = ctx.getMessage("validation.orderCommand.state.null", null, Locale.US);

        assertEquals(ERROR_TEST, message);
    }
}