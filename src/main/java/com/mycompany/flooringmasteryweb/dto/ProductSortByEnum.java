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
public enum ProductSortByEnum {
    SORT_BY_PRODUCT_NAME,
    SORT_BY_PRODUCT_NAME_INVERSE,
    SORT_BY_PRODUCT_COST,
    SORT_BY_PRODUCT_COST_INVERSE;

    public static ProductSortByEnum parse(String input) {
        if (input == null) {
            return null;
        }

        ProductSortByEnum currentEnum = null;

        if (input.toLowerCase().contains("name")) {
            currentEnum = SORT_BY_PRODUCT_NAME;
        } else if (input.toLowerCase().contains("cost")) {
            currentEnum = SORT_BY_PRODUCT_COST;
        } else {
            currentEnum = SORT_BY_PRODUCT_NAME;
        }

        if (input.toLowerCase().contains("inv") || input.toLowerCase().contains("rev")) {
            currentEnum = reverse(currentEnum);
        }
        return currentEnum;
    }

    public ProductSortByEnum reverse() {
        return reverse(this);
    }

    public static ProductSortByEnum reverse(ProductSortByEnum currentEnum) {
        switch (currentEnum) {
            case SORT_BY_PRODUCT_COST:
                return SORT_BY_PRODUCT_COST_INVERSE;
            case SORT_BY_PRODUCT_COST_INVERSE:
                return SORT_BY_PRODUCT_COST;
            case SORT_BY_PRODUCT_NAME:
                return SORT_BY_PRODUCT_NAME_INVERSE;
            case SORT_BY_PRODUCT_NAME_INVERSE:
                return SORT_BY_PRODUCT_NAME;
            default:
                throw new IndexOutOfBoundsException("No Possible Reverse Found.");
        }
    }
}