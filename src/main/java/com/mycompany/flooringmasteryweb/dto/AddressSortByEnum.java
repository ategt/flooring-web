/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import com.google.common.base.Strings;

import java.util.Objects;

/**
 * @author ATeg
 */
public enum AddressSortByEnum {
    SORT_BY_LAST_NAME,
    SORT_BY_FIRST_NAME,
    SORT_BY_COMPANY,
    SORT_BY_ID,
    SORT_BY_LAST_NAME_INVERSE,
    SORT_BY_FIRST_NAME_INVERSE,
    SORT_BY_COMPANY_INVERSE,
    SORT_BY_ID_INVERSE;

    public static AddressSortByEnum parse(String input) {
        input = Strings.nullToEmpty(input);

        AddressSortByEnum currentEnum = null;

        if (input.toLowerCase().contains("id")) {
            currentEnum = SORT_BY_ID;
        } else if (input.toLowerCase().contains("last")) {
            currentEnum = SORT_BY_LAST_NAME;
        } else if (input.toLowerCase().contains("first")) {
            currentEnum = SORT_BY_FIRST_NAME;
        } else if (input.toLowerCase().contains("company")) {
            currentEnum = SORT_BY_COMPANY;
        } else {
            currentEnum = SORT_BY_ID;
        }

        if (input.toLowerCase().contains("inv") || input.toLowerCase().contains("rev")) {
            currentEnum = reverse(currentEnum);
        }

        return currentEnum;
    }

    public AddressSortByEnum reverse() {
        return reverse(this);
    }

    public static AddressSortByEnum reverse(AddressSortByEnum currentEnum) {
        switch (currentEnum) {
            case SORT_BY_COMPANY:
                return SORT_BY_COMPANY_INVERSE;
            case SORT_BY_COMPANY_INVERSE:
                return SORT_BY_COMPANY;
            case SORT_BY_FIRST_NAME:
                return SORT_BY_FIRST_NAME_INVERSE;
            case SORT_BY_FIRST_NAME_INVERSE:
                return SORT_BY_FIRST_NAME;
            case SORT_BY_ID:
                return SORT_BY_ID_INVERSE;
            case SORT_BY_ID_INVERSE:
                return SORT_BY_ID;
            case SORT_BY_LAST_NAME:
                return SORT_BY_LAST_NAME_INVERSE;
            case SORT_BY_LAST_NAME_INVERSE:
                return SORT_BY_LAST_NAME;
            default:
                throw new IndexOutOfBoundsException("No Possible Reverse Found.");
        }
    }
}
