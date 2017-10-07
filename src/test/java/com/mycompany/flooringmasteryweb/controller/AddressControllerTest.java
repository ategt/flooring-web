package com.mycompany.flooringmasteryweb.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.*;
import com.mycompany.flooringmasteryweb.modelBinding.AddressResultSegmentResolver;
import org.junit.After;
import org.junit.Before;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class AddressControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private AddressDao mockAddressDao;

    @InjectMocks
    private AddressController addressController;

    private MockMvc mockMvc;
    private Random random = new Random();
    private AddressResultSegment[] lastAddressResultSegmentUsed = null;
    private List<Address> addressList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        addressList = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 10; i++) {
            addressList.add(AddressTest.addressGenerator());
        }

        lastAddressResultSegmentUsed = new AddressResultSegment[1];

        Mockito.when(mockAddressDao.list(getResultProperties(lastAddressResultSegmentUsed))).thenReturn(addressList);

        addressController.setApplicationContext(webApplicationContext);
        AddressResultSegmentResolver addressResultSegmentResolver = new AddressResultSegmentResolver();
        addressResultSegmentResolver.setApplicationContext(webApplicationContext);

        mockMvc = MockMvcBuilders
                .standaloneSetup(addressController)
                .setCustomArgumentResolvers(addressResultSegmentResolver)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void indexTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/address/"))
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

        for (Address address : addressList) {
            assertTrue(addressListModel.contains(address));
        }
    }

    @Test
    public void indexTestWithResultProperties() throws Exception {

        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            addressList.add(AddressTest.addressGenerator());
        }

        AddressResultSegment[] lastAddressResultSegmentUsed = new AddressResultSegment[1];

        Mockito.when(mockAddressDao.list(getResultProperties(lastAddressResultSegmentUsed))).thenReturn(addressList);

        MvcResult mvcResult = mockMvc.perform(get("/address/")
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

        for (Address address : addressList) {
            assertTrue(addressListModel.contains(address));
        }

        AddressResultSegment addressResultSegment = lastAddressResultSegmentUsed[0];

        AddressSortByEnum sortByEnum = addressResultSegment.getSortByEnum();

        assertEquals(sortByEnum, AddressSortByEnum.SORT_BY_LAST_NAME);

        assertEquals(Integer.valueOf(90), addressResultSegment.getResultsPerPage());
        assertEquals(Integer.valueOf(20), addressResultSegment.getPageNumber());
    }

    private AddressResultSegment getResultProperties(AddressResultSegment[] lastAddressResultSegmentUsed) {
        return ArgumentMatchers.argThat(new ArgumentMatcher<AddressResultSegment>() {
            @Override
            public boolean matches(AddressResultSegment addressResultSegment) {
                lastAddressResultSegmentUsed[0] = addressResultSegment;
                return true;
            }
        });
    }

    @Test
    public void indexJsonFull() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/address/")
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

        assertEquals(addresses.length, addressList.size());
    }

    @Test
    public void indexJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/address/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("results", "5")
                .param("page", "4")
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

        AddressResultSegment addressResultSegment = lastAddressResultSegmentUsed[0];

        assertEquals(Integer.valueOf(5), addressResultSegment.getResultsPerPage());
        assertEquals(Integer.valueOf(4), addressResultSegment.getPageNumber());
    }

    @Test
    public void create() throws Exception {

        Address address = AddressTest.addressGenerator();

        Mockito.when(mockAddressDao.create(ArgumentMatchers.any(Address.class))).thenReturn(address);

        Gson gson = new GsonBuilder().create();

        String addressJson = gson.toJson(address);

        MvcResult mvcResult = mockMvc.perform(post("/address/")
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
    public void updateTest() throws Exception {

        Address address = AddressTest.addressGenerator();
        address.setId(random.nextInt(Integer.MAX_VALUE));

        Gson gson = new GsonBuilder().create();

        String addressJson = gson.toJson(address);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/address/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(addressJson)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Address addressReturned = gson.fromJson(responseContent, Address.class);

        assertNotNull(addressReturned);

        assertEquals(address, addressReturned);
    }

    @Test
    public void delete() throws Exception {
        int idToDelete = random.nextInt();

        Address address = AddressTest.addressGenerator();
        address.setId(idToDelete);

        Mockito.when(mockAddressDao.delete(idToDelete)).thenReturn(address);

        Gson gson = new GsonBuilder().create();

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete("/address/{0}", idToDelete)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Address addressReturned = gson.fromJson(responseContent, Address.class);

        assertNotNull(addressReturned);

        assertEquals(address, addressReturned);
    }

    @Test
    public void show() throws Exception {
        Address address = AddressTest.addressGenerator();

        address.setId(random.nextInt());

        Mockito.when(mockAddressDao.get(5)).thenReturn(address);

        Gson gson = new GsonBuilder().create();

        String addressJson = gson.toJson(address);

        MvcResult mvcResult = mockMvc.perform(get("/address/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(addressJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Address addressReturned = gson.fromJson(responseContent, Address.class);

        assertNotNull(addressReturned);

        Integer id = addressReturned.getId();

        assertEquals(address, addressReturned);
    }

    @Test
    public void show1() throws Exception {
        Address address = AddressTest.addressGenerator();

        address.setId(random.nextInt());

        int randomId = random.nextInt();

        Mockito.when(mockAddressDao.get(randomId)).thenReturn(address);

        Gson gson = new GsonBuilder().create();

        String addressJson = gson.toJson(address);

        MvcResult mvcResult = mockMvc.perform(get("/address/{0}", randomId)
                .content(addressJson)
        )
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("address"));

        Address addressReturned = (Address) model.get("address");

        assertNotNull(addressReturned);

        Integer id = addressReturned.getId();

        assertEquals(address, addressReturned);
    }

    @Test
    public void editHtmlForm() throws Exception {
        Address address = AddressTest.addressGenerator();

        address.setId(random.nextInt());

        int randomId = random.nextInt();

        // Update currently returns void.
        //Mockito.when(mockAddressDao.update());
        Mockito.when(mockAddressDao.get(randomId)).thenReturn(address);

        MvcResult mvcResult = mockMvc.perform(get("/address/edit/{0}", randomId)
        )
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("address"));

        Address addressReturned = (Address) model.get("address");

        assertNotNull(addressReturned);

        Integer id = addressReturned.getId();

        assertEquals(address, addressReturned);
    }

    @Test
    public void edit() throws Exception {
        int randomId = random.nextInt();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "25");

        params.add("firstName", "Pat");
        params.add("lastName", "Toner");
        params.add("company", "SWG");
        params.add("streetNumber", "8");
        params.add("streetName", "AGBA Rd");
        params.add("city", "Akron");
        params.add("state", "OH");
        params.add("zip", "25795");

        mockMvc.perform(post("/address/edit/{0}", randomId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL_VALUE)
                .params(params)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/address/25"));
    }

    @Test
    public void size() throws Exception {

        Integer dbSize = random.nextInt(Integer.MAX_VALUE);

        Mockito.when(mockAddressDao.size()).thenReturn(dbSize);

        MvcResult mvcResult = mockMvc.perform(get("/address/size").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(Integer.parseInt(content), dbSize.intValue());
    }

    @Test
    public void sizeRefuse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/address/size"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(content, "");
    }

}