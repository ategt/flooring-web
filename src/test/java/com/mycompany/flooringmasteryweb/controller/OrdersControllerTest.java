package com.mycompany.flooringmasteryweb.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.flooringmasteryweb.aop.ApplicationContextProvider;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.*;
import com.mycompany.flooringmasteryweb.modelBinding.OrderSearchRequestResolver;
import com.mycompany.flooringmasteryweb.modelBinding.OrderResultSegmentResolver;
import com.mycompany.flooringmasteryweb.validation.*;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.hibernate.validator.HibernateValidator;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
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

    @Autowired
    private MockServletContext servletContext;

    @Mock
    private OrderDao mockOrdersDao;

    @Mock
    private StateDao mockStateDao;

    @Mocked
    ValidProductValidator validProductValidator;

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
    private ApplicationContext previousApplicationContext;

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

        Calendar calendar = Calendar.getInstance();

        for (Order order : orderList) {
            Date orderDate = order.getDate();

            calendar.setTime(orderDate);
            TimeZone timeZone = calendar.getTimeZone();

            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            order.setDate(calendar.getTime());
        }

        lastOrderResultSegmentUsed = new OrderResultSegment[1];

        Mockito.when(mockOrdersDao.list(getOrderResultSegment(lastOrderResultSegmentUsed))).thenReturn(orderList);
        Mockito.when(mockStateDao.getListOfStates()).thenReturn(stateList);
        Mockito.when(mockProductDao.buildCommandProductList()).thenReturn(productCommandList);

        OrderResultSegmentResolver orderResultSegmentResolver = new OrderResultSegmentResolver();
        orderResultSegmentResolver.setApplicationContext(webApplicationContext);

        mockMvc = MockMvcBuilders
                .standaloneSetup(ordersController)
                .setCustomArgumentResolvers(new OrderSearchRequestResolver(), orderResultSegmentResolver)
                .build();

        webMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        ordersController.setApplicationContext(webApplicationContext);

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
        OrderResultSegmentResolver orderResultSegmentResolver = new OrderResultSegmentResolver();
        orderResultSegmentResolver.setApplicationContext(webApplicationContext);

        mockMvc = MockMvcBuilders
                .standaloneSetup(ordersController)
                .setCustomArgumentResolvers(new OrderSearchRequestResolver(), orderResultSegmentResolver)
                .build();

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct("Product");
        commandOrder.setId(0);

        Order reconstructedOrder = OrderTest.orderGenerator();
        Order outputOrder = OrderTest.orderGenerator();

        Mockito.when(mockProductDao.validProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn(true);
        Mockito.when(mockProductDao.bestGuessProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn("Product Name");
        Mockito.when(mockProductDao.get(ArgumentMatchers.eq("Product Name"))).thenReturn(new Product());
        Mockito.when(mockStateDao.get(ArgumentMatchers.anyString())).thenReturn(new State());

        Mockito.when(mockOrdersDao.orderBuilder(ArgumentMatchers.any(OrderCommand.class))).thenReturn(reconstructedOrder);
        Mockito.when(mockOrdersDao.create(ArgumentMatchers.eq(reconstructedOrder))).thenReturn(outputOrder);

        String orderJson = gsonDeserializer.toJson(commandOrder);

        new Expectations() {{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        when(mockProductDao.validProductName(ArgumentMatchers.anyString())).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        restoreProperContext();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);
        assertTrue(OrderTest.verifyOrder(outputOrder, orderReturned));
    }

    public void restoreProperContext() {
        new ApplicationContextProvider().setApplicationContext(previousApplicationContext);
    }

    public void switchToMockContext() {
        previousApplicationContext = ApplicationContextProvider.getApplicationContext();
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
        new ApplicationContextProvider().setApplicationContext(applicationContext);
        when(applicationContext.getBean(ArgumentMatchers.contains("productDao"), ArgumentMatchers.any(ProductDao.class))).thenReturn(mockProductDao);
    }

    @Test
    public void update() throws Exception {

        new Expectations() {{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

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

        switchToMockContext();

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

        new Expectations() {{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

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

        switchToMockContext();

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

        new Expectations() {{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

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

        switchToMockContext();

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
        // TODO: make order dao update return updated order then mock it up in this test.
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

        OrderSearchRequest orderSearchRequest = orderSearchRequestArgumentCaptor.getValue();
        assertEquals(orderSearchRequest.getSearchBy().ordinal(), OrderSearchByOptionEnum.ORDER_NUMBER.ordinal());
        assertEquals(orderSearchRequest.getSearchText(), SEARCH_STRING);
    }

    @Test
    public void searchPostByIdToWeb() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Order_number";

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
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

        assertEquals(orders.size(), 1);
    }

    @Test
    public void searchPostRandomToWeb() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "everything";

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
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

        assertEquals(orders.size(), 0);
    }

    @Test
    public void searchPostWithPaging() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "Order_number";

        Mockito.when(mockOrdersDao.search(ArgumentMatchers.any(OrderSearchRequest.class),
                ArgumentMatchers.any(ResultSegment.class)))
                .thenReturn(orderList.subList(0, 10));

        MvcResult mvcResult = mockMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "2")
                .param("results", "8")
                .param("sort_by", "date")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

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

        OrderSearchRequest orderSearchRequest = orderSearchRequestArgumentCaptor.getValue();
        assertEquals(orderSearchRequest.getSearchBy().ordinal(), OrderSearchByOptionEnum.ORDER_NUMBER.ordinal());
        assertEquals(orderSearchRequest.getSearchText(), SEARCH_STRING);
    }

    @Test
    public void searchPostByIdToWebWithPagination() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Order_number";

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "2")
                .param("results", "8")
                .param("sort_by", "date")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 0);
    }

    @Test
    public void searchPostByIdToWebWithPaginationLoadsPagingLinksToModel() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Everything";

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "0")
                .param("results", "1")
                .param("sort_by", "date")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        assertTrue(model.containsKey("last_link"));
        assertTrue(model.containsKey("next_link"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 1);
    }

    @Test
    public void searchPostByIdToWebWithPaginationLoadsPagingLinksToModelOnPage1() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Everything";

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "1")
                .param("results", "1")
                .param("sort_by", "date")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        assertTrue(model.containsKey("last_link"));
        assertTrue(model.containsKey("next_link"));
        assertTrue(model.containsKey("first_link"));
        assertTrue(model.containsKey("prev_link"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 1);
    }

    @Test
    public void searchWebLoadsPagingLinksToModelOnPage1() throws Exception {

        MvcResult mvcResult = webMvc.perform(get("/orders/search")
                .param("page", "0")
                .param("results", "1")
                .param("sort_by", "date")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        assertTrue(model.containsKey("last_link"));
        assertTrue(model.containsKey("next_link"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 1);
    }

    @Test
    public void searchPostByIdToWebWithPaginationOnWrongPage() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Order_number";

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "2")
                .param("results", "8")
                .param("sort_by", "date")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 0);
    }

    @Test
    public void searchPostRandomToWebWithPagination() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "everything";

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("results", "8")
                .param("sort_by", "date")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order\\search"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();

        assertTrue(model.containsKey("orders"));

        List<Order> orders = (List<Order>) model.get("orders");

        assertEquals(orders.size(), 0);
    }

    @Test
    public void searchPostWithJson() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();

        Mockito.when(mockOrdersDao.search(ArgumentMatchers.any(OrderSearchRequest.class),
                ArgumentMatchers.any(ResultSegment.class)))
                .thenReturn(orderList.subList(0, 10));

        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();
        orderSearchRequest.setSearchBy(OrderSearchByOptionEnum.ORDER_NUMBER);
        orderSearchRequest.setSearchText(SEARCH_STRING);

        String orderSearchJson = gsonDeserializer.toJson(orderSearchRequest);

        MvcResult mvcResult = mockMvc.perform(post("/orders/search")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderSearchJson)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Order[] ordersArray = gsonDeserializer.fromJson(content, Order[].class);
        List<Order> orders = Arrays.asList(ordersArray);

        assertEquals(orders.size(), 10);

        for (Order order : orders) {
            assertTrue(OrderTest.verifyOrder(order, orderList.get(orders.indexOf(order))));
        }

        ArgumentCaptor<OrderSearchRequest> orderSearchRequestArgumentCaptor = ArgumentCaptor.forClass(OrderSearchRequest.class);
        ArgumentCaptor<ResultSegment> resultSegmentArgumentCaptor = ArgumentCaptor.forClass(ResultSegment.class);
        Mockito.verify(mockOrdersDao, times(1)).search(orderSearchRequestArgumentCaptor.capture(), resultSegmentArgumentCaptor.capture());

        ResultSegment<OrderSortByEnum> resultSegment = resultSegmentArgumentCaptor.getValue();

        OrderSearchRequest remoteOrderSearchRequest = orderSearchRequestArgumentCaptor.getValue();
        assertEquals(remoteOrderSearchRequest.getSearchBy().ordinal(), OrderSearchByOptionEnum.ORDER_NUMBER.ordinal());
        assertEquals(remoteOrderSearchRequest.getSearchText(), SEARCH_STRING);
    }

    @Test
    public void searchPostByIdToWebUsingJson() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Order_number";

        String orderSearchJson = gsonDeserializer.toJson(new OrderSearchRequest(SEARCH_STRING, OrderSearchByOptionEnum.ORDER_NUMBER));

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .accept(MediaType.APPLICATION_JSON)
                .content(orderSearchJson)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Order[] ordersArray = gsonDeserializer.fromJson(content, Order[].class);
        List<Order> orders = Arrays.asList(ordersArray);

        assertEquals(orders.size(), 1);
    }

    @Test
    public void searchPostRandomToWebWithJson() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "everything";

        String orderSearchJson = gsonDeserializer.toJson(new OrderSearchRequest(SEARCH_STRING, OrderSearchByOptionEnum.EVERYTHING));

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderSearchJson)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Order[] ordersArray = gsonDeserializer.fromJson(content, Order[].class);
        List<Order> orders = Arrays.asList(ordersArray);

        assertEquals(orders.size(), 0);
    }

    @Test
    public void searchPostWithPagingUsingJson() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "Order_number";

        Mockito.when(mockOrdersDao.search(ArgumentMatchers.any(OrderSearchRequest.class),
                ArgumentMatchers.any(ResultSegment.class)))
                .thenReturn(orderList.subList(0, 10));

        String orderSearchJson = gsonDeserializer.toJson(new OrderSearchRequest(SEARCH_STRING, OrderSearchByOptionEnum.ORDER_NUMBER));

        MvcResult mvcResult = mockMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "2")
                .param("results", "8")
                .param("sort_by", "date")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderSearchJson)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Order[] ordersArray = gsonDeserializer.fromJson(content, Order[].class);
        List<Order> orders = Arrays.asList(ordersArray);

        assertEquals(orders.size(), 10);

        for (Order order : orders) {
            assertTrue(OrderTest.verifyOrder(order, orderList.get(orders.indexOf(order))));
        }

        ArgumentCaptor<OrderSearchRequest> orderSearchRequestArgumentCaptor = ArgumentCaptor.forClass(OrderSearchRequest.class);
        ArgumentCaptor<ResultSegment> resultSegmentArgumentCaptor = ArgumentCaptor.forClass(ResultSegment.class);
        Mockito.verify(mockOrdersDao, times(1)).search(orderSearchRequestArgumentCaptor.capture(), resultSegmentArgumentCaptor.capture());

        ResultSegment<OrderSortByEnum> resultSegment = resultSegmentArgumentCaptor.getValue();

        OrderSearchRequest orderSearchRequest = orderSearchRequestArgumentCaptor.getValue();
        assertEquals(orderSearchRequest.getSearchBy().ordinal(), OrderSearchByOptionEnum.ORDER_NUMBER.ordinal());
        assertEquals(orderSearchRequest.getSearchText(), SEARCH_STRING);
    }

    @Test
    public void searchPostByIdToWebWithPaginationUsingJson() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Order_number";

        String orderSearchJson = gsonDeserializer.toJson(new OrderSearchRequest(SEARCH_STRING, OrderSearchByOptionEnum.ORDER_NUMBER));

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "2")
                .param("results", "8")
                .param("sort_by", "date")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderSearchJson)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Order[] ordersArray = gsonDeserializer.fromJson(content, Order[].class);
        List<Order> orders = Arrays.asList(ordersArray);

        assertEquals(orders.size(), 0);
    }

    @Test
    public void searchPostByIdToWebWithPaginationOnWrongPageUsingJson() throws Exception {

        final String SEARCH_STRING = "1";
        final String SEARCH_BY = "Order_number";

        String orderSearchJson = gsonDeserializer.toJson(new OrderSearchRequest(SEARCH_STRING, OrderSearchByOptionEnum.ORDER_NUMBER));

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("page", "2")
                .param("results", "8")
                .param("sort_by", "date")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderSearchJson)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Order[] ordersArray = gsonDeserializer.fromJson(content, Order[].class);
        List<Order> orders = Arrays.asList(ordersArray);

        assertEquals(orders.size(), 0);
    }

    @Test
    public void searchPostRandomToWebWithPaginationUsingJson() throws Exception {

        final String SEARCH_STRING = UUID.randomUUID().toString();
        final String SEARCH_BY = "everything";

        String orderSearchJson = gsonDeserializer.toJson(new OrderSearchRequest(SEARCH_STRING, OrderSearchByOptionEnum.EVERYTHING));

        MvcResult mvcResult = webMvc.perform(post("/orders/search")
                .param("searchBy", SEARCH_BY)
                .param("searchText", SEARCH_STRING)
                .param("results", "8")
                .param("sort_by", "date")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderSearchJson)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Order[] ordersArray = gsonDeserializer.fromJson(content, Order[].class);
        List<Order> orders = Arrays.asList(ordersArray);

        assertEquals(orders.size(), 0);
    }

    @Test
    public void testStateValidation() throws Exception {

        new Expectations() {{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct("Product");

        commandOrder.setId(0);

        Order reconstructedOrder = OrderTest.orderGenerator();
        Order outputOrder = OrderTest.orderGenerator();

        Mockito.when(mockProductDao.validProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn(true);
        Mockito.when(mockProductDao.bestGuessProductName(ArgumentMatchers.eq(commandOrder.getProduct()))).thenReturn("Product Name");
        Mockito.when(mockProductDao.get(ArgumentMatchers.eq("Product Name"))).thenReturn(new Product());
        Mockito.when(mockStateDao.get(ArgumentMatchers.anyString())).thenReturn(new State());

        Mockito.when(mockOrdersDao.orderBuilder(ArgumentMatchers.any(OrderCommand.class))).thenReturn(reconstructedOrder);
        Mockito.when(mockOrdersDao.create(ArgumentMatchers.eq(reconstructedOrder))).thenReturn(outputOrder);

        String orderJson = gsonDeserializer.toJson(commandOrder);

        switchToMockContext();

        MvcResult mvcResult = mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
//                .andExpect(status().isOk())
                //          .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Order orderReturned = gsonDeserializer.fromJson(responseContent, Order.class);

        assertNotNull(orderReturned);
        assertTrue(OrderTest.verifyOrder(outputOrder, orderReturned));
    }

    @Test
    public void testStateInvalidationValidation() throws Exception {

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("HQ");
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

        switchToMockContext();

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testStateInvalidationValidationFromWebContext() throws Exception {

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("HQ");
        commandOrder.setProduct("Product");

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = webMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        ValidationErrorContainer validationErrorContainer =
                gsonDeserializer.fromJson(content, ValidationErrorContainer.class);

        List<ValidationError> validationErrorList = validationErrorContainer.getErrors();

        assertTrue(validationErrorList.stream().anyMatch(
                validationError -> validationError.getFieldName().equalsIgnoreCase("state"))
        );
    }

    @Test
    public void testProductValidatorMockingSucessfulPositiveTest() throws Exception {

        new Expectations() {{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct(UUID.randomUUID().toString());
        commandOrder.setId(0);

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().isOk())
                .andReturn();

        new Verifications(){{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            minTimes = 1;
        }};
    }

    @Test
    public void testProductValidatorMockingSucessfulNegativeTest() throws Exception {

        new Expectations() {{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = false;
        }};

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct(UUID.randomUUID().toString());
        commandOrder.setId(0);

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().is4xxClientError())
                .andReturn();

        new Verifications(){{
            validProductValidator.isValid((String) any, (ConstraintValidatorContext) any);
            minTimes = 1;
        }};
    }

    @Test
    public void testProductInvalidationValidationFromWebContext() throws Exception {

        webMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        OrderCommand commandOrder =
                OrderCommand.build(OrderTest.orderGenerator());

        commandOrder.setState("MN");
        commandOrder.setProduct(UUID.randomUUID().toString());
        commandOrder.setId(0);

        String orderJson = gsonDeserializer.toJson(commandOrder);

        MvcResult mvcResult = webMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(orderJson)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        ValidationErrorContainer validationErrorContainer =
                gsonDeserializer.fromJson(content, ValidationErrorContainer.class);

        List<ValidationError> validationErrorList = validationErrorContainer.getErrors();

        assertTrue(validationErrorList.stream().anyMatch(
                validationError -> validationError.getFieldName().equalsIgnoreCase("product"))
        );
    }
}