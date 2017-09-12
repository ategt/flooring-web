package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\test\\resources\\test-SetupSimulatedProductionEnvironment.xml",
        "file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\main\\webapp\\WEB-INF\\spring-dispatcher-servlet.xml",
        "file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\main\\resources\\spring-persistence.xml"})
@WebAppConfiguration
public class AddressControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AddressDao addressDao;

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
    public void indexTest() throws Exception {
            mvc.perform(get("/address/"))
                    .andExpect(new ResultMatcher() {
                        @Override
                        public void match(MvcResult mvcResult) throws Exception {
                            MockHttpServletResponse response = mvcResult.getResponse();

                            assertEquals(200, response.getStatus());

                            ModelAndView modelAndView = mvcResult.getModelAndView();

                            Map<String, Object> model = modelAndView.getModel();

                            assertFalse(modelAndView.isEmpty());
                            assertTrue(modelAndView.hasView());

                            String viewName = modelAndView.getViewName();

                            assertEquals(viewName, "address\\index");

                            assertTrue(model.containsKey("addresses"));
                        }
                    })
            .andExpect(status().isOk());
    }

    @Test
    public void indexJsonFull() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/address/")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .param("results", Integer.toString(Integer.MAX_VALUE))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

                        MockHttpServletResponse response = mvcResult.getResponse();

                        assertEquals(200, response.getStatus());

                        String content = response.getContentAsString();

                        Gson gson = new GsonBuilder().create();

                        Address[] addresses = gson.fromJson(content, Address[].class);

                        assertEquals(addresses.length, addressDao.size(new AddressSearchRequest()));

    }

    @Test
    public void indexJson() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/address/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(200, response.getStatus());

        String content = response.getContentAsString();

        Gson gson = new GsonBuilder().create();

        Address[] addresses = gson.fromJson(content, Address[].class);

        assertNotNull(addresses);
        assertTrue(addresses.length > 1);
    }

    @Test
    public void create() throws Exception {
    }

    @Test
    public void guessWithAjax() throws Exception {
    }

    @Test
    public void listNamesWithAjax() throws Exception {
    }

    @Test
    public void altListNamesWithAjax() throws Exception {
    }

    @Test
    public void editSubmitWithAjax() throws Exception {
    }

    @Test
    public void deleteWithAjax() throws Exception {
    }

    @Test
    public void edit() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void search() throws Exception {
    }

    @Test
    public void search1() throws Exception {
    }

    @Test
    public void search2() throws Exception {
    }

    @Test
    public void search3() throws Exception {
    }

    @Test
    public void show() throws Exception {
    }

    @Test
    public void show1() throws Exception {
    }

    @Test
    public void size() throws Exception {
    }

}