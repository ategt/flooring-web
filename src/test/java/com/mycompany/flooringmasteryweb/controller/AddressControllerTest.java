package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressTest;
import com.mycompany.flooringmasteryweb.dto.ResultProperties;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\test\\resources\\test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\main\\resources\\spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:C:\\Users\\ATeg\\Documents\\_repos\\flooringmasteryweb\\src\\main\\webapp\\WEB-INF\\spring-dispatcher-servlet.xml"})
})
public class AddressControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private InternalResourceViewResolver viewResolver;

    private MockMvc mvc;

    private List<Address> addressList;

    @Before
    public void setUp() throws Exception {
        addressList = Arrays.asList(new Address[]{AddressTest.addressGenerator(),
                                                    AddressTest.addressGenerator(),
                                                    AddressTest.addressGenerator()
                                    });

        Mockito.when(addressDao.list(ArgumentMatchers.argThat(new ArgumentMatcher<ResultProperties>() {
            @Override
            public boolean matches(ResultProperties resultProperties) {
                return false;
            }
        }))).thenReturn(addressList);

        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void indexTest() throws Exception {
            MvcResult mvcResult = mvc.perform(get("/address/"))
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
            .andExpect(status().isOk())
            .andReturn();

            Map<String, Object> model = mvcResult.getModelAndView().getModel();

            assertEquals(model.size(), addressList.size());
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

    private class CustomCharWriter extends HttpServletResponseWrapper{

        private final CharArrayWriter charArrayWriter = new CharArrayWriter();
        public CustomCharWriter(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(charArrayWriter);
        }
        public String getOutput(){
            return charArrayWriter.toString();
        }
    }

    @Test
    public void adf() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/jsp/");
        viewResolver.setSuffix(".jsp");

        AddressController addressController = new AddressController(addressDao);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(addressController)
                .setViewResolvers(viewResolver)
                .build();

        MvcResult mvcResult = mockMvc.perform(get("/address/")
                .param("results", "20")
                .param("page", "0")
            )
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(200, response.getStatus());

        ModelAndView modelAndView = mvcResult.getModelAndView();

        assertFalse(modelAndView.isEmpty());
        assertTrue(modelAndView.hasView());

        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();

        String forward = response.getForwardedUrl();
        String inc = response.getIncludedUrl();
        String redir = response.getRedirectedUrl();

        //CustomCharWriter customCharWriter = new CustomCharWriter(response);

        MockHttpServletRequest mockHttpServletRequest = mvcResult.getRequest();
        //String template = mockHttpServletRequest.getParameter("tmpl");
        //RequestDispatcher requestDispatcher = mockHttpServletRequest.getRequestDispatcher(template);
        //requestDispatcher.forward(mockHttpServletRequest, response);

        //String output = customCharWriter.getOutput();

        PrintWriter printWriter = response.getWriter();
        String pasdf = printWriter.toString();

        ServletOutputStream servletOutputStream = response.getOutputStream();
        String asf = servletOutputStream.toString();

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            private final StringWriter sw = new StringWriter();

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(sw);
            }

            @Override
            public String toString() {
                return sw.toString();
            }
        };
        mockHttpServletRequest.getRequestDispatcher("test.jsp").include(mockHttpServletRequest, responseWrapper);
        String content = responseWrapper.toString();
        System.out.println("Output : " + content);
        response.getWriter().write(content);

        //servletOutputStream.

        //new org.apache.commons.io.output.ByteArrayOutputStream()

        //new ByteArrayOutputStream(servletOutputStream);

        //servletOutputStream.write();

        //HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
        //responseWrapper.getO
        //responseWrapper.get

        MvcResult resultgb = mockMvc.perform(get(forward))
                .andReturn();



        //mvcResult.

        MockHttpServletResponse response2 = resultgb.getResponse();

        assertEquals(200, response2.getStatus());

        ModelAndView modelAndView2 = resultgb.getModelAndView();

        assertFalse(modelAndView2.isEmpty());
        assertTrue(modelAndView2.hasView());

        String viewName2 = modelAndView2.getViewName();
        Map<String, Object> mode2l = modelAndView2.getModel();

        String forward2 = response2.getForwardedUrl();
        String inc2 = response2.getIncludedUrl();
        String redir2 = response2.getRedirectedUrl();

    }


    @Test
    public void create() throws Exception {

        mvc.perform(post("/address/")
                    .content("sadf=bill")
        )
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
                .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().hasNoErrors());
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