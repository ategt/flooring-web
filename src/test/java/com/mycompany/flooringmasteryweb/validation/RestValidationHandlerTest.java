package com.mycompany.flooringmasteryweb.validation;

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

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.*;

public class RestValidationHandlerTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void simplerEquallyDesperateTest() {

        // This is me trying to make sense out of how the loader works.
        // ReloadableRsourceBundleMessageSource.java: 494
        //protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {

        final String PROPERTIES_SUFFIX = ".properties";
        final String XML_SUFFIX = ".xml";

        for (String filename : new String[]{
                "classpath:/WEB-INF/messages",
                "classpath:WEB-INF/messages",
                "classpath:messages",
                "/WEB-INF/messages",
                "WEB-INF/messages",
                "messages"
        }) {
            ResourceLoader resourceLoader = new DefaultResourceLoader();

            Resource resource = resourceLoader.getResource(filename + PROPERTIES_SUFFIX);
            if (!resource.exists()) {
                resource = resourceLoader.getResource(filename + XML_SUFFIX);
            }

            if (resource.exists()) {
                assertTrue("This is the desired outcome.", true);
            } else {
                //fail("This is what has been happening.");
            }
        }

        fail("This is what has been happening.");
    }

    @Test
    public void deperationResourceTest() {

        // This is me trying to make sense out of how the loader works.
        // ReloadableRsourceBundleMessageSource.java: 494
        //protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {

        long cacheMillis = -1;
        //PropertiesHolder propHolder = null;

        long refreshTimestamp = (cacheMillis < 0 ? -1 : System.currentTimeMillis());


        final String PROPERTIES_SUFFIX = ".properties";
        final String XML_SUFFIX = ".xml";

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Log logger = LogFactory.getLog(getClass());

        //logger.
        String filename = "messages";

        Resource resource = resourceLoader.getResource(filename + PROPERTIES_SUFFIX);
        if (!resource.exists()) {
            resource = resourceLoader.getResource(filename + XML_SUFFIX);
        }

        if (resource.exists()) {
            long fileTimestamp = -1;
            if (cacheMillis >= 0) {
                // Last-modified timestamp of file will just be read if caching with timeout.
                try {
                    fileTimestamp = resource.lastModified();
                    fail("Not sure what this does yet.");
//                    if (propHolder != null && propHolder.getFileTimestamp() == fileTimestamp) {
//                        if (logger.isDebugEnabled()) {
//                            logger.debug("Re-caching properties for filename [" + filename + "] - file hasn't been modified");
//                        }
//                        propHolder.setRefreshTimestamp(refreshTimestamp);
//                        return propHolder;
//                    }
                } catch (IOException ex) {
                    // Probably a class path resource: cache it forever.
                    if (logger.isDebugEnabled()) {
                        logger.debug(resource + " could not be resolved in the file system - assuming that it hasn't changed", ex);
                    }
                    fileTimestamp = -1;
                }
            }
            try {
                //Properties props = loadProperties(resource, filename);
                //propHolder = new ReloadableResourceBundleMessageSource.PropertiesHolder(props, fileTimestamp);
                fail("This has not been implemented yet.");
                throw new IOException();
            } catch (IOException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Could not parse properties file [" + resource.getFilename() + "]", ex);
                }
                // Empty holder representing "not valid".
                //propHolder = new ReloadableResourceBundleMessageSource.PropertiesHolder();
                fail("Execution should not reach this point.");
            }
        } else {
            // Resource does not exist.
            if (logger.isDebugEnabled()) {
                logger.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
            }
            // Empty holder representing "not found".
            //propHolder = new ReloadableResourceBundleMessageSource.PropertiesHolder();
            fail("Execution should not reach this point.");
        }
    }

    @Test
    public void messageFromBundleBeanTheHardWayTest() throws IOException {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        Properties props = new Properties();
        //props.
        Properties properties = PropertiesLoaderUtils.loadAllProperties("classpath:messages");
        Properties properties1 = PropertiesLoaderUtils.loadAllProperties("TestMessages.properties");
        Properties properties2 = PropertiesLoaderUtils.loadAllProperties("TestMessages");
        Properties properties3 = PropertiesLoaderUtils.loadAllProperties("silly");
        Properties properties4 = PropertiesLoaderUtils.loadAllProperties("silly.properties");

        Properties properties5 = PropertiesLoaderUtils.loadAllProperties("ValidationMessages");
        Properties properties6 = PropertiesLoaderUtils.loadAllProperties("ValidationMessages.properties");
        Properties properties7 = PropertiesLoaderUtils.loadAllProperties("/WEB-INF/ValidationMessages");
        Properties properties8 = PropertiesLoaderUtils.loadAllProperties("/WEB-INF/ValidationMessages.properties");
        Properties properties9 = PropertiesLoaderUtils.loadAllProperties("WEB-INF/ValidationMessages");
        Properties properties10 = PropertiesLoaderUtils.loadAllProperties("WEB-INF/ValidationMessages.properties");

//                 <value>ValidationMessages</value>
//                <value>ApplicationResources</value>
//                <value>messages</value>
//                <value>/WEB-INF/ValidationMessages</value>
//                <value>/WEB-INF/ApplicationResources</value>
//                <value>/WEB-INF/messages</value>

        //messageSource.setBasename("classpath:messages");
        messageSource.setBasename("/WEB-INF/messages");
        messageSource.setCacheSeconds(5);
        //messageSource.set

        ReloadableResourceBundleMessageSource messageSource2 = new ReloadableResourceBundleMessageSource();
        messageSource2.setBasename("TestMessages");
        messageSource2.setCacheSeconds(5);


        String messageBundleToString = messageSource.toString();

        Locale locale = LocaleContextHolder.getLocale();

        String goot = messageSource2.getMessage("error.test", null, locale);

        assertEquals(goot, "bill error");


        for (String testString : new String[]{
                "classpath:/resource/messages",
                "classpath:/WEB-INF/messages",
                "classpath:WEB-INF/messages",
                "classpath:messages",
                "/WEB-INF/messages",
                "WEB-INF/messages",
                "messages",
                "WEB-INF/messages"
        }) {

            try {
                ReloadableResourceBundleMessageSource messageSourceTest = new ReloadableResourceBundleMessageSource();

                messageSourceTest.setBasename(testString);
                messageSourceTest.setCacheSeconds(5);
                messageSourceTest.clearCache();
                messageSourceTest.clearCacheIncludingAncestors();

                String message = messageSourceTest.getMessage("error.test", null, locale);

                assertNotNull(message);

            } catch (org.springframework.context.NoSuchMessageException ex) {

                String trewq = ex.getMessage();
                ex.getCause();

            }

        }

        String mess0 = messageSource.getMessage("error.test", null, locale);
        String mess1 = messageSource.getMessage("error.test", null, Locale.US);
        String mess2 = messageSource.getMessage("error.test", null, null);
        String mess3 = messageSource.getMessage("error.test", null, Locale.getDefault());
        String mess4 = messageSource.getMessage("error.test", null, "Failed to Resolve message.", Locale.US);
        MessageSource messageSourceParent = messageSource.getParentMessageSource();

        assertEquals("That State Can Not Be Added To That Order.", mess4);
    }

    @Test
    public void messageFromBundleBeanTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("messageBundleTests.xml");

        ReloadableResourceBundleMessageSource messageBundle = ctx.getBean("messageSource", ReloadableResourceBundleMessageSource.class);

        String messageBundleToString = messageBundle.toString();

        Locale locale = LocaleContextHolder.getLocale();

        String mess0 = messageBundle.getMessage("product.custom.product", null, locale);
        String mess1 = messageBundle.getMessage("product.custom.product", null, Locale.US);
        String mess2 = messageBundle.getMessage("product.custom.product", null, null);
        String mess3 = messageBundle.getMessage("product.custom.product", null, Locale.getDefault());
        String mess4 = messageBundle.getMessage("product.custom.product", null, "Failed to Resolve message.", Locale.US);
        MessageSource messageSource = messageBundle.getParentMessageSource();

        assertEquals("That State Can Not Be Added To That Order.", mess4);
    }

    @Test
    public void messageFromBundleTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("messageBundleTests.xml");

        String message = ctx.getMessage("error.user.orderCommand.state", null, Locale.US);

        assertEquals("That State Can Not Be Added To That Order.", message);
    }

    public void processValidationErrors() throws Exception {
    }

}