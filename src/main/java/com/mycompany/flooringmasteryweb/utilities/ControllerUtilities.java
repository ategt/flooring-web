/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.ResultSegment;
import com.mycompany.flooringmasteryweb.dto.SearchRequest;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.dto.ValueEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ATeg
 */
public class ControllerUtilities {
    public static final String RESULTS_COOKIE_NAME = "results_cookie";
    public static final String SORT_COOKIE_NAME = "sort_cookie";

    public static void loadStateCommandsToMap(StateDao stateDao, Map model) {
        List<StateCommand> stateCommands = stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());

        model.put("stateCommands", stateCommands);
    }

    public static List<StateCommand> stateList(StateDao stateDao) {
        return stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());
    }

    public static Integer loadDefaultPageNumber(ApplicationContext ctx, Integer page) throws BeansException {
        if (page == null) {
            int defaultStartingPage = ctx.getBean("defaultStartingPage", Integer.class);
            page = defaultStartingPage;
        }
        return page;
    }

    public static Integer loadDefaultResults(ApplicationContext ctx, Integer resultsPerPage, Cookie resultsPerPageCookie) throws BeansException {
        final String DEFAULT_RESULTS_PER_PAGE = "defaultResultsPerPage";
        return getIntegerWithContext(ctx, resultsPerPage, resultsPerPageCookie, DEFAULT_RESULTS_PER_PAGE);
    }

    private static Integer getIntegerWithContext(ApplicationContext ctx, Integer resultsPerPage, Cookie resultsPerPageCookie, String DEFAULT_RESULTS_PER_PAGE) {
        if (resultsPerPage == null) {
            if (resultsPerPageCookie == null) {
                int defaultResultsPerPage = ctx.getBean(DEFAULT_RESULTS_PER_PAGE, Integer.class);
                resultsPerPage = defaultResultsPerPage;
            } else {
                if (Objects.nonNull(resultsPerPageCookie))
                    try {
                        resultsPerPage = Integer.parseInt(resultsPerPageCookie.getValue());
                    } catch (NumberFormatException ex) {
                        resultsPerPage = null;
                    }
            }
        }
        return resultsPerPage;
    }

    public static Integer loadAllPageNumber(ApplicationContext ctx, Integer page) throws BeansException {
        if (page == null) {
            int defaultStartingPage = ctx.getBean("allStartingPage", Integer.class);
            page = defaultStartingPage;
        }
        return page;
    }

    public static Integer loadAllResults(ApplicationContext ctx, Integer resultsPerPage, Cookie resultsPerPageCookie) throws BeansException {
        final String DEFAULT_RESULTS_PER_PAGE = "allResultsPerPage";
        return getIntegerWithContext(ctx, resultsPerPage, resultsPerPageCookie, DEFAULT_RESULTS_PER_PAGE);
    }

    public static Integer updateResultsCookie(Integer resultsPerPage, String resultCookieName, HttpServletResponse response) {
        if (resultsPerPage != null && resultsPerPage > 0) {
            response.addCookie(new Cookie(resultCookieName, Integer.toString(resultsPerPage)));
        }
        return resultsPerPage;
    }

    public static void generatePagingLinks(ApplicationContext ctx, int totalItems, ResultSegment resultProperties, HttpServletRequest request, UriComponentsBuilder uriComponentsBuilder, Map model) {
        generatePagingLinks(ctx, totalItems, resultProperties, request, uriComponentsBuilder, model, null);
    }

    public static void generatePagingLinks(
            ApplicationContext ctx,
            int totalItems,
            ResultSegment resultProperties,
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

        uriComponentsBuilder.replaceQueryParam("sort_by", "");

                                        loadPagingLinkToMap("first_link", page > 0, 0, uriComponentsBuilder, model, uri);
        boolean displayPreviousPage =   loadPagingLinkToMap("prev_link", page > 0, page - 1, uriComponentsBuilder, model, uri);
        boolean displayNextPage     =   loadPagingLinkToMap("next_link", page < totalPages, page + 1, uriComponentsBuilder, model, uri);
                                        loadPagingLinkToMap("last_link", page < totalPages, totalPages, uriComponentsBuilder, model, uri);

        if (displayNextPage && displayPreviousPage) {
            model.put("current_page", page);
        }
    }

    public static boolean loadPagingLinkToMap(String modelKey,
                                              boolean viewCondition,
                                              int destinationPageNumber,
                                              UriComponentsBuilder uriComponentsBuilder,
                                              Map model,
                                              String uri) {
        if (viewCondition) {
            String linkQuery = uri + "?" + uriComponentsBuilder
                    .replaceQueryParam("page", destinationPageNumber)
                    .build()
                    .getQuery();
            model.put(modelKey, linkQuery);
        }
        return viewCondition;
    }
}
