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
public interface SearchRequest {

    /**
     * @return the searchText
     */
    String getSearchText();

    ValueEnum searchBy();

    /**
     * @param searchText the searchText to set
     */
    void setSearchText(String searchText);
    
}
