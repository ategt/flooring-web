package com.mycompany.flooringmasteryweb.dto;

public class OrderResultSegment {
    Integer pageNumber;
    Integer resultsPerPage;
    OrderSortByEnum sortByEnum;

    public OrderResultSegment(OrderSortByEnum sortByEnum, Integer pageNumber, Integer resultsPerPage) {
        this(pageNumber, resultsPerPage, sortByEnum);
    }

    public OrderResultSegment(Integer pageNumber, Integer resultsPerPage, OrderSortByEnum sortByEnum) {
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
        this.sortByEnum = sortByEnum;
    }
}