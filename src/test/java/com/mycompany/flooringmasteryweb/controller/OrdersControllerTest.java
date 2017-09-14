package com.mycompany.flooringmasteryweb.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dto.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class OrdersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private OrderDao mockOrdersDao;

    @InjectMocks
    private OrdersController ordersController;

    private MockMvc mockMvc;
    private Random random = new Random();
    private ResultProperties[] lastResultPropertiesUsed = null;
    private List<Order> orderList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        orderList = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 10; i++) {
            orderList.add(OrderTest.orderGenerator());
        }

        lastResultPropertiesUsed = new ResultProperties[1];

        Mockito.when(mockOrdersDao.list(getResultProperties(lastResultPropertiesUsed))).thenReturn(orderList);
        Mockito.when(mockOrdersDao.getOrderesSortedByParameter(getResultProperties(lastResultPropertiesUsed))).thenReturn(orderList);

        ordersController.setApplicationContext(webApplicationContext);

        mockMvc = MockMvcBuilders
                .standaloneSetup(ordersController)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void indexTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/order/"))
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

                        assertEquals(viewName, "order\\index");

                        assertTrue(model.containsKey("orderes"));
                    }
                })
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("orderes"));

        List<Order> orderListModel = (List<Order>) model.get("orderes");

        assertEquals(orderListModel.size(), orderList.size());

        for (Order order : orderList) {
            assertTrue(orderListModel.contains(order));
        }
    }

    @Test
    public void indexTestWithResultProperties() throws Exception {

        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            orderList.add(OrderTest.orderGenerator());
        }

        ResultProperties[] lastResultPropertiesUsed = new ResultProperties[1];

        Mockito.when(mockOrdersDao.list(getResultProperties(lastResultPropertiesUsed))).thenReturn(orderList);
        Mockito.when(mockOrdersDao.getOrderesSortedByParameter(getResultProperties(lastResultPropertiesUsed))).thenReturn(orderList);

        MvcResult mvcResult = mockMvc.perform(get("/order/")
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

                        assertEquals(viewName, "order\\index");

                        assertTrue(model.containsKey("orderes"));
                    }
                })
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("orderes"));

        List<Order> orderListModel = (List<Order>) model.get("orderes");

        assertEquals(orderListModel.size(), orderList.size());

        for (Order order : orderList) {
            assertTrue(orderListModel.contains(order));
        }

        ResultProperties resultProperties = lastResultPropertiesUsed[0];

        OrderSortByEnum sortByEnum = resultProperties.getSortByEnum();

        assertEquals(sortByEnum, OrderSortByEnum.SORT_BY_LAST_NAME);

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
    public void indexJsonFull() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/order/")
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

        Order[] orderes = gson.fromJson(content, Order[].class);

        assertEquals(orderes.length, orderList.size());
    }

    @Test
    public void indexJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/order/")
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

        Order[] orderes = gson.fromJson(content, Order[].class);

        assertNotNull(orderes);
        assertTrue(orderes.length > 1);

        ResultProperties resultProperties = lastResultPropertiesUsed[0];

        assertEquals(Integer.valueOf(5), resultProperties.getResultsPerPage());
        assertEquals(Integer.valueOf(4), resultProperties.getPageNumber());
    }

    @Test
    public void create() throws Exception {

        Order order = OrderTest.orderGenerator();

        Mockito.when(mockOrdersDao.create(ArgumentMatchers.any(Order.class))).thenReturn(order);

        Gson gson = new GsonBuilder().create();

        String orderJson = gson.toJson(order);

        MvcResult mvcResult = mockMvc.perform(post("/order/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gson.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);

        Integer id = orderReturned.getId();

        assertNull(id);

        assertEquals(order, orderReturned);
    }

    @Test
    public void updateTest() throws Exception {

        Order order = OrderTest.orderGenerator();
        order.setId(random.nextInt(Integer.MAX_VALUE));

        Gson gson = new GsonBuilder().create();

        String orderJson = gson.toJson(order);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/order/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(orderJson)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gson.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);

        assertEquals(order, orderReturned);
    }

    @Test
    public void delete() throws Exception {
        int idToDelete = random.nextInt();

        Order order = OrderTest.orderGenerator();
        order.setId(idToDelete);

        Mockito.when(mockOrdersDao.delete(idToDelete)).thenReturn(order);

        Gson gson = new GsonBuilder().create();

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete("/order/{0}", idToDelete)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gson.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);

        assertEquals(order, orderReturned);
    }

    @Test
    public void show() throws Exception {
        Order order = OrderTest.orderGenerator();

        order.setId(random.nextInt());

        Mockito.when(mockOrdersDao.get(5)).thenReturn(order);

        Gson gson = new GsonBuilder().create();

        String orderJson = gson.toJson(order);

        MvcResult mvcResult = mockMvc.perform(get("/order/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gson.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);

        Integer id = orderReturned.getId();

        assertEquals(order, orderReturned);
    }

    @Test
    public void show1() throws Exception {
        Order order = OrderTest.orderGenerator();

        order.setId(random.nextInt());

        int randomId = random.nextInt();

        Mockito.when(mockOrdersDao.get(randomId)).thenReturn(order);

        Gson gson = new GsonBuilder().create();

        String orderJson = gson.toJson(order);

        MvcResult mvcResult = mockMvc.perform(get("/order/{0}", randomId)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("order"));

        Order orderReturned = (Order) model.get("order");

        assertNotNull(orderReturned);

        Integer id = orderReturned.getId();

        assertEquals(order, orderReturned);
    }

    @Test
    public void editHtmlForm() throws Exception {
        Order order = OrderTest.orderGenerator();

        order.setId(random.nextInt());

        int randomId = random.nextInt();

        // Update currently returns void.
        //Mockito.when(mockOrdersDao.update());
        Mockito.when(mockOrdersDao.get(randomId)).thenReturn(order);

        MvcResult mvcResult = mockMvc.perform(get("/order/edit/{0}", randomId)
        )
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("order"));

        Order orderReturned = (Order) model.get("order");

        assertNotNull(orderReturned);

        Integer id = orderReturned.getId();

        assertEquals(order, orderReturned);
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

        mockMvc.perform(post("/order/edit/{0}", randomId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL_VALUE)
                .params(params)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/order/25"));
    }

    @Test
    public void size() throws Exception {

        Integer dbSize = random.nextInt(Integer.MAX_VALUE);

        Mockito.when(mockOrdersDao.size()).thenReturn(dbSize);

        MvcResult mvcResult = mockMvc.perform(get("/order/size").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(Integer.parseInt(content), dbSize.intValue());
    }

    @Test
    public void sizeRefuse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/order/size"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(content, "");
    }

}