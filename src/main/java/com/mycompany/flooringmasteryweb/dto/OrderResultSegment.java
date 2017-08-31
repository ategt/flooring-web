package com.mycompany.flooringmasteryweb.dto;

public class OrderResultSegment implements ResultSegement {

    private final Integer pageNumber;
    private final Integer resultsPerPage;
    private final OrderSortByEnum sortByEnum;

    public OrderResultSegment(OrderSortByEnum orderSortByEnum, Integer resultsPerPage, Integer pageNumber) {
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
        this.sortByEnum = orderSortByEnum;
    }

    /**
     * @return the pageNumber
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * @return the resultsPerPage
     */
    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * @return the sortByEnum
     */
    public OrderSortByEnum getSortByEnum() {
        return sortByEnum;
    }
}
