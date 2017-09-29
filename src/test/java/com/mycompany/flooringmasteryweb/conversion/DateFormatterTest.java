package com.mycompany.flooringmasteryweb.conversion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class DateFormatterTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FormattingConversionService formattingConversionService;

    @Autowired
    private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;
    //
//    @Autowired
//    private AcceptHeaderLocaleResolver acceptHeaderLocaleResolver;

//    @Autowired
//    private  SessionLocaleResolver sessionLocaleResolver;

    @Autowired
    private LocaleResolver localeResolver;

    @Test
    public void parse() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1984, 5, 8);

        String formattedDateString = formattingConversionService.convert(calendar.getTime(), String.class);
        assertEquals("06-08-1984", formattedDateString);
    }

    @Test
    public void print() throws Exception {


        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        Locale frenchLocale = Locale.CANADA_FRENCH;
//
        mockRequest.addPreferredLocale(frenchLocale);
//
//        webApplicationContext.getServletContext().
//
//        LocaleResolver resolver = RequestContextUtils.getLocaleResolver(mockRequest);
//        System.out.println(mockRequest.getLocale().toString());
//        System.out.println(resolver.resolveLocale(mockRequest).toString());
//        //assertTrue(!mockRequest.getLocale().equals(resolver.resolveLocale(mockRequest)));

        //new org.springframework.web.servlet.i18n.LocaleChangeInterceptor().

        Locale locale = localeResolver.resolveLocale(mockRequest); //.setLocale();
        //setDefaultLocale(Locale.CHINESE);
//        new SessionLocaleResolver().
//        webApplicationContext.

        LocaleContextHolder.setLocale(new Locale("test", "TEST", "FORMAT"));

        reloadableResourceBundleMessageSource.setBasenames("classpath:messages",
                "WEB-INF/classes/messages",
                "messages");
        //reloadableResourceBundleMessageSource.addBasenames();
        //reloadableResourceBundleMessageSource.addBasenames();
        //reloadableResourceBundleMessageSource.getBasenameSet();

        //reloadableResourceBundleMessageSource.s`

        Date date = formattingConversionService.convert("06-08-1984", Date.class);
        String dateString = date.toString();
        long dateLong = date.getTime();
        assertEquals(455515200000l, dateLong);
        assertTrue(dateString.matches(".*Fri.*Jun.*8.*1984.*"));



//        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
//
//
//
//
//
//        mockRequest.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
//
//        String language = "zh_CN";
//
//        LoginPostController loginPostControllerTest = new LoginPostController();
//
//        loginPostControllerTest.localize(mockRequest, mockResponse, language);
//        System.out.println(mockRequest.getLocale().toString());
//    }
//
//
//
//        new org.springframework.web.servlet.i18n.CookieLocaleResolver().setLocale();
//
//        formattingConversionService.
//
//        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
//        localeResolver.setLocale(request, response, StringUtils.parseLocaleString("fr"));
//
//
//        org.springframework.web.servlet.i18n.LocaleChangeInterceptor localeChangeInterceptor
//                = new org.springframework.web.servlet.i18n.LocaleChangeInterceptor();
//
//        localeChangeInterceptor.
//
//        org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping
//
//
//        acceptHeaderLocaleResolver.

    }
}