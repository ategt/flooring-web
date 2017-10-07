package com.mycompany.flooringmasteryweb.interceptors;

import com.mycompany.flooringmasteryweb.dao.TimingDao;
import com.mycompany.flooringmasteryweb.dto.Timing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
        @ContextConfiguration(locations = {"/test-SetupSimulatedProductionEnvironment.xml"}),
        @ContextConfiguration(locations = {"/spring-persistence.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml"})
})
public class ExecuteTimeInterceptorTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TimingDao timingDao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkForTimingInfoInModelMapTest() throws Exception {
        Timing beforeTiming = timingDao.getLast();

        MockMvc mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        MvcResult mvcResult = mvc.perform(get("/orders/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        HandlerInterceptor[] handlerInterceptors = mvcResult.getInterceptors();

        assertTrue(model.containsKey("timing"));

        Timing modelTiming = (Timing) model.get("timing");
        assertTrue(handlerInterceptors.length > 0);

        Timing afterTiming = timingDao.getLast();

        List<Timing> timingList = new ArrayList<>();

        for (int i = beforeTiming.getId(); i <= afterTiming.getId(); i++) {
            timingList.add(timingDao.get(i));
        }

        for (Timing timing : timingList) {
            System.out.println(timing.toString());
        }

        assertTrue(timingList.contains(modelTiming));
    }
}