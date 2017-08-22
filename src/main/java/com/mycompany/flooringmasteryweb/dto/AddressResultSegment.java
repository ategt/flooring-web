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
public class AddressResultSegment {
    private Integer page;
    private Integer resultsPerPage;
    
    private AddressSortByEnum sortBy;

    public AddressResultSegment(){}
    
    public AddressResultSegment(Integer page, Integer resultsPerPage, AddressSortByEnum sortBy){
        this.page = page;
        this.resultsPerPage = resultsPerPage;
        this.sortBy = sortBy;
    }
    
    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return the resultsPerPage
     */
    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * @param resultsPerPage the resultsPerPage to set
     */
    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    /**
     * @return the sortBy
     */
    public AddressSortByEnum getSortBy() {
        return sortBy;
    }

    /**
     * @param sortBy the sortBy to set
     */
    public void setSortBy(AddressSortByEnum sortBy) {
        this.sortBy = sortBy;
    }
}
