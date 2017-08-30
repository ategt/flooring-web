/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

/**
 *
 * @author ATeg
 */
public enum OrderSortByEnum {
    SORT_BY_ID,
    SORT_BY_NAME,
    SORT_BY_PRODUCT,
    SORT_BY_STATE,
    SORT_BY_DATE,
    SORT_BY_ID_INVERSE,
    SORT_BY_NAME_INVERSE,
    SORT_BY_PRODUCT_INVERSE,
    SORT_BY_STATE_INVERSE,
    SORT_BY_DATE_INVERSE;

    public static OrderSortByEnum parse(String input) {
        if (input == null) {
            return null;
        }

        OrderSortByEnum currentEnum = null;

        if (input.toLowerCase().contains("id")) {
            currentEnum = SORT_BY_ID;
        } else if (input.toLowerCase().contains("name")) {
            currentEnum = SORT_BY_NAME;
        } else if (input.toLowerCase().contains("product")) {
            currentEnum = SORT_BY_PRODUCT;
        } else if (input.toLowerCase().contains("state")) {
            currentEnum = SORT_BY_STATE;
        } else if (input.toLowerCase().contains("date")) {
            currentEnum = SORT_BY_DATE;
        } else {
            currentEnum = SORT_BY_ID;
        }

        if (input.toLowerCase().contains("inv") || input.toLowerCase().contains("rev")) {
            currentEnum = reverse(currentEnum);
        }
        return currentEnum;
    }

    public OrderSortByEnum reverse() {
        return reverse(this);
    }

    public static OrderSortByEnum reverse(OrderSortByEnum currentEnum) {
        switch (currentEnum) {
            case SORT_BY_DATE:
                return SORT_BY_DATE_INVERSE;
            case SORT_BY_DATE_INVERSE:
                return SORT_BY_DATE;
            case SORT_BY_ID:
                return SORT_BY_ID_INVERSE;
            case SORT_BY_ID_INVERSE:
                return SORT_BY_ID;
            case SORT_BY_NAME:
                return SORT_BY_NAME_INVERSE;
            case SORT_BY_NAME_INVERSE:
                return SORT_BY_NAME;
            case SORT_BY_PRODUCT:
                return SORT_BY_PRODUCT_INVERSE;
            case SORT_BY_PRODUCT_INVERSE:
                return SORT_BY_PRODUCT;
            case SORT_BY_STATE:
                return SORT_BY_STATE_INVERSE;
            case SORT_BY_STATE_INVERSE:
                return SORT_BY_STATE;
            default:
                throw new IndexOutOfBoundsException("No Possible Reverse Found.");
        }
    }
}