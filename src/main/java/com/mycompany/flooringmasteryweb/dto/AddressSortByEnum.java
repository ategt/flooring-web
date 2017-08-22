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
public enum AddressSortByEnum {
    LAST_NAME,
    FIRST_NAME,
    COMPANY,
    ID,
    LAST_NAME_INVERSE,
    FIRST_NAME_INVERSE,
    COMPANY_INVERSE,
    ID_INVERSE;

    public AddressSortByEnum parse(String input) {
        if (input == null) {
            return null;
        }

        AddressSortByEnum currentEnum = null;

        if (input.toLowerCase().contains("id")) {
            currentEnum = ID;
        } else if (input.toLowerCase().contains("last")) {
            currentEnum = LAST_NAME;
        } else if (input.toLowerCase().contains("first")) {
            currentEnum = FIRST_NAME;
        } else if (input.toLowerCase().contains("company")) {
            currentEnum = COMPANY;
        } else {
            currentEnum = ID;
        }

        if (input.toLowerCase().contains("inverse") || input.toLowerCase().contains("reverse")) {
            currentEnum = reverse(currentEnum);
        }
        return currentEnum;
    }

    public AddressSortByEnum reverse(AddressSortByEnum currentEnum) {
        switch (currentEnum) {
            case COMPANY:
                return COMPANY_INVERSE;
            case COMPANY_INVERSE:
                return COMPANY;
            case FIRST_NAME:
                return FIRST_NAME_INVERSE;
            case FIRST_NAME_INVERSE:
                return FIRST_NAME;
            case ID:
                return ID_INVERSE;
            case ID_INVERSE:
                return ID;
            case LAST_NAME:
                return LAST_NAME_INVERSE;
            case LAST_NAME_INVERSE:
                return LAST_NAME;
            default:
                throw new IndexOutOfBoundsException("No Possible Reverse Found.");
        }
    }
}
