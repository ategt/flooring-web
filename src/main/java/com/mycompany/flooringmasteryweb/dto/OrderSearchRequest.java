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
public class OrderSearchRequest implements SearchRequest{

    private OrderSearchByOptionEnum searchBy;
    private String searchText;

    public OrderSearchRequest() {
        this.searchText = "";
        this.searchBy = OrderSearchByOptionEnum.EVERYTHING;
    }

    public OrderSearchRequest(String searchText, OrderSearchByOptionEnum searchBy) {
        this.searchText = searchText;
        this.searchBy = searchBy;
    }

    /**
     * @return the searchBy
     */
    public OrderSearchByOptionEnum getSearchBy() {
        return searchBy;
    }

    /**
     * @param searchBy the searchBy to set
     */
    public void setSearchBy(OrderSearchByOptionEnum searchBy) {
        this.searchBy = searchBy;
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    @Override
    public ValueEnum searchBy() {
        return searchBy;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
