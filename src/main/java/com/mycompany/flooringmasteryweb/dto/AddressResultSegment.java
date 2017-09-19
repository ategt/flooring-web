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
public class AddressResultSegment implements ResultSegment {

    private final Integer pageNumber;
    private final Integer resultsPerPage;
    private final AddressSortByEnum sortByEnum;

    public AddressResultSegment(AddressSortByEnum sortByEnum, Integer pageNumber, Integer resultsPerPage) {
        this(pageNumber, resultsPerPage, sortByEnum);
    }

    public AddressResultSegment(Integer pageNumber, Integer resultsPerPage, AddressSortByEnum sortByEnum) {
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
        this.sortByEnum = sortByEnum;
    }

    /**
     * @return the pageNumber
     */
    @Override
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * @return the resultsPerPage
     */
    @Override
    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * @return the sortByEnum
     */    
    public AddressSortByEnum getSortByEnum() {
        return sortByEnum;
    }
}
