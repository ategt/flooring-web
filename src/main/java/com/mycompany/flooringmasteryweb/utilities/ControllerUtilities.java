/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.ResultSegement;
import com.mycompany.flooringmasteryweb.dto.SearchRequest;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.dto.ValueEnum;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author ATeg
 */
public class ControllerUtilities {
    public static Integer loadDefaultPageNumber(ApplicationContext ctx, Integer page) throws BeansException {
        if (page == null) {
            int defaultStartingPage = ctx.getBean("defaultStartingPage", Integer.class);
            page = defaultStartingPage;
        }
        return page;
    }

    public static Integer loadDefaultResults(ApplicationContext ctx, Integer resultsPerPage, Integer resultsPerPageCookie) throws BeansException {
        if (resultsPerPage == null) {
            if (resultsPerPageCookie == null) {
                int defaultResultsPerPage = ctx.getBean("defaultResultsPerPage", Integer.class);
                resultsPerPage = defaultResultsPerPage;
            } else {
                resultsPerPage = resultsPerPageCookie;
            }
        }
        return resultsPerPage;
    }

    public static Integer updateResultsCookie(Integer resultsPerPage, String resultCookieName, HttpServletResponse response) {
        if (resultsPerPage != null && resultsPerPage > 0) {
            response.addCookie(new Cookie(resultCookieName, Integer.toString(resultsPerPage)));
        }
        return resultsPerPage;
    }

    public static void generatePagingLinks(ApplicationContext ctx, int totalItems, ResultSegement resultProperties, HttpServletRequest request, UriComponentsBuilder uriComponentsBuilder, Map model) {
        generatePagingLinks(ctx, totalItems, resultProperties, request, uriComponentsBuilder, model, null);
    }

    public static void generatePagingLinks(
            ApplicationContext ctx,
            int totalItems,
            ResultSegement resultProperties,
            HttpServletRequest request,
            UriComponentsBuilder uriComponentsBuilder,
            Map model,
            SearchRequest addressSearchRequest
    ) {

        int totalAddresses = totalItems;

        int resultsPerPage = resultProperties.getResultsPerPage();

        if (resultsPerPage < 1) {
            resultsPerPage = ctx.getBean("defaultResultsPerPage", Integer.class);
        }
        
        int totalPages = totalAddresses / resultsPerPage;
        int page = resultProperties.getPageNumber();

        String query = request.getQueryString();
        uriComponentsBuilder.query(query);
        String uri = request.getRequestURI();

        if (addressSearchRequest != null) {
            ValueEnum valueEnum = addressSearchRequest.searchBy();
            uriComponentsBuilder
                    .replaceQueryParam("searchBy", valueEnum.value());

            String searchQuery = addressSearchRequest.getSearchText();
            uriComponentsBuilder
                    .replaceQueryParam("searchText", searchQuery);
        }

        boolean displayFirstPage = page > 0;
        if (displayFirstPage) {
            String firstQuery = uri + "?" + uriComponentsBuilder
                    .replaceQueryParam("page", 0)
                    .build()
                    .getQuery();
            model.put("first_link", firstQuery);
        }

        boolean displayPreviousPage = page > 0;
        if (displayPreviousPage) {
            String prevQuery = uri + "?" + uriComponentsBuilder
                    .replaceQueryParam("page", page - 1)
                    .build()
                    .getQuery();
            model.put("prev_link", prevQuery);
        }

        boolean displayNextPage = page < totalPages;
        if (displayNextPage) {
            String nextQuery = uri + "?" + uriComponentsBuilder
                    .replaceQueryParam("page", page + 1)
                    .build()
                    .getQuery();
            model.put("next_link", nextQuery);
        }

        boolean displayLastPage = page < totalPages;
        if (displayLastPage) {
            String lastQuery = uri + "?" + uriComponentsBuilder
                    .replaceQueryParam("page", totalPages)
                    .build()
                    .getQuery();
            model.put("last_link", lastQuery);
        }

        if (displayNextPage && displayPreviousPage) {
            model.put("current_page", page);
        }
    }
}