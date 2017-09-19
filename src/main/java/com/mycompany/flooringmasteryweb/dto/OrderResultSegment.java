package com.mycompany.flooringmasteryweb.dto;

public class OrderResultSegment implements ResultSegment {

    private final Integer pageNumber;
    private final Integer resultsPerPage;
    private final OrderSortByEnum sortByEnum;

    public OrderResultSegment(OrderSortByEnum orderSortByEnum, Integer pageNumber, Integer resultsPerPage) {
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
        this.sortByEnum = orderSortByEnum;
    }

    public OrderResultSegment(ResultSegment<OrderSortByEnum> resultSegment){
        this.pageNumber = resultSegment.getPageNumber();
        this.resultsPerPage = resultSegment.getResultsPerPage();
        this.sortByEnum = resultSegment.getSortByEnum();
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
