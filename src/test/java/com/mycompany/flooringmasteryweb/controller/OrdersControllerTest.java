package com.mycompany.flooringmasteryweb.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.*;
import com.mycompany.flooringmasteryweb.modelBinding.OrderSearchRequestResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Mock
    private StateDao mockStateDao;

    @Mock
    private ProductDao mockProductDao;

    @InjectMocks
    private OrdersController ordersController;

    private MockMvc mockMvc;
    private static MockMvc webMvc;
    private Random random = new Random();
    private OrderResultSegment[] lastOrderResultSegmentUsed = null;
    private List<Order> orderList;
    private List<State> stateList;
    private List<ProductCommand> productCommandList;
    private Gson gsonDeserializer = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        stateList = Arrays.asList(new State[]{StateTest.nonsenseStateGenerator(),
                StateTest.nonsenseStateGenerator(),
                StateTest.nonsenseStateGenerator(),
                StateTest.nonsenseStateGenerator(),
                StateTest.nonsenseStateGenerator(),
                StateTest.nonsenseStateGenerator()});

        productCommandList = Arrays.asList(new ProductCommand[]{
                ProductCommand.buildProductCommand(ProductTest.productGenerator()),
                ProductCommand.buildProductCommand(ProductTest.productGenerator()),
                ProductCommand.buildProductCommand(ProductTest.productGenerator()),
                ProductCommand.buildProductCommand(ProductTest.productGenerator())
        });

        orderList = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 10; i++) {
            orderList.add(OrderTest.orderGenerator());
        }

        lastOrderResultSegmentUsed = new OrderResultSegment[1];

        Mockito.when(mockOrdersDao.list(getOrderResultSegment(lastOrderResultSegmentUsed))).thenReturn(orderList);
        Mockito.when(mockStateDao.getListOfStates()).thenReturn(stateList);
        Mockito.when(mockProductDao.buildCommandProductList()).thenReturn(productCommandList);

        mockMvc = MockMvcBuilders
                .standaloneSetup(ordersController)
                .setCustomArgumentResolvers(new OrderSearchRequestResolver())
                //.webAppContextSetup(webApplicationContext)
                .build();

        webMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void indexTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        Map<String, Object> model = modelAndView.getModel();

        assertFalse(modelAndView.isEmpty());
        assertTrue(modelAndView.hasView());

        String viewName = modelAndView.getViewName();

        assertEquals(viewName, "order\\index");

        assertTrue(model.containsKey("orders"));

        assertTrue(model.containsKey("orderCommand"));

        OrderCommand orderCommand = (OrderCommand) model.get("orderCommand");
        assertNotNull(orderCommand);

        List<Order> orderListModel = (List<Order>) model.get("orders");

        assertEquals(orderListModel.size(), orderList.size());

        for (Order order : orderList) {
            assertTrue(orderListModel.contains(order));
        }

        checkForProductsInModel(model);
        checkForStatesInModel(model);
    }

    public void checkForStatesInModel(Map<String, Object> model) {
        assertTrue(model.containsKey("stateCommands"));

        List<StateCommand> stateListModel = (List<StateCommand>) model.get("stateCommands");

        assertEquals(stateListModel.size(), stateList.size());

        for (State state : stateList) {
            assertTrue(stateListModel.contains(StateCommand.buildCommandState(state)));
        }
    }

    public void checkForProductsInModel(Map<String, Object> model) {
        assertTrue(model.containsKey("productCommands"));
        List<ProductCommand> productCommandListModel = (List<ProductCommand>) model.get("productCommands");

        assertEquals(productCommandListModel.size(), productCommandList.size());

        for (ProductCommand productCommand : productCommandList) {
            assertTrue(productCommandListModel.contains(productCommand));
        }
    }

    @Test
    public void indexTestWithOrderResultSegment() throws Exception {

        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            orderList.add(OrderTest.orderGenerator());
        }

        OrderResultSegment[] lastOrderResultSegmentUsed = new OrderResultSegment[1];

        Mockito.when(mockOrdersDao.list(getOrderResultSegment(lastOrderResultSegmentUsed))).thenReturn(orderList);

        MvcResult mvcResult = mockMvc.perform(get("/orders/")
                .param("page", "20")
                .param("results", "90")
                .param("sort_by", "name"))
                .andExpect(status().isOk())
                .andReturn();


        ModelAndView modelAndView = mvcResult.getModelAndView();

        Map<String, Object> model = modelAndView.getModel();

        assertFalse(modelAndView.isEmpty());
        assertTrue(modelAndView.hasView());

        String viewName = modelAndView.getViewName();

        assertEquals(viewName, "order\\index");


        assertTrue(model.containsKey("orders"));

        List<Order> orderListModel = (List<Order>) model.get("orders");

        assertEquals(orderListModel.size(), orderList.size());

        for (Order order : orderList) {
            assertTrue(orderListModel.contains(order));
        }

        OrderResultSegment orderResultSegment = lastOrderResultSegmentUsed[0];

        OrderSortByEnum sortByEnum = orderResultSegment.getSortByEnum();

        assertEquals(sortByEnum, OrderSortByEnum.SORT_BY_NAME);

        assertEquals(Integer.valueOf(90), orderResultSegment.getResultsPerPage());
        assertEquals(Integer.valueOf(20), orderResultSegment.getPageNumber());

        assertTrue(model.containsKey("orderCommand"));

        OrderCommand orderCommand = (OrderCommand) model.get("orderCommand");
        assertNotNull(orderCommand);

        assertEquals(orderListModel.size(), orderList.size());

        for (Order order : orderList) {
            assertTrue(orderListModel.contains(order));
        }

        checkForProductsInModel(model);
        checkForStatesInModel(model);
    }

    private OrderResultSegment getOrderResultSegment(OrderResultSegment[] lastOrderResultSegmentUsed) {
        return ArgumentMatchers.argThat(new ArgumentMatcher<OrderResultSegment>() {
            @Override
            public boolean matches(OrderResultSegment orderResultSegment) {
                lastOrderResultSegmentUsed[0] = orderResultSegment;
                return true;
            }
        });
    }

    @Test
    public void indexJsonFull() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/orders/")
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

        Order[] orders = gsonDeserializer.fromJson(content, Order[].class);

        assertEquals(orders.length, orderList.size());
    }

    @Test
    public void indexJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/orders/")
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

        Order[] orderes = gsonDeserializer.fromJson(content, Order[].class);

        assertNotNull(orderes);
        assertTrue(orderes.length > 1);

        OrderResultSegment orderResultSegment = lastOrderResultSegmentUsed[0];

        assertEquals(Integer.valueOf(5), orderResultSegment.getResultsPerPage());
        assertEquals(Integer.valueOf(4), orderResultSegment.getPageNumber());
    }

    @Test
    public void create() throws Exception {

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct("Product");

        Order reconstructedOrder = OrderTest.orderGenerator();
        Order outputOrder = OrderTest.orderGenerator();

        Mockito.when(mockProductDao.validProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn(true);
        Mockito.when(mockProductDao.bestGuessProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn("Product Name");
        Mockito.when(mockProductDao.get(ArgumentMatchers.eq("Product Name"))).thenReturn(new Product());
        Mockito.when(mockStateDao.get(ArgumentMatchers.anyString())).thenReturn(new State());

        Mockito.when(mockOrdersDao.orderBuilder(ArgumentMatchers.any(OrderCommand.class))).thenReturn(reconstructedOrder);
        Mockito.when(mockOrdersDao.create(ArgumentMatchers.eq(reconstructedOrder))).thenReturn(outputOrder);

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);
        assertTrue(OrderTest.verifyOrder(outputOrder, orderReturned));
    }

    @Test
    public void update() throws Exception {

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct("Product");

        Order reconstructedOrder = OrderTest.orderGenerator();
        Order outputOrder = OrderTest.orderGenerator();

        Mockito.when(mockProductDao.validProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn(true);
        Mockito.when(mockProductDao.bestGuessProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn("Product Name");
        Mockito.when(mockProductDao.get(ArgumentMatchers.eq("Product Name"))).thenReturn(new Product());
        Mockito.when(mockStateDao.get(ArgumentMatchers.anyString())).thenReturn(new State());

        Mockito.when(mockOrdersDao.orderBuilder(ArgumentMatchers.any(OrderCommand.class))).thenReturn(reconstructedOrder);
        Mockito.when(mockOrdersDao.update(ArgumentMatchers.eq(reconstructedOrder))).thenReturn(outputOrder);
        Mockito.when(mockOrdersDao.create(ArgumentMatchers.any())).thenThrow(new AssertionError("This was not the create endpoint."));

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = mockMvc.perform(put("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);
        assertTrue(OrderTest.verifyOrder(outputOrder, orderReturned));
    }

    @Test
    public void updateWithoutId() throws Exception {

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct("Product");
        commandOrder.setId(null);

        Order reconstructedOrder = OrderTest.orderGenerator();
        reconstructedOrder.setId(null);

        Order outputOrder = OrderTest.orderGenerator();

        Mockito.when(mockProductDao.validProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn(true);
        Mockito.when(mockProductDao.bestGuessProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn("Product Name");
        Mockito.when(mockProductDao.get(ArgumentMatchers.eq("Product Name"))).thenReturn(new Product());
        Mockito.when(mockStateDao.get(ArgumentMatchers.anyString())).thenReturn(new State());

        Mockito.when(mockOrdersDao.orderBuilder(ArgumentMatchers.any(OrderCommand.class))).thenReturn(reconstructedOrder);
        Mockito.when(mockOrdersDao.create(ArgumentMatchers.eq(reconstructedOrder))).thenReturn(outputOrder);
        Mockito.when(mockOrdersDao.update(ArgumentMatchers.any())).thenThrow(new AssertionError("This was supposed to be handled by create method."));

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = mockMvc.perform(put("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);
        assertTrue(OrderTest.verifyOrder(outputOrder, orderReturned));
    }

    @Test
    public void updateWithIdZero() throws Exception {

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct("Product");
        commandOrder.setId(0);

        Order reconstructedOrder = OrderTest.orderGenerator();
        reconstructedOrder.setId(0);

        Order outputOrder = OrderTest.orderGenerator();

        Mockito.when(mockProductDao.validProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn(true);
        Mockito.when(mockProductDao.bestGuessProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn("Product Name");
        Mockito.when(mockProductDao.get(ArgumentMatchers.eq("Product Name"))).thenReturn(new Product());
        Mockito.when(mockStateDao.get(ArgumentMatchers.anyString())).thenReturn(new State());

        Mockito.when(mockOrdersDao.orderBuilder(ArgumentMatchers.any(OrderCommand.class))).thenReturn(reconstructedOrder);
        Mockito.when(mockOrdersDao.create(ArgumentMatchers.eq(reconstructedOrder))).thenReturn(outputOrder);
        Mockito.when(mockOrdersDao.update(ArgumentMatchers.any())).thenThrow(new AssertionError("This was supposed to be handled by create method."));

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = mockMvc.perform(put("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);
        assertTrue(OrderTest.verifyOrder(outputOrder, orderReturned));
    }

    @Test
    public void delete() throws Exception {
        int idToDelete = random.nextInt();

        Order order = OrderTest.orderGenerator();
        order.setId(idToDelete);

        Mockito.when(mockOrdersDao.delete(idToDelete)).thenReturn(order);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/{0}", idToDelete)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);

        assertTrue(OrderTest.verifyOrder(order, orderReturned));
    }

    @Test
    public void deleteByHtml() throws Exception {
        int idToDelete = random.nextInt();

        Order order = OrderTest.orderGenerator();
        order.setId(idToDelete);

        boolean[] orderDeleted = new boolean[]{false};

        Mockito.when(mockOrdersDao.delete(idToDelete)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocationOnMock) throws Throwable {
                orderDeleted[0] = true;
                return order;
            }
        });

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/delete/{0}", idToDelete)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/orders/"));

        assertTrue(orderDeleted[0]);
    }

    @Test
    public void show() throws Exception {
        Order order = OrderTest.orderGenerator();

        order.setId(random.nextInt());

        Mockito.when(mockOrdersDao.get(5)).thenReturn(order);

        String orderJson = gsonDeserializer.toJson(order);

        MvcResult mvcResult = mockMvc.perform(get("/orders/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);

        assertNotNull(orderReturned.getId());

        assertTrue(OrderTest.verifyOrder(order, orderReturned));
    }

    @Test
    public void show1() throws Exception {
        Order order = OrderTest.orderGenerator();

        order.setId(random.nextInt());

        int randomId = random.nextInt();

        Mockito.when(mockOrdersDao.get(randomId)).thenReturn(order);

        String orderJson = gsonDeserializer.toJson(order);

        MvcResult mvcResult = mockMvc.perform(get("/orders/{0}", randomId)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("order"));

        Order orderReturned = (Order) model.get("order");

        assertNotNull(orderReturned);

        assertNotNull(orderReturned.getId());

        assertTrue(OrderTest.verifyOrder(order, orderReturned));
    }

    @Test
    public void editHtmlForm() throws Exception {
        Order order = OrderTest.orderGenerator();

        order.setId(random.nextInt());

        int randomId = random.nextInt();

        // Update currently returns void.
        //Mockito.when(mockOrdersDao.update());
        Mockito.when(mockOrdersDao.get(randomId)).thenReturn(order);
        Mockito.when(mockOrdersDao.resolveOrderCommand(ArgumentMatchers.eq(order))).thenReturn(OrderCommand.build(order));

        MvcResult mvcResult = mockMvc.perform(get("/orders/edit/{0}", randomId)
        )
                .andExpect(status().isOk())
                .andReturn();

        String viewName = mvcResult.getModelAndView().getViewName();
        assertEquals(viewName, "order\\index");

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        assertTrue(model.containsKey("orderCommand"));

        OrderCommand orderReturned = (OrderCommand) model.get("orderCommand");

        assertNotNull(orderReturned);

        assertNotNull(orderReturned.getId());

        assertEquals(OrderCommand.build(order), orderReturned);
    }

    public void equalityTest(OrderCommand inputOrderCommand, OrderCommand orderCommand) {
        assertTrue(OrderTest.isSameDay(orderCommand.getDate(), inputOrderCommand.getDate()));
        assertEquals(orderCommand.getId(), inputOrderCommand.getId());
        assertEquals(orderCommand.getState(), inputOrderCommand.getState());
        assertEquals(orderCommand.getArea(), inputOrderCommand.getArea(), 0.00001);
        assertEquals(orderCommand.getProduct(), inputOrderCommand.getProduct());
        assertEquals(orderCommand.getName(), inputOrderCommand.getName());
    }

    @Test
    public void size() throws Exception {

        Integer dbSize = random.nextInt(Integer.MAX_VALUE);

        Mockito.when(mockOrdersDao.size()).thenReturn(dbSize);

        MvcResult mvcResult = mockMvc.perform(get("/orders/size").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(Integer.parseInt(content), dbSize.intValue());
    }

    @Test
    public void sizeRefuse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/size"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(content, "");
    }

    @Test
    public void searchGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();
    }

    @Test
    public void searchPost() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "Order_number";

        Mockito.when(mockOrdersDao.search(ArgumentMatchers.any(OrderSearchRequest.class),
                                    ArgumentMatchers.any(ResultSegment.class)))
                .thenReturn(orderList.subList(0, 10));

        MvcResult mvcResult = mockMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 10);

        for (Order order : orders) {
            assertEquals(order, orderList.get(orders.indexOf(order)));
        }

        ArgumentCaptor<OrderSearchRequest> orderSearchRequestArgumentCaptor = ArgumentCaptor.forClass(OrderSearchRequest.class);
        ArgumentCaptor<ResultSegment> resultSegmentArgumentCaptor = ArgumentCaptor.forClass(ResultSegment.class);
        Mockito.verify(mockOrdersDao, times(1)).search(orderSearchRequestArgumentCaptor.capture(), resultSegmentArgumentCaptor.capture());

        ResultSegment<OrderSortByEnum> resultSegment = resultSegmentArgumentCaptor.getValue();
        //assertEquals(resultSegment.getPageNumber().intValue(), 4 );
        //assertEquals(resultSegment.getResultsPerPage().intValue(), 20);
        //assertEquals(resultSegment.getSortByEnum().ordinal(), OrderSortByEnum.SORT_BY_STATE_INVERSE.ordinal());

        OrderSearchRequest orderSearchRequest = orderSearchRequestArgumentCaptor.getValue();
        assertEquals(orderSearchRequest.getSearchBy().ordinal(), OrderSearchByOptionEnum.ORDER_NUMBER.ordinal());
        assertEquals(orderSearchRequest.getSearchText(), SEARCH_STRING);
    }

    @Test
    public void searchPostByWeb() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "Order_number";

//        Mockito.when(mockOrdersDao.search(ArgumentMatchers.any(OrderSearchRequest.class),
//                ArgumentMatchers.any(ResultSegment.class)))
//                .thenReturn(orderList.subList(0, 10));

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
        )
                //.andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Exception ex = mvcResult.getResolvedException();
        if (ex != null) {
            String message = ex.getMessage();
            System.out.println(message);
        }

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 10);

        for (Order order : orders) {
            assertEquals(order, orderList.get(orders.indexOf(order)));
        }

        ArgumentCaptor<OrderSearchRequest> orderSearchRequestArgumentCaptor = ArgumentCaptor.forClass(OrderSearchRequest.class);
        ArgumentCaptor<ResultSegment> resultSegmentArgumentCaptor = ArgumentCaptor.forClass(ResultSegment.class);
        Mockito.verify(mockOrdersDao, times(1)).search(orderSearchRequestArgumentCaptor.capture(), resultSegmentArgumentCaptor.capture());

        ResultSegment<OrderSortByEnum> resultSegment = resultSegmentArgumentCaptor.getValue();
        //assertEquals(resultSegment.getPageNumber().intValue(), 4 );
        //assertEquals(resultSegment.getResultsPerPage().intValue(), 20);
        //assertEquals(resultSegment.getSortByEnum().ordinal(), OrderSortByEnum.SORT_BY_STATE_INVERSE.ordinal());

        OrderSearchRequest orderSearchRequest = orderSearchRequestArgumentCaptor.getValue();
        assertEquals(orderSearchRequest.getSearchBy().ordinal(), OrderSearchByOptionEnum.ORDER_NUMBER.ordinal());
        assertEquals(orderSearchRequest.getSearchText(), SEARCH_STRING);
    }
}