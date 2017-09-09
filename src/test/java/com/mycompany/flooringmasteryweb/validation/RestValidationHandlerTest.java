package com.mycompany.flooringmasteryweb.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

import static org.junit.Assert.*;

public class RestValidationHandlerTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void messageFromBundleBeanTest(){
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
    public void messageFromBundleTest(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("messageBundleTests.xml");

        String message = ctx.getMessage("error.user.orderCommand.state", null, Locale.US);

        assertEquals("That State Can Not Be Added To That Order.", message);
    }

    public void processValidationErrors() throws Exception {
    }

}