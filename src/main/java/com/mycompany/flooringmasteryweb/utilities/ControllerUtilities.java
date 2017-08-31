/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.ResultProperties;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author ATeg
 */
public class ControllerUtilities {

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

    public static void generatePagingLinks(int totalItems, ResultProperties resultProperties, HttpServletRequest request, UriComponentsBuilder uriComponentsBuilder, Map model) {
        generatePagingLinks(totalItems, resultProperties, request, uriComponentsBuilder, model, null);
    }

    public static void generatePagingLinks(
            int totalItems,
            ResultProperties resultProperties,
            HttpServletRequest request,
            UriComponentsBuilder uriComponentsBuilder,
            Map model,
            AddressSearchRequest addressSearchRequest
    ) {

        int totalAddresses = totalItems;
        int totalPages = totalAddresses / resultProperties.getResultsPerPage();
        int page = resultProperties.getPageNumber();

        String query = request.getQueryString();
        uriComponentsBuilder.query(query);
        String uri = request.getRequestURI();

        if (addressSearchRequest != null) {
            AddressSearchByOptionEnum addressSearchByOptionEnum = addressSearchRequest.searchBy();
            uriComponentsBuilder
                    .replaceQueryParam("searchBy", addressSearchByOptionEnum.value());

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