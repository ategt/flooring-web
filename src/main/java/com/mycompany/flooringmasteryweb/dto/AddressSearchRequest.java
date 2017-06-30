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
public class AddressSearchRequest {
    private String searchBy;
    private String searchText;

    /**
     * @return the searchBy
     */
    public String getSearchBy() {
        return searchBy;
    }

    /**
     * @param searchBy the searchBy to set
     */
    public void setSearchBy(String searchBy) {
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
