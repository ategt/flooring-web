/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
import com.mycompany.flooringmasteryweb.dto.ResultProperties;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author ATeg
 */
@Controller
@RequestMapping(value = "/address")
public class AddressController {

    private final AddressDao addressDao;
    private ApplicationContext ctx;

    private final String RESULTS_COOKIE_NAME = "results_cookie";
    private final String SORT_COOKIE_NAME = "sort_cookie";

    @Inject
    public AddressController(AddressDao addressDao) {
        this.addressDao = addressDao;
        ctx = com.mycompany.flooringmasteryweb.aop.ApplicationContextProvider.getApplicationContext();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request,
            Map model) {

        ResultProperties resultProperties = processResultPropertiesWithContextDefaults(resultsPerPage, resultsPerPageCookie, page, sortBy, response, sortCookie);

        List<Address> addresses = addressDao.getAddressesSortedByParameter(resultProperties);

        generatePagingLinks(resultProperties, request, uriComponentsBuilder, model);

        model.put("addresses", addresses);
        return "address\\index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Address[] index(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            HttpServletResponse response) {

        ResultProperties resultProperties = processResultPropertiesWithAllAsDefault(
                sortBy, 
                response, 
                sortCookie, 
                page, 
                resultsPerPage, 
                resultsPerPageCookie);

        List<Address> addresses = addressDao.getAddressesSortedByParameter(resultProperties);

        return addresses.toArray(new Address[addresses.size()]);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Address create(@Valid @RequestBody Address address) {
        return addressDao.create(address);
    }

    @RequestMapping(value = "/{input}/search", method = RequestMethod.GET)
    @ResponseBody
    public Address guessWithAjax(@PathVariable("input") String addressInput) {
        Address contact = addressDao.get(addressInput);

        return contact;
    }

    @RequestMapping(value = "/{input}/name_completion", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> listNamesWithAjax(
            @PathVariable("input") String addressInput,
            @RequestParam(name = "limit", required = false) Integer addressLimit) {

        if (addressLimit == null) {
            addressLimit = 30;
        }

        Set<String> names = addressDao.getCompletionGuesses(addressInput, addressLimit);

        return names;
    }

    @RequestMapping(value = "/name_completion", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> altListNamesWithAjax(
            @RequestParam(name = "query", required = false) String addressInput,
            @RequestParam(name = "term", required = false) String altAddressInput,
            @RequestParam(name = "limit", required = false) Integer addressLimit) {

        if (addressLimit == null) {
            addressLimit = 30;
        }

        String input = addressInput;
        if (addressInput == null) {
            input = altAddressInput;
        }

        Set<String> names = addressDao.getCompletionGuesses(input, addressLimit);

        return names;
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public Address editSubmitWithAjax(@Valid @RequestBody Address address) {
        addressDao.update(address);
        return address;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Address deleteWithAjax(@PathVariable("id") Integer addressId) {
        return addressDao.delete(addressId);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer contactId, Map model) {
        loadAddress(contactId, model);
        return "address\\edit";
    }

    private void loadAddress(Integer contactId, Map model) {
        Address address = addressDao.get(contactId);
        model.put("address", address);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String update(@PathVariable("id") Integer contactId, @ModelAttribute Address address, Map model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getErrorCount();
            List<ObjectError> errors = bindingResult.getAllErrors();

            String errorString = "";
            for (ObjectError error : errors) {
                errorString += error.getDefaultMessage() + "<br />";
            }

            model.put("errors", errorString);

            loadAddress(contactId, model);
            return "address\\edit";
        } else {
            addressDao.update(address);
            return "redirect:/address/" + address.getId();
        }
    }

    private ResultProperties processResultPropertiesWithContextDefaults(
            Integer resultsPerPage, 
            Integer resultsPerPageCookie, 
            Integer page, 
            String sortBy, 
            HttpServletResponse response, 
            String sortCookie
    ) throws BeansException {
        
        resultsPerPage = loadDefaultResults(resultsPerPage, resultsPerPageCookie);
        page = loadDefaultPageNumber(page);
        ResultProperties resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return resultProperties;
    }

    private ResultProperties processResultPropertiesWithAllAsDefault(
            String sortBy,
            HttpServletResponse response,
            String sortCookie,
            Integer page,
            Integer resultsPerPage,
            Integer resultsPerPageCookie) {

        resultsPerPage = loadDefaultResults(resultsPerPage, resultsPerPageCookie);
        page = loadDefaultPageNumber(page);
        ResultProperties resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return resultProperties;
    }

    private ResultProperties processResultProperties(String sortBy, HttpServletResponse response, String sortCookie, Integer page, Integer resultsPerPage) {
        AddressSortByEnum sortEnum = updateSortCookie(sortBy, response, sortCookie);

        ResultProperties resultProperties = new ResultProperties(sortEnum, page, resultsPerPage);

        updateResultsCookie(resultProperties.getResultsPerPage(), response);
        return resultProperties;
    }

    private AddressSortByEnum updateSortCookie(String sortBy, HttpServletResponse response, String sortCookie) {
        if (sortBy != null) {
            response.addCookie(new Cookie(SORT_COOKIE_NAME, sortBy));
        } else if (sortCookie != null) {
            sortBy = sortCookie;
        }

        return AddressSortByEnum.parse(sortBy);
    }

    private Integer updateResultsCookie(Integer resultsPerPage, HttpServletResponse response) {
        if (resultsPerPage != null) {
            response.addCookie(new Cookie(RESULTS_COOKIE_NAME, Integer.toString(resultsPerPage)));
        }
        return resultsPerPage;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = {"Content-type=application/json", "Accept=application/json"})
    @ResponseBody
    public List<Address> search(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @Valid @RequestBody AddressSearchRequest addressSearchRequest,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        ResultProperties resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);

        List<Address> addresses = searchDatabase(addressSearchRequest, resultProperties);

        return addresses;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Address> search(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @ModelAttribute AddressSearchRequest addressSearchRequest,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            HttpServletResponse response
    ) {
        ResultProperties resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);

        List<Address> addresses = searchDatabase(addressSearchRequest, resultProperties);

        return addresses;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @ModelAttribute AddressSearchRequest addressSearchRequest,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            HttpServletResponse response,
            HttpServletRequest request,
            UriComponentsBuilder uriComponentsBuilder,
            Map model
    ) {

        ResultProperties resultProperties = processResultPropertiesWithContextDefaults(resultsPerPage, resultsPerPageCookie, page, sortBy, response, sortCookie);

        List<Address> addresses = searchDatabase(addressSearchRequest, resultProperties);

        generatePagingLinks(resultProperties, request, uriComponentsBuilder, model, addressSearchRequest);

        model.put("addresses", addresses);

        return "address\\search";

    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @Valid @ModelAttribute AddressSearchRequest addressSearchRequest,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request,
            Map model) {

        ResultProperties resultProperties = processResultPropertiesWithContextDefaults(resultsPerPage, resultsPerPageCookie, page, sortBy, response, sortCookie);

        List<Address> addresses;

        if (addressSearchRequest == null) {
            addresses = addressDao.list(resultProperties);
        } else {
            addresses = searchDatabase(addressSearchRequest, resultProperties);
        }

        //generatePagingLinks(resultProperties, request, uriComponentsBuilder, model);
        generatePagingLinks(resultProperties, request, uriComponentsBuilder, model, addressSearchRequest);

        model.put("addresses", addresses);

        return "address\\search";
    }

    private List<Address> searchDatabase(AddressSearchRequest searchRequest, ResultProperties resultProperties) {
        return addressDao.search(searchRequest,
                resultProperties);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Integer addressId,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            HttpServletResponse response,
            Map model) {

        Address address = addressDao.get(addressId);

        ResultProperties resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        List<Address> addresses = addressDao.list(resultProperties);

        model.put("address", address);
        model.put("addresses", addresses);

        return "address\\show";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Address show(@PathVariable("id") Integer addressId) {
        Address contact = addressDao.get(addressId);

        return contact;
    }

    @RequestMapping(value = "/size", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Integer size(HttpServletRequest request, HttpServletResponse response) {
        String acceptHeader = request.getHeader("Accept");
        if (Objects.nonNull(acceptHeader) && acceptHeader.equalsIgnoreCase("application/json")) {
            return addressDao.size();
        } else {
            response.setStatus(404);
            return null;
        }
    }

    private Integer loadDefaultPageNumber(Integer page) throws BeansException {
        if (page == null) {
            int defaultStartingPage = ctx.getBean("defaultStartingPage", Integer.class);
            page = defaultStartingPage;
        }
        return page;
    }

    private Integer loadDefaultResults(Integer resultsPerPage, Integer resultsPerPageCookie) throws BeansException {
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

    private void generatePagingLinks(ResultProperties resultProperties, HttpServletRequest request, UriComponentsBuilder uriComponentsBuilder, Map model) {
        generatePagingLinks(resultProperties, request, uriComponentsBuilder, model, null);
    }

    private void generatePagingLinks(
            ResultProperties resultProperties,
            HttpServletRequest request,
            UriComponentsBuilder uriComponentsBuilder,
            Map model,
            AddressSearchRequest addressSearchRequest) {

        int totalAddresses = addressDao.size(addressSearchRequest);
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
