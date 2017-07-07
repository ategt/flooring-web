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
public class AddressSearchRequest {

    private ADDRESS_SEARCH_BY searchBy;
    private String searchText;

    public enum ADDRESS_SEARCH_BY {
        LAST_NAME("searchByLastName"),
        FIRST_NAME("searchByFirstName"),
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
        DEFAULT("searchByAll");

        private String searchString;

        private ADDRESS_SEARCH_BY(String searchString) {
            this.searchString = searchString;
        }

        public String value() {
            return searchString;
        }

        public static ADDRESS_SEARCH_BY parse(String input) {
            Optional<ADDRESS_SEARCH_BY> result = Arrays.stream(values()).filter(option -> option.value().equalsIgnoreCase(input)).findAny();
            
            if (!result.isPresent()){
                result = Arrays.stream(values()).filter(option -> option.name().equalsIgnoreCase(input)).findAny();
            }

            if (!result.isPresent()){
                result = Arrays.stream(values()).filter(option -> Integer.compare(option.ordinal(), Integer.parseInt(input)) == 0).findAny();
            }
            
            return result.orElse(LAST_NAME);
        }
    }

    public AddressSearchRequest() {
        this.searchText = "";
        this.searchBy = ADDRESS_SEARCH_BY.DEFAULT;
    }

    public AddressSearchRequest(String searchText, ADDRESS_SEARCH_BY searchBy) {
        this.searchText = searchText;
        this.searchBy = searchBy;
    }

    /**
     * @return the searchBy
     */
    public String getSearchBy() {
        if (searchBy == null){
            return ADDRESS_SEARCH_BY.DEFAULT.value(); 
        }
        return searchBy.searchString;
    }

    public ADDRESS_SEARCH_BY searchBy() {
        return searchBy;
    }

    /**
     * @param searchBy the searchBy to set
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = ADDRESS_SEARCH_BY.parse(searchBy);
    }

    public void setSearchByEnum(ADDRESS_SEARCH_BY searchBy) {
        this.searchBy = searchBy;
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}