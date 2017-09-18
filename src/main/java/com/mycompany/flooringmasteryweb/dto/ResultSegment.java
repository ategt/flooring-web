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
public interface ResultSegment<T extends Enum> {

    /**
     * @return the pageNumber
     */
    Integer getPageNumber();

    /**
     * @return the resultsPerPage
     */
    Integer getResultsPerPage();
    
    T getSortByEnum();

}
