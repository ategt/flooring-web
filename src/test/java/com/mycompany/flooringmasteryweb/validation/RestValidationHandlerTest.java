package com.mycompany.flooringmasteryweb.validation;

import com.google.common.base.Strings;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RestValidationHandlerTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void resolveMessageCorrectlyTest(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("RestValidationHandlerTestContext.xml");
        RestValidationHandler restValidationHandler = ctx.getBean("restValidationHandler", RestValidationHandler.class);

        OrderCommand orderCommand = new OrderCommand();
        orderCommand.setId(0);
        orderCommand.setArea(100.0d);
        orderCommand.setDate(new Date());
        orderCommand.setName("Validation Test - Test Order");

        orderCommand.setState("Invalid State");
        orderCommand.setProduct("Invalid Product");

        BindingResult bindingResult =
            new BeanPropertyBindingResult(orderCommand,
                    "orderCommand",
                    true,
                    256);

        bindingResult.rejectValue("state", "validation.orderCommand.state.invalid" );
        bindingResult.rejectValue("state", "state.doNotDoBusinessThere", "-The System Can Not Currently Handle Orders In That State. Please Call The Office To Place This Order.");
        bindingResult.rejectValue("state", "state.bad");

        bindingResult.rejectValue("product", "product.custom", "-We Do not Carry This Item.");
        bindingResult.rejectValue("product", "product.notFound", "-We Do not Carry That Item.");

        ValidationErrorContainer validationErrorContainer =
                restValidationHandler.processValidationErrors(new MethodArgumentNotValidException(null, bindingResult));

        List<ValidationError> validationErrorList = validationErrorContainer.getErrors();

        assertTrue(validationErrorList.stream()
                .noneMatch(validationError -> Strings.nullToEmpty(validationError.getMessage()).contains("-"))
        );
    }

    private ApplicationContext loadTheProductionContextWithTestDatabase() throws URISyntaxException {
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
        return ctx;
    }
}