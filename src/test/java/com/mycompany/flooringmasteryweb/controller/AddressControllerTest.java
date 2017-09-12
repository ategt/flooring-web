package com.mycompany.flooringmasteryweb.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Mock
    private AddressDao addressDao;

    @InjectMocks
    private AddressController addressController;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        addressController.setApplicationContext(webApplicationContext);

        mvc = MockMvcBuilders
                .standaloneSetup(addressController)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void indexTest() throws Exception {

        List<Address> addressList = new ArrayList<>();
        for (int i = 0 ; i < 20 ; i++) {
            addressList.add(AddressTest.addressGenerator());
        }

        ResultProperties[] lastResultPropertiesUsed = new ResultProperties[1];

        Mockito.when(addressDao.list(getResultProperties(lastResultPropertiesUsed))).thenReturn(addressList);
        Mockito.when(addressDao.getAddressesSortedByParameter(getResultProperties(lastResultPropertiesUsed))).thenReturn(addressList);

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

        assertTrue(model.containsKey("addresses"));

        List<Address> addressListModel = (List<Address>) model.get("addresses");

        assertEquals(addressListModel.size(), addressList.size());

        for (Address address : addressList){
            assertTrue(addressListModel.contains(address));
        }
    }

    @Test
    public void indexTestWithResultProperties() throws Exception {

        List<Address> addressList = new ArrayList<>();
        for (int i = 0 ; i < 20 ; i++) {
            addressList.add(AddressTest.addressGenerator());
        }

        ResultProperties[] lastResultPropertiesUsed = new ResultProperties[1];

        Mockito.when(addressDao.list(getResultProperties(lastResultPropertiesUsed))).thenReturn(addressList);
        Mockito.when(addressDao.getAddressesSortedByParameter(getResultProperties(lastResultPropertiesUsed))).thenReturn(addressList);

        MvcResult mvcResult = mvc.perform(get("/address/")
        .param("page", "20")
        .param("results", "90")
        .param("sort_by", "last_name"))
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

        assertTrue(model.containsKey("addresses"));

        List<Address> addressListModel = (List<Address>) model.get("addresses");

        assertEquals(addressListModel.size(), addressList.size());

        for (Address address : addressList){
            assertTrue(addressListModel.contains(address));
        }

        ResultProperties resultProperties = lastResultPropertiesUsed[0];

        AddressSortByEnum sortByEnum = resultProperties.getSortByEnum();

        assertEquals(sortByEnum, AddressSortByEnum.SORT_BY_LAST_NAME);

        assertEquals(Integer.valueOf(90), resultProperties.getResultsPerPage());
        assertEquals(Integer.valueOf(20), resultProperties.getPageNumber());
    }

    private ResultProperties getResultProperties(ResultProperties[] lastResultPropertiesUsed) {
        return ArgumentMatchers.argThat(new ArgumentMatcher<ResultProperties>() {
            @Override
            public boolean matches(ResultProperties resultProperties) {
                lastResultPropertiesUsed[0] = resultProperties;
                return true;
            }
        });
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
    public void create() throws Exception {

        Address address = AddressTest.addressGenerator();

        Mockito.when(addressDao.create(ArgumentMatchers.any(Address.class))).thenReturn(address);

        Gson gson = new GsonBuilder().create();

        String addressJson = gson.toJson(address);

        MvcResult mvcResult = mvc.perform(post("/address/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addressJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Address addressReturned = gson.fromJson(responseContent, Address.class);

        assertNotNull(addressReturned);

        Integer id = addressReturned.getId();

        assertNull(id);

        assertEquals(address, addressReturned);
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