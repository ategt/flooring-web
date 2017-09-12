package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockJspWriter;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
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

//    @Mock
    @Autowired
    private AddressDao addressDao;

//    @InjectMocks
//    private AddressController addressController;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {

        //MockitoAnnotations.initMocks(this);
        //Mockito.when(addressDao.list(ArgumentMatchers.any(ResultProperties.class))).thenReturn(new ArrayList<Address>());

        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

//        mvc = MockMvcBuilders
//                .standaloneSetup(addressController)
//                .build();

        //Mock
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
    }

    @Test
    @Ignore
    public void localDeployTest() throws IOException {

        //WebClient webClient = new WebClient();
        //new MockJspWriter()

        MockMvcWebClientBuilder mockMvcWebClientBuilder = MockMvcWebClientBuilder.mockMvcSetup(mvc);

        WebClient webClient = mockMvcWebClientBuilder.build();

        webClient.setWebConnection(new MockMvcWebConnection(mvc));

        HtmlPage page = webClient.getPage("http://localhost/FlooringMasteryWeb/");

        assertTrue(page.isHtmlPage());

        HtmlPage htmlPage = (HtmlPage) page;

        String title = htmlPage.getTitleText();

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
    @Ignore
    public void tryToLoadAJsp() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/jsp/");
        viewResolver.setSuffix(".jsp");

        AddressController addressController = new AddressController(addressDao);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(addressController)
                .setViewResolvers(viewResolver)
                .build();

        WebClient webClient = new WebClient();
        webClient.setWebConnection(new MockMvcWebConnection(mockMvc));

        HtmlPage page = webClient.getPage("http://localhost/FlooringMasteryWeb/");

        assertTrue(page.isHtmlPage());

        HtmlPage htmlPage = (HtmlPage) page;

        String title = htmlPage.getTitleText();

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

        Address address = AddressTest.addressGenerator();

        AddressDao addressDao = Mockito.mock(AddressDao.class);

        Mockito.when(addressDao.get(5)).thenReturn(address);

        Mockito.when(addressDao.get(ArgumentMatchers.anyInt())).thenReturn(address);

        Mockito.when(addressDao.create(address)).thenReturn(address);

        Mockito.when(addressDao.create(ArgumentMatchers.any())).thenReturn(address);

        Mockito.when(addressDao.create(address)).thenReturn(address);

        mvc.perform(post("/address/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Patrick\",\"lastName\":\"Toner\",\"company\":\"SWG\",\"streetNumber\":\"1\",\"streetName\":\"AGBA Rd\",\"city\":\"Akron\",\"state\":\"Oh\",\"zip\":\"44687\",\"id\":0}")
        )
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        MockHttpServletResponse response = mvcResult.getResponse();

                        assertEquals(200, response.getStatus());

                        String responseContent = mvcResult.getResponse().getContentAsString();


                    }
                });
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