package com.mycompany.flooringmasteryweb.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sun.rmi.server.LoaderHandler;

import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\test\\resources\\test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\main\\resources\\spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\main\\webapp\\WEB-INF\\spring-dispatcher-servlet.xml"})
})

public class MessageControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void showMessageFromNonsenseCodeTest() throws Exception {
        try {
            mvc.perform(get("/message/" + UUID.randomUUID().toString()))
                    .andExpect(new ResultMatcher() {
                        @Override
                        public void match(MvcResult mvcResult) throws Exception {
                            String responseString = mvcResult.getResponse()
                                    .getContentAsString();
                            fail(responseString);
                        }
                    });
        } catch (org.springframework.context.NoSuchMessageException ex) {
            assertTrue("No such message Found Exception Thrown.", true);
        }
    }

    @Test
    public void showMessageFromDefaultMessageCodeTest() throws Exception {
        String defaultMessageCode = webApplicationContext.getBean("defaultMessageCode", String.class);

        String defaultMessage = webApplicationContext.getMessage(defaultMessageCode, null, Locale.getDefault());

        assertEquals(defaultMessage.trim(), "No message is defined for this situation.");

        mvc.perform(get("/message/default-message"))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        String responseString = mvcResult.getResponse()
                                .getContentAsString();

                        assertEquals("Default message code response: " + responseString, responseString, defaultMessage);
                    }
                });
    }

    @Test
    public void showProductInvalidMessageTest() throws Exception {
        final String MESSAGE_EXPECTED = "If You Are Seeing This Message, Then We No Longer Carry That Product. ";

        mvc.perform(get("/message/product-invalid"))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        String responseString = mvcResult.getResponse()
                                .getContentAsString();

                        assertEquals("Default message code response: " + responseString, responseString, MESSAGE_EXPECTED);
                    }
                });
    }

}