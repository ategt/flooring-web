/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ATeg
 */
public class TextUtilitiesTest {
    
    public TextUtilitiesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of toTitleCase method, of class TextUtilities.
     */
    @Test
    public void testToTitleCaseWithNull() {
        System.out.println("toTitleCase");
        String givenString = null;
        String expResult = null;
        String result = TextUtilities.toTitleCase(givenString);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toTitleCase method, of class TextUtilities.
     */
    @Test
    public void testToTitleCaseWithBlank() {
        System.out.println("toTitleCase");
        String givenString = " ";
        String expResult = " ";
        String result = TextUtilities.toTitleCase(givenString);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toTitleCase method, of class TextUtilities.
     */
    @Test
    public void testToTitleCaseWithEmpty() {
        System.out.println("toTitleCase");
        String givenString = "";
        String expResult = "";
        String result = TextUtilities.toTitleCase(givenString);
        assertEquals(expResult, result);
    }
    
    
}
