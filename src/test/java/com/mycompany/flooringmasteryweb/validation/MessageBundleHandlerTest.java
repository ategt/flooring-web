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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
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
}