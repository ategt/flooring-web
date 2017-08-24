/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author ATeg
 */
public enum AddressSearchByOptionEnum {
    LAST_NAME("searchByLastName"),
    FIRST_NAME("searchByFirstName"),
    FULL_NAME("searchByFullName"),
    CITY("searchByCity"),
    STATE("searchByState"),
    ZIP("searchByZip"),
    COMPANY("searchByCompany"),
    STREET_NUMBER("searchByStreetNumber"),
    STREET_NAME("searchByStreetName"),
    STREET("searchByStreet"),
    NAME("searchByName"),
    NAME_OR_COMPANY("searchByNameOrCompany"),
    ALL("searchByAll"),
    ANY("searchByAny"),
    DEFAULT("searchByAll");

    private String searchString;

    private AddressSearchByOptionEnum(String searchString) {
        this.searchString = searchString;
    }

    public String value() {
        return searchString;
    }

    public static AddressSearchByOptionEnum parse(String input) {
        Optional<AddressSearchByOptionEnum> result = Arrays.stream(values()).filter(option -> option.value().equalsIgnoreCase(input)).findAny();

        if (!result.isPresent()) {
            result = Arrays.stream(values()).filter(option -> option.name().equalsIgnoreCase(input)).findAny();
        }

        if (!result.isPresent()) {
            result = Arrays.stream(values()).filter(option -> Integer.compare(option.ordinal(), Integer.parseInt(input)) == 0).findAny();
        }

        return result.orElse(LAST_NAME);
    }
}
