package com.mycompany.flooringmasteryweb.conversion;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class DateFormatterTest {

    @Autowired
    private FormattingConversionService formattingConversionService;

    @Autowired
    private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

    @Before
    public void setup(){
        LocaleContextHolder.setLocale(new Locale("test", "TEST", "FORMAT"));
        reloadableResourceBundleMessageSource.setBasename("classpath:messages");
    }

    @Test
    public void parse() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1984, 5, 8);

        String formattedDateString = formattingConversionService.convert(calendar.getTime(), String.class);
        assertEquals("06-08-1984", formattedDateString);
    }

    @Test
    public void print() throws Exception {
        Date date = formattingConversionService.convert("06-08-1984", Date.class);
        String dateString = date.toString();
        long dateLong = date.getTime();
        assertEquals(455515200000l, dateLong);
        assertTrue(dateString.matches(".*Fri.*Jun.*8.*1984.*"));
    }
}