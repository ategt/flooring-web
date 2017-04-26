/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class StateUtilitiesTest {
    
    public StateUtilitiesTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of stateFromAbbr method, of class StateUtilities.
     */
    @Test
    public void testStateFromAbbr() {
        System.out.println("stateFromAbbr");
        String abbr = "UT";
        String expResult = "Utah";
        String result = StateUtilities.stateFromAbbr(abbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }
    
    /**
     * Test of stateFromAbbr method, of class StateUtilities.
     */
    @Test
    public void testStateFromAbbrZ() {
        System.out.println("stateFromAbbr");
        String abbr = null;
        String expResult = null;
        String result = StateUtilities.stateFromAbbr(abbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }
    
    /**
     * Test of stateFromAbbr method, of class StateUtilities.
     */
    @Test
    public void testStateFromAbbrA() {
        System.out.println("stateFromAbbr");
        String abbr = "Ut";
        String expResult = "Utah";
        String result = StateUtilities.stateFromAbbr(abbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }
    
    /**
     * Test of stateFromAbbr method, of class StateUtilities.
     */
    @Test
    public void testStateFromAbbrB() {
        System.out.println("stateFromAbbr");
        String abbr = "ut";
        String expResult = "Utah";
        String result = StateUtilities.stateFromAbbr(abbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of abbrFromState method, of class StateUtilities.
     */
    @Test
    public void testAbbrFromState() {
        System.out.println("abbrFromState");
        String stateName = "Utah";
        String expResult = "UT";
        String result = StateUtilities.abbrFromState(stateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of abbrFromState method, of class StateUtilities.
     */
    @Test
    public void testAbbrFromStateA() {
        System.out.println("abbrFromState");
        String stateName = "sddfrgthyjk";
        String expResult = null;
        String result = StateUtilities.abbrFromState(stateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of abbrFromState method, of class StateUtilities.
     */
    @Test
    public void testAbbrFromStateB() {
        System.out.println("abbrFromState");
        String stateName = null;
        String expResult = null;
        String result = StateUtilities.abbrFromState(stateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of bestGuessStateName method, of class StateUtilities.
     */
    @Test
    public void testBestGuessStateName() {
        System.out.println("bestGuessStateName");
        String stateName = "ken";
        String expResult = "Kentucky";
        String result = StateUtilities.bestGuessStateName(stateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of bestGuessStateName method, of class StateUtilities.
     */
    @Test
    public void testBestGuessStateNameD() {
        System.out.println("bestGuessStateName");
        String stateName = "AK";
        String expResult = "Alaska";
        String result = StateUtilities.bestGuessStateName(stateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of bestGuessStateName method, of class StateUtilities.
     */
    @Test
    public void testBestGuessStateNameA() {
        System.out.println("bestGuessStateName");
        String stateName = "";
        String expResult = null;
        String result = StateUtilities.bestGuessStateName(stateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    /**
     * Test of bestGuessStateName method, of class StateUtilities.
     */
    @Test
    public void testBestGuessStateNameB() {
        System.out.println("bestGuessStateName");
        String stateName = null;
        String expResult = null;
        String result = StateUtilities.bestGuessStateName(stateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of guessStateName method, of class StateUtilities.
     */
    @Test
    public void testGuessStateName() {
        System.out.println("guessStateName");
        String stateName = "";
        int expResult = 71;
        List<String> result = StateUtilities.guessStateName(stateName);
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of guessStateName method, of class StateUtilities.
     */
    @Test
    public void testGuessStateNameA() {
        System.out.println("guessStateName");
        String stateName = null;
        int expResult = 0;
        List<String> result = StateUtilities.guessStateName(stateName);
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of validStateAbbr method, of class StateUtilities.
     */
    @Test
    public void testValidStateAbbr() {
        System.out.println("validStateAbbr");
        String stateAbbr = "";
        boolean expResult = false;
        boolean result = StateUtilities.validStateAbbr(stateAbbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of validStateAbbr method, of class StateUtilities.
     */
    @Test
    public void testValidStateAbbrA() {
        System.out.println("validStateAbbr");
        String stateAbbr = null;
        boolean expResult = false;
        boolean result = StateUtilities.validStateAbbr(stateAbbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of validStateAbbr method, of class StateUtilities.
     */
    @Test
    public void testValidStateAbbrQ() {
        System.out.println("validStateAbbr");
        String stateAbbr = "GO";
        boolean expResult = false;
        boolean result = StateUtilities.validStateAbbr(stateAbbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of validStateAbbr method, of class StateUtilities.
     */
    @Test
    public void testValidStateAbbrB() {
        System.out.println("validStateAbbr");
        String stateAbbr = "OH";
        boolean expResult = true;
        boolean result = StateUtilities.validStateAbbr(stateAbbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of validStateAbbr method, of class StateUtilities.
     */
    @Test
    public void testValidStateAbbrC() {
        System.out.println("validStateAbbr");
        String stateAbbr = "UT";
        boolean expResult = true;
        boolean result = StateUtilities.validStateAbbr(stateAbbr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInput() {
        System.out.println("validStateInput");
        String input = "";
        boolean expResult = false;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    

    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInputZ() {
        System.out.println("validStateInput");
        String input = null;
        boolean expResult = false;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInputA() {
        System.out.println("validStateInput");
        String input = " ";
        boolean expResult = false;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInputB() {
        System.out.println("validStateInput");
        String input = "  ";
        boolean expResult = false;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInputC() {
        System.out.println("validStateInput");
        String input = "3";
        boolean expResult = false;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInputD() {
        System.out.println("validStateInput");
        String input = "asdfghjkl";
        boolean expResult = false;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInputE() {
        System.out.println("validStateInput");
        String input = "Flor";
        boolean expResult = true;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    
    /**
     * Test of validStateInput method, of class StateUtilities.
     */
    @Test
    public void testValidStateInputF() {
        System.out.println("validStateInput");
        String input = "Flor";
        boolean expResult = true;
        boolean result = StateUtilities.validStateInput(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        //public static Map<String, String> stringMapReverser(Map<String, String> stringMap)
    }
    
}
