package com.mycompany.flooringmasteryweb.conversion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Date;

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
    private FormatterRegistry registry;

    @Autowired
    private FormattingConversionService formattingConversionService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parse() throws Exception {

//        FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
//        factoryBean.setFormatters(Collections.emptySet());
//
//
//        FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean
//                = webApplicationContext.getBean("conversionService", FormattingConversionServiceFactoryBean.class);

//        FormattingConversionService formattingConversionService
//                = formattingConversionServiceFactoryBean.getObject();

        String jhgf = formattingConversionService.convert(new Date(), String.class);
        System.out.println(jhgf);

    }

    @Test
    public void print() throws Exception {
    }

}