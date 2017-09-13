package com.mycompany.flooringmasteryweb.validation;

import com.google.common.base.Strings;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

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

        System.out.println("");
        System.out.println(Strings.repeat("-", 25));
        System.out.println("");
        System.out.println("        Closed one context and opening another.");
        System.out.println("");
        System.out.println(Strings.repeat("-", 25));
        System.out.println("");


        File springDispatcherServlet = new File("src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml");
        File springPersistance = new File("src/main/resources/spring-persistence.xml");

        String springDispatcherServletPath = springDispatcherServlet.getAbsolutePath();

        boolean afd = springDispatcherServlet.exists();

        File fadf = new File("");

        System.setProperty("DATABASE_URL", piecedTogetherUri);

        ApplicationContext webApplicationContext = new ClassPathXmlApplicationContext(
                new String[]{"file:" + springPersistance.getAbsolutePath(),
                "file:" + springDispatcherServletPath});

        ReloadableResourceBundleMessageSource rewa = webApplicationContext.getBean("messageSource", ReloadableResourceBundleMessageSource.class);

        String asfdasrewr = rewa.getMessage("validation.orderCommand.state.null", null, Locale.US);

        String message = webApplicationContext.getMessage("validation.orderCommand.state.null", null, Locale.US);

        assertEquals(ERROR_TEST, message);
    }

    private void afdafd(){
        //servletContext.get

        MockServletContext mockServletContext =
            new MockServletContext("/WEB-INF/spring-dispatcher-servlet.xml classpath:spring-persistence.xml");

        //mockServletContext.mes

        //mockServletContext.



        //DispatcherServlet dispatcherServlet = new DispatcherServlet(); //  new ContextConfigurationAttributes()

        //dispatcherServlet.
        //new org.springframework.web.SpringServletContainerInitializer().onStartup();

        XmlWebApplicationContext ctx = new XmlWebApplicationContext();

        ctx.setServletContext(mockServletContext);
//        ctx.refresh();
        //new DefaultResourceLoader().

        //WebApplicationContexthtx = WebApplicationContextUtils.

        //ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-persistence.xml");

        //ApplicationContext servletContext = new ClassPathXmlApplicationContext(new String[]{"/WEB-INF/spring-dispatcher-servlet.xml",
          //      "classpath:spring-persistence.xml"});
        //file:/C:/Users/ATeg/Documents/_repos/flooringmasteryweb/target/tomcat.8080/webapps/expanded/META-INF/context.xml
        //new SpringServletContainerInitializer()

        //ctx.setServletContext(servletContext);

//        new ServletContext()
//
//        ctx.setServletContext();
//        ctx.refresh();

        String message = ctx.getMessage("validation.orderCommand.state.null", null, Locale.US);

        //assertEquals(ERROR_TEST, message);
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