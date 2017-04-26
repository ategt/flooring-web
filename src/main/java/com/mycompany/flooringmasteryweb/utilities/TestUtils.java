/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.State;

/**
 *
 * @author apprentice
 */
public class TestUtils {

    public static Boolean isStateEqual(State state1, State state2) {
        if (state1 == null && state2 == null) {
            return true;
        }

        if (state1 == null || state2 == null) {
            return false;
        }

        boolean valid = true;

        if (!state1.getStateName().equals(state2.getStateName())) {
            valid = false;
        }

        if (state1.getStateTax() != state2.getStateTax()) {
            valid = false;
        }

        return valid;
    }

    public static Boolean isProductEqual(Product product1, Product product2) {
        if (product1 == null && product2 == null) {
            return true;
        }

        if (product1 == null || product2 == null) {
            return false;
        }

        boolean valid = true;

        if (product1.getLaborCost() != product2.getLaborCost()) {
            valid = false;
        }

        if (!product1.getProductName().equals(product2.getProductName())) {
            valid = false;
        }

        if (product1.getCost() != product2.getCost()) {
            valid = false;
        }

        return valid;
    }
}
