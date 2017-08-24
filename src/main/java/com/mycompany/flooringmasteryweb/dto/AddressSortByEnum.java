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
    SORT_BY_COMPANY("company", 2),
    SORT_BY_ID("id", 3),
    SORT_BY_FIRST_NAME("first_name", 1),
    SORT_BY_LAST_NAME("last_name", 0);

    private final String sortString;
    private final Integer sortByInt;
    
    private static final AddressSortByEnum DEFAULT = AddressSortByEnum.SORT_BY_ID;

    private AddressSortByEnum(String sortString, Integer sortByInt) {
        this.sortString = sortString;
        this.sortByInt = sortByInt;
    }

    public String value() {
        return sortString;
    }

    public int intValue() {
        return sortByInt;
    }

    public static AddressSortByEnum parse(Integer input) {
        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            if (addressesSortBy.intValue() == input) {
                return addressesSortBy;
            }
        }
        return DEFAULT;
    }

    public static AddressSortByEnum parse(String input) {
        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            if (addressesSortBy.value().equalsIgnoreCase(input)) {
                return addressesSortBy;
            }
        }

        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            if (addressesSortBy.name().equalsIgnoreCase(input)) {
                return addressesSortBy;
            }
        }

        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            try {
                if (addressesSortBy.ordinal() == Integer.parseInt(input)) {
                    return addressesSortBy;
                }
            } catch (NumberFormatException ex) {
            }
        }

        return DEFAULT;
    }
}
