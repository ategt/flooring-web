/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.exceptions.ConfigurationFileCorruptException;
import com.mycompany.flooringmasteryweb.exceptions.FileCreationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class StateDaoImplTest {

    ConfigDao configDao;

    public StateDaoImplTest() {
    }

    @Before
    public void setUp() {

        java.io.File testFile = new java.io.File("StatesTestData.txt");
        java.io.File backupTestFile = new java.io.File("StatesTestData-Backup.txt");
        backupTestFile.deleteOnExit();
        testFile.renameTo(backupTestFile);

        boolean isATest = true;
        ConfigDao configDao = null;

        try {
            configDao = new ConfigDao();
            configDao.get().setTaxesFile(testFile);
        } catch (ConfigurationFileCorruptException | FileCreationException ex) {
            Logger.getLogger(OrderDaoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Throwing This Exception Should Not Be Possible.\n" + ex.getMessage());
        }

        configDao.get().setInTestMode(isATest);
        this.configDao = configDao;

        StateDao instance = new StateDaoFileImpl(configDao);

        // If There is already an instance of SG in the Dao, delete it.
        State stateToDelete = instance.get("SG");
        if (stateToDelete != null) {
            instance.delete(stateToDelete);
        }

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        State state = new State();
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.create(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateB() {
        System.out.println("create");
        State state = null;
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.create(state);
        assertEquals(expResult, result);
    }


    @Test
    public void testGetA() {
        System.out.println("get - null");
        String state = null;
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.get(null);
        assertEquals(expResult, result);
    }
 
    @Test
    public void testGetC() {
        System.out.println("get - null");
        Object state = null;
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.get((String) state);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetD() {
        System.out.println("get - null");
        Object object = null;
        StateDao instance = new StateDaoFileImpl(configDao);
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
        assertEquals(expResult, result);
        
        
        instance.delete(state1);
        instance.delete(state2);
        instance.delete(state3);
        instance.delete(state4);
        
        
    }

    @Test
    public void testGetB() {
        System.out.println("get - null");
        String state = null;
        configDao.get().setInTestMode(false);
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.get(null);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateC() {
        System.out.println("create");
        State state = new State();
        state.setState("GH");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = state;
        State result = instance.create(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateD() {
        System.out.println("create");
        State state = new State();
        state.setState("Z");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.create(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateE() {
        System.out.println("create");
        State state = new State();
        state.setState("SW");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = state;
        State result = instance.create(state, state.getState());
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateF() {
        System.out.println("create");
        State state = new State();
        state.setState("Mexico");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.create(state, state.getState());
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateH() {
        System.out.println("create");
        State state = new State();
        state.setState("HQ");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = null;
        State result = instance.create(state, "HR");
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateG() {

        System.out.println("create");
        State state = new State();
        state.setState("SG");

        State otherState = new State();
        otherState.setState("SG");

        StateDao instance = new StateDaoFileImpl(configDao);

        State expResult = state;
        State result = instance.create(state);
        State otherResult = instance.create(otherState);

        assertEquals(expResult, result);
        assertEquals(null, otherResult);

        instance.delete(state);
        instance.delete(otherState);

    }

    @Test
    public void testDelete() {
        System.out.println("create");
        State state = new State();
        state.setState("SG");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = state;
        State result = instance.create(state);
        assertEquals(expResult, result);

        // Test get method.
        State returnedState = instance.get(state.getState());
        assertEquals(returnedState, result);
        instance.delete(state);

        returnedState = instance.get(state.getState());
        assertEquals(returnedState, null);

    }

    @Test
    public void testGet() {
        System.out.println("create");
        State state = new State();
        state.setState("SG");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = state;
        State result = instance.create(state);
        assertEquals(expResult, result);

        // Test get method.
        State returnedState = instance.get(state.getState());
        assertEquals(returnedState, result);
        instance.delete(state);

        returnedState = instance.get(state.getState());
        assertEquals(returnedState, null);

    }

    @Test
    public void testGet2() {
        System.out.println("create");
        State state = new State();
        state.setState("SG");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = state;
        String stateNameLowerCase = "sg";
        State result = instance.create(state);
        assertEquals(expResult, result);

        // Test get method.
        State returnedState = instance.get(stateNameLowerCase);
        assertEquals(returnedState, result);
        instance.delete(state);

        returnedState = instance.get(stateNameLowerCase);
        assertEquals(returnedState, null);

    }

    @Test
    public void testGet3() {
        System.out.println("create");
        State state = new State();
        state.setState("SG");
        StateDao instance = new StateDaoFileImpl(configDao);
        State expResult = state;
        String stateNameLowerCase = "sG";
        State result = instance.create(state);
        assertEquals(expResult, result);

        // Test get method.
        State returnedState = instance.get(stateNameLowerCase);
        assertEquals(returnedState, result);
        instance.delete(state);

        returnedState = instance.get(stateNameLowerCase);
        assertEquals(returnedState, null);

    }

    @Test
    public void testSize() {

        State state = new State();
        state.setState("DQ");
        StateDao instance = new StateDaoFileImpl(configDao);

        instance.create(state);

        State secondState = new State();
        secondState.setState("SG");

        State thirdState = new State();
        thirdState.setState("FG");

        instance.create(secondState);
        instance.create(thirdState);

        assertEquals(3, instance.size());

        assertTrue(instance.getList().contains("SG"));
        assertTrue(instance.getList().contains("FG"));
        assertTrue(instance.getList().contains("DQ"));

    }

    @Test
    public void testEncodeAndDecode() {

        // The true parameter in the StateDao constructor signifies a test.
        StateDao stateDao = new StateDaoFileImpl(configDao);
        State testState = new State();

        String stateName = "UK";
        testState.setState(stateName);

        // Create the file in the Dao.
        State returnedState = stateDao.create(testState);

        // Verify that the state object that the create method passed back
        // was the same one it was given.
        assertEquals(testState, returnedState);

        double taxDouble = 8.25;
        returnedState.setStateTax(taxDouble);

        // Use the update method to save this new text to file.
        stateDao.update(testState);

        // Load a new instance of the StateDao.
        StateDao secondDao = new StateDaoFileImpl(configDao);

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
        StateDao thirdDao = new StateDaoFileImpl(configDao);
        assertEquals(thirdDao.get(stateName), null);

    }

}
