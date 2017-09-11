package com.mycompany.flooringmasteryweb.validation;

import com.google.common.base.Strings;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

    @Test
    public void mockMvcMessageTest() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {

        final String DATABASE_URL = "postgres://myself:post@localhost:5432/flooring_master";
        final String DATABASE_KEY = "DATABASE_URL";

        org.springframework.beans.factory.config.MethodInvokingFactoryBean mifb = new org.springframework.beans.factory.config.MethodInvokingFactoryBean();
        mifb.setTargetClass(java.lang.System.class);
        mifb.setTargetMethod("setProperty");
        mifb.setArguments(new Object[]{DATABASE_KEY,DATABASE_URL});

        String dbUrl = System.getProperty(DATABASE_KEY);

        assertTrue(Strings.isNullOrEmpty(dbUrl));

        mifb.prepare();
        mifb.invoke();

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);
    }

    @Test
    public void contextInvokationTest(){

        final String DATABASE_URL = "postgres://myself:post@localhost:5432/flooring_master";
        final String DATABASE_KEY = "DATABASE_URL";

        //System.setProperty(DATABASE_KEY, DATABASE_URL);

        String dbUrl = System.getProperty(DATABASE_KEY);
        assertNotEquals(DATABASE_URL, dbUrl);

        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("test-SetupSimulatedProductionEnvironment.xml");

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);

        String dbString = ctx.getBean("dbStr", String.class);

        assertNotNull(dbString);

        ctx.close();

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);

        ConfigurableApplicationContext nctx = new ClassPathXmlApplicationContext("test-SimulatedProductionEnvironment.xml");

        URI uri = nctx.getBean("dbUrl", URI.class);

        assertNotNull(uri);

        String uriString = uri.toString();
        assertEquals(uriString, DATABASE_URL);
    }

    //test-SetupSimulatedProductionEnvironment.xml",
}