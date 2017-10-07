package com.mycompany.flooringmasteryweb.validation;

import com.google.common.base.Strings;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
//import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class MessageBundleHandlerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
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

        ClassPathXmlApplicationContext testContext = new ClassPathXmlApplicationContext("test-SQLBase-applicationContext.xml");
        BasicDataSource basicDataSource = testContext.getBean("dataSource", org.apache.commons.dbcp.BasicDataSource.class);

        String testDatabaseUrl = basicDataSource.getUrl();

        String password = basicDataSource.getPassword();
        String userName = basicDataSource.getUsername();

        if (testDatabaseUrl.startsWith(JDBC_PORTION))
            testDatabaseUrl = testDatabaseUrl.substring(JDBC_PORTION.length());

        URI uri = new URI(testDatabaseUrl);

        String host = uri.getHost();

        String piecedTogetherUri = testDatabaseUrl.replace(host, userName + ":" + password + "@" + host);

        testContext.close();
        int numberOfDashes = 75;

        System.out.println("");
        System.out.println(Strings.repeat("-", numberOfDashes));
        System.out.println("");
        System.out.println("        Closed one context and switching to another.");
        System.out.println("");
        System.out.println(Strings.repeat("-", numberOfDashes));
        System.out.println("");

        System.setProperty("DATABASE_URL", piecedTogetherUri);

        org.springframework.context.support.ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource
                = webApplicationContext.getBean("messageSource", org.springframework.context.support.ReloadableResourceBundleMessageSource.class);

        Set<String> basenameSet = reloadableResourceBundleMessageSource.getBasenameSet();
        System.out.println("\n" + basenameSet.stream()
                .map(basename -> basename + "\n")
                .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString() + "\n");

        String launchDir = System.getProperty("user.dir");
        System.out.println("Launch Dir: " + launchDir);

        String message = webApplicationContext.getMessage("validation.orderCommand.state.null", null, Locale.US);

        assertEquals(ERROR_TEST, message);
    }

    @Test
    public void mockMvcMessageTest() throws
            InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {

        final String DATABASE_URL = "postgres://myself:post@localhost:5432/flooring_master";
        final String DATABASE_KEY = "DATABASE_TEST_URL";

        org.springframework.beans.factory.config.MethodInvokingFactoryBean mifb = new org.springframework.beans.factory.config.MethodInvokingFactoryBean();
        mifb.setTargetClass(java.lang.System.class);
        mifb.setTargetMethod("setProperty");
        mifb.setArguments(new Object[]{DATABASE_KEY, DATABASE_URL});

        String dbUrl = System.getProperty(DATABASE_KEY);

        assertTrue(Strings.isNullOrEmpty(dbUrl));

        mifb.prepare();
        mifb.invoke();

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);
    }

    @Test
    public void contextInvokationTest() {

        final String DATABASE_URL = "postgres://myself:post@localhost:5432/flooring_master";
        final String DATABASE_KEY = "DATABASE_URL";

        System.setProperty(DATABASE_KEY, UUID.randomUUID().toString());

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

    @Test
    public void sqlEnvironmentHandlingTest() throws SQLException {
        final String DATABASE_URL = "postgres://myself:post@localhost:5432/flooring_master_random_test";
        final String DATABASE_KEY = "DATABASE_URL";

        String dbUrl = System.getProperty(DATABASE_KEY);
        assertNotEquals(DATABASE_URL, dbUrl);

        System.setProperty(DATABASE_KEY, DATABASE_URL);

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);

        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("SQLBase-applicationContext.xml");

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);

        org.springframework.jdbc.core.JdbcTemplate jdbcTemplate = ctx.getBean("jdbcTemplate", org.springframework.jdbc.core.JdbcTemplate.class);

        assertNotNull(jdbcTemplate);

        DataSource dataSource = jdbcTemplate.getDataSource();

        assertNotNull(dataSource);

        assertTrue(dataSource instanceof BasicDataSource);
        BasicDataSource basicDataSource = (BasicDataSource) dataSource;

        String userName = basicDataSource.getUsername();
        String password = basicDataSource.getPassword();

        assertEquals("myself", userName);
        assertEquals("post", password);
    }

    @Test
    public void sqlProductionEnvironmentHandlingTest() throws SQLException {
        final String DATABASE_URL = "postgres://myself:post@localhost:5432/flooring_master";
        final String DATABASE_KEY = "DATABASE_URL";

        String dbUrl = System.getProperty(DATABASE_KEY);
        assertNotEquals(DATABASE_URL, dbUrl);

        System.setProperty(DATABASE_KEY, DATABASE_URL);

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);

        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("spring-persistence.xml");

        dbUrl = System.getProperty(DATABASE_KEY);
        assertEquals(DATABASE_URL, dbUrl);

        org.springframework.jdbc.core.JdbcTemplate jdbcTemplate = ctx.getBean("jdbcTemplate", org.springframework.jdbc.core.JdbcTemplate.class);

        assertNotNull(jdbcTemplate);

        DataSource dataSource = jdbcTemplate.getDataSource();

        assertNotNull(dataSource);

        assertTrue(dataSource instanceof BasicDataSource);
        BasicDataSource basicDataSource = (BasicDataSource) dataSource;

        String userName = basicDataSource.getUsername();
        String password = basicDataSource.getPassword();
        String url = basicDataSource.getUrl();

        assertEquals("myself", userName);
        assertEquals("post", password);

        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
    }
}