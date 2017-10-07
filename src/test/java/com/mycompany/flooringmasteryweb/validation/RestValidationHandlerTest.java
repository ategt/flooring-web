package com.mycompany.flooringmasteryweb.validation;

import com.google.common.base.Strings;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.utilities.TextUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class RestValidationHandlerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void resolveMessageCorrectlyTest() {
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

        bindingResult.rejectValue("state", "validation.orderCommand.state.invalid");
        bindingResult.rejectValue("state", "state.doNotDoBusinessThere", "-The System Can Not Currently Handle Orders In That State. Please Call The Office To Place This Order.");
        bindingResult.rejectValue("state", "state.bad");

        bindingResult.rejectValue("product", "product.custom", "-We Do not Carry This Item.");
        bindingResult.rejectValue("product", "product.notFound", "-We Do not Carry That Item.");

        org.springframework.context.support.ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource
                = ctx.getBean("messageSource", org.springframework.context.support.ReloadableResourceBundleMessageSource.class);

        reloadableResourceBundleMessageSource.setCacheSeconds(7);

        Set<String> basenameSet = reloadableResourceBundleMessageSource.getBasenameSet();
        System.out.println("\n" + basenameSet.stream()
                .map(basename -> basename + "\n")
                .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString() + "\n");

        ValidationErrorContainer validationErrorContainer =
                restValidationHandler.processValidationErrors(new MethodArgumentNotValidException(null, bindingResult));

        List<ValidationError> validationErrorList = validationErrorContainer.getErrors();

        assertTrue(validationErrorList.stream()
                .noneMatch(validationError -> Strings.nullToEmpty(validationError.getMessage()).contains("-"))
        );
    }

    @Test
    public void resolveMessageCorrectlyInProductionTest() throws URISyntaxException {
        ApplicationContext ctx = loadTheProductionContextWithTestDatabase();
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

        bindingResult.rejectValue("state", "validation.orderCommand.state.invalid");
        bindingResult.rejectValue("state", "state.doNotDoBusinessThere", "-The System Can Not Currently Handle Orders In That State. Please Call The Office To Place This Order.");
        bindingResult.rejectValue("state", "state.bad");

        bindingResult.rejectValue("product", "product.custom", "-We Do not Carry This Item.");
        bindingResult.rejectValue("product", "product.notFound", "-We Do not Carry That Item.");

        ValidationErrorContainer validationErrorContainer =
                restValidationHandler.processValidationErrors(new MethodArgumentNotValidException(null, bindingResult));

        List<ValidationError> validationErrorList = validationErrorContainer.getErrors();

        assertTrue("No error messages are supposed to contain dashes(-):\n" +
                        validationErrorList.stream().map(
                                validationError -> validationError.getFieldName() + ":" + validationError.getMessage() + "\n")
                                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                                .toString() + "\n\n",
                validationErrorList.stream()
                        .noneMatch(validationError -> Strings.nullToEmpty(validationError.getMessage()).contains("-"))
        );
    }

    @Test
    public void resolveImplossibleMessageCorrectlyInProductionTest() throws URISyntaxException {
        ApplicationContext ctx = loadTheProductionContextWithTestDatabase();
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

        bindingResult.rejectValue("state", "validation.orderCommand.state.invalid");
        bindingResult.rejectValue("state", "state.doNotDoBusinessThere", "-The System Can Not Currently Handle Orders In That State. Please Call The Office To Place This Order.");
        bindingResult.rejectValue("state", "state.bad");
        bindingResult.rejectValue("area", "valication.this.is.not.meant.to.be.a.real.thing");

        bindingResult.rejectValue("product", "product.custom", "-We Do not Carry This Item.");
        bindingResult.rejectValue("product", "product.notFound", "-We Do not Carry That Item.");

        ValidationErrorContainer validationErrorContainer =
                restValidationHandler.processValidationErrors(new MethodArgumentNotValidException(null, bindingResult));

        List<ValidationError> validationErrorList = validationErrorContainer.getErrors();

        assertTrue("No error messages are supposed to contain dashes(-):\n" +
                        validationErrorList.stream().map(
                                validationError -> validationError.getFieldName() + ":" + validationError.getMessage() + "\n")
                                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                                .toString() + "\n\n",
                validationErrorList.stream()
                        .noneMatch(validationError -> Strings.nullToEmpty(validationError.getMessage()).contains("-"))
        );
    }

    @Test
    public void resolveDefaultMessageInProductionTest() throws URISyntaxException {
        ApplicationContext ctx = loadTheProductionContextWithTestDatabase();

        String defaultMessageCode = ctx.getBean("defaultMessageCode", String.class);
        String resultMessage = ctx.getMessage(defaultMessageCode, null, Locale.getDefault());

        assertNotNull(resultMessage);
        assertFalse(Strings.isNullOrEmpty(resultMessage));
    }

    @Test
    public void messageTest() {
        Object[] args = null;
        String defaultMessage = "{product.notFound}";
        defaultMessage = TextUtilities.sanitizeParenthesis(defaultMessage);
        MessageFormat form = new MessageFormat(defaultMessage);
        String resultMessage = form.format(args);

        assertEquals(resultMessage, "product.notFound");
    }

    private ApplicationContext loadTheProductionContextWithTestDatabase() {
        return webApplicationContext;
    }
}