/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.State;
import java.util.Objects;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author apprentice
 */
public class StateDaoDbImplTest {

    ApplicationContext ctx;

    public StateDaoDbImplTest() {
        ctx = new ClassPathXmlApplicationContext("testStateDb-DedicatedApplicationContext.xml");
    }

    @Before
    public void setUp() {

        StateDao instance = ctx.getBean("stateDao", StateDao.class);

        String[] fakeStates = {"SG", "DQ", "SW", "FG", "GH", "DG", "IN", "OH", "KC" , "CA" };

        for (String fakeState : fakeStates) {
            if (instance.get(fakeState) != null) {
                State state = new State();
                state.setState(fakeState);
                instance.delete(state);
            }
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        State state = new State();
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testCreateB() {
        System.out.println("create");
        State state = null;
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testGetA() {
        System.out.println("get - null");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;
        State result = instance.get(null);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testGetC() {
        System.out.println("get - null");
        Object state = null;
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;
        State result = instance.get((String) state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testGetD() {
        System.out.println("get - null");
        Object object = null;
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;

        State state1 = new State();
        state1.setState("GH");
        instance.create(state1);

        State state2 = new State();
        state2.setState("GZ");
        instance.create(state2);

        State state3 = new State();
        state3.setState("GQ");
        instance.create(state3);

        State state4 = new State();
        state4.setState("GR");
        instance.create(state4);

        State result = instance.get((String) object);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));

        instance.delete(state1);
        instance.delete(state2);
        instance.delete(state3);
        instance.delete(state4);
    }

    @Test
    public void testCreateC() {
        System.out.println("create");
        State state = new State();
        state.setState("GH");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = state;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testCreateD() {
        System.out.println("create");
        State state = new State();
        state.setState("Z");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testCreateE() {
        System.out.println("create");
        State state = new State();
        state.setStateTax(0.0d);
        state.setState("SW");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = state;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testCreateQ() {
        System.out.println("create");
        State state = new State();
        state.setState("SW");
        State stateB = new State();
        stateB.setState("SW");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = state;
        instance.create(state);
        State result = instance.create(stateB);
        assertNull(result);
    }

    @Test
    public void testCreateR() {
        System.out.println("create");
        State state = new State();
        state.setState("SW");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        instance.create(state);
        State result = instance.create(state);
        assertNull(result);
    }

    @Test
    public void testCreateF() {
        System.out.println("create");
        State state = new State();
        state.setState("Mexico");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testCreateH() {
        System.out.println("create");
        State state = new State();
        state.setState("HQ");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = null;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
    }

    @Test
    public void testCreateG() {

        System.out.println("create");
        State state = new State();
        state.setState("SG");

        State otherState = new State();
        otherState.setState("SG");

        StateDao instance = ctx.getBean("stateDao", StateDao.class);

        State expResult = state;
        State result = instance.create(state);
        State otherResult = instance.create(otherState);

        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));
        assertEquals(null, otherResult);

        instance.delete(state);
        instance.delete(otherState);
    }

    @Test
    public void testDelete() {
        StateDao instance = ctx.getBean("stateDao", StateDao.class);

        if (instance.get("SG") != null) {
            State state = new State();
            state.setState("SG");
            instance.delete(state);

        }

        System.out.println("create");
        State state = new State();
        state.setState("SG");
        State expResult = state;
        State result = instance.create(state);
        
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));

        // Test get method.
        State returnedState = instance.get(state.getState());
        assertTrue(verifyState(returnedState, result));
        instance.delete(state);

        returnedState = instance.get(state.getState());
        assertNull(returnedState);
    }

    @Test
    public void testGet() {
        System.out.println("create");
        State state = new State();
        state.setState("SG");
        state.setStateTax(0.0d);
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = state;
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));

        // Test get method.
        State returnedState = instance.get(state.getState());
        assertTrue(verifyState(returnedState, result));
        instance.delete(state);

        returnedState = instance.get(state.getState());
        assertEquals(returnedState, null);
    }

    @Test
    public void testGet2() {
        System.out.println("create");
        State state = new State();
        state.setState("SG");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = state;
        String stateNameLowerCase = "sg";
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));

        // Test get method.
        State returnedState = instance.get(stateNameLowerCase);
        assertTrue(verifyState(returnedState, result));
        instance.delete(state);

        returnedState = instance.get(stateNameLowerCase);
        assertEquals(returnedState, null);
    }

    @Test
    public void testGet3() {
        System.out.println("create");
        State state = new State();
        state.setState("SG");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);
        State expResult = state;
        String stateNameLowerCase = "sG";
        State result = instance.create(state);
        assertTrue("Expected: " + expResult + ", Actual: " + result, verifyState(expResult, result));

        // Test get method.
        State returnedState = instance.get(stateNameLowerCase);
        assertTrue(verifyState(returnedState, result));
        instance.delete(state);

        returnedState = instance.get(stateNameLowerCase);
        assertEquals(returnedState, null);
    }

    @Test
    public void testSize() {

        State state = new State();
        state.setState("DQ");
        StateDao instance = ctx.getBean("stateDao", StateDao.class);

        instance.create(state);

        State secondState = new State();
        secondState.setState("SG");

        State thirdState = new State();
        thirdState.setState("FG");

        instance.create(secondState);
        instance.create(thirdState);

        assertTrue(instance.getList().contains("SG"));
        assertTrue(instance.getList().contains("FG"));
        assertTrue(instance.getList().contains("DQ"));
    }

    @Test
    public void testEncodeAndDecode() {

        StateDao stateDao = ctx.getBean("stateDao", StateDao.class);
        State testState = new State();
        double taxDouble = 0.0d;
        testState.setStateTax(taxDouble);
        
        String stateName = "UK";
        testState.setState(stateName);
        
        // Create the file in the Dao.
        State returnedState = stateDao.create(testState);

        // Verify that the state object that the create method passed back
        // was the same one it was given.
        assertEquals(testState, returnedState);

        returnedState.setStateTax(taxDouble);

        // Use the update method to save this new text to file.
        stateDao.update(testState);

        // Load a new instance of the StateDao.
        StateDao secondDao = ctx.getBean("stateDao", StateDao.class);

        // Pull a state  using the id number recorded earlier.
        State thirdState = secondDao.get(stateName);

        assertTrue(thirdState != null);

        // Check that the update method saved the new text.
        assertEquals(taxDouble, thirdState.getStateTax(), 1e-8);
        assertEquals(stateName, thirdState.getState());

        // Delete the test state.
        secondDao.delete(thirdState);

        // Load a third instance of the Dao and verify that 
        // the state was deleted from the file.
        StateDao thirdDao = ctx.getBean("stateDao", StateDao.class);
        assertEquals(thirdDao.get(stateName), null);
    }

    private Boolean verifyState(State state1, State state2) {
        
        if (state1 == null && state2 == null) {
            return true;
        }

        if (state1 == null || state2 == null) {
            return false;
        }

        assertEquals(state1.getStateName(), state2.getStateName());
        assertEquals(state1.getStateTax(), state2.getStateTax(), 0.005);

        return Objects.equals(state1, state2);
    }
}