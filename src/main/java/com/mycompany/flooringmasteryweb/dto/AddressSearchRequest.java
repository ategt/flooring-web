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
public class AddressSearchRequest implements SearchRequest {

    private AddressSearchByOptionEnum searchBy;
    private String searchText;

    public AddressSearchRequest() {
        this.searchText = "";
        this.searchBy = AddressSearchByOptionEnum.DEFAULT;
    }

    public AddressSearchRequest(String searchText, AddressSearchByOptionEnum searchBy) {
        this.searchText = searchText;
        this.searchBy = searchBy;
    }

    /**
     * @return the searchBy
     */
    public String getSearchBy() {
        if (searchBy == null){
            return AddressSearchByOptionEnum.DEFAULT.value(); 
        }
        return searchBy.value();
    }

    @Override
    public AddressSearchByOptionEnum searchBy() {
        return searchBy;
    }

    /**
     * @param searchBy the searchBy to set
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = AddressSearchByOptionEnum.parse(searchBy);
    }

    public void setSearchByEnum(AddressSearchByOptionEnum searchBy) {
        this.searchBy = searchBy;
    }

    /**
     * @return the searchText
     */
    @Override
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
