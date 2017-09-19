/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;
import com.mycompany.flooringmasteryweb.utilities.ControllerUtilities;

import java.util.*;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class AddressController  implements ApplicationContextAware {

    private final AddressDao addressDao;
    private ApplicationContext applicationContext;

    private final String RESULTS_COOKIE_NAME = "results_cookie";
    private final String SORT_COOKIE_NAME = "sort_cookie";

    @Inject
    public AddressController(AddressDao addressDao) {
        this.addressDao = addressDao;
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

        AddressResultSegment addressResultSegment = processResultPropertiesWithContextDefaults(resultsPerPage, resultsPerPageCookie, page, sortBy, response, sortCookie);

        List<Address> addresses = addressDao.getAddressesSortedByParameter(addressResultSegment);

        ControllerUtilities.generatePagingLinks(applicationContext, addressDao.size(), addressResultSegment, request, uriComponentsBuilder, model);

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

        AddressResultSegment addressResultSegment = processResultPropertiesWithAllAsDefault(
                sortBy,
                response,
                sortCookie,
                page,
                resultsPerPage,
                resultsPerPageCookie);

        List<Address> addresses = addressDao.getAddressesSortedByParameter(addressResultSegment);

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

    @RequestMapping(value = "/edit/{showOnFail}", method = RequestMethod.POST)
    public String update(@PathVariable("showOnFail") Integer contactNumber, @ModelAttribute Address address, Map model, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            bindingResult.getErrorCount();
            List<ObjectError> errors = bindingResult.getAllErrors();

            String errorString = "";
            for (ObjectError error : errors) {
                errorString += error.getDefaultMessage() + "<br />";
            }

            model.put("errors", errorString);

            loadAddress(contactNumber, model);
            return "address\\edit";
        } else {
            addressDao.update(address);
            return "redirect:/address/" + address.getId();
        }
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
        AddressResultSegment addressResultSegment = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);

        List<Address> addresses = searchDatabase(addressSearchRequest, addressResultSegment);

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
        AddressResultSegment addressResultSegment = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);

        List<Address> addresses = searchDatabase(addressSearchRequest, addressResultSegment);

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

        AddressResultSegment addressResultSegment = processResultPropertiesWithContextDefaults(resultsPerPage, resultsPerPageCookie, page, sortBy, response, sortCookie);

        List<Address> addresses = searchDatabase(addressSearchRequest, addressResultSegment);

        ControllerUtilities.generatePagingLinks(applicationContext, addressDao.size(addressSearchRequest), addressResultSegment, request, uriComponentsBuilder, model, addressSearchRequest);

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

        AddressResultSegment addressResultSegment = processResultPropertiesWithContextDefaults(resultsPerPage, resultsPerPageCookie, page, sortBy, response, sortCookie);

        List<Address> addresses;

        if (addressSearchRequest == null) {
            addresses = addressDao.list(addressResultSegment);
        } else {
            addresses = searchDatabase(addressSearchRequest, addressResultSegment);
        }

        ControllerUtilities.generatePagingLinks(applicationContext, addressDao.size(addressSearchRequest), addressResultSegment, request, uriComponentsBuilder, model, addressSearchRequest);

        model.put("addresses", addresses);

        return "address\\search";
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

        AddressResultSegment addressResultSegment = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        List<Address> addresses = addressDao.list(addressResultSegment);

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

    private AddressResultSegment processResultPropertiesWithContextDefaults(
            Integer resultsPerPage,
            Integer resultsPerPageCookie,
            Integer page,
            String sortBy,
            HttpServletResponse response,
            String sortCookie
    ) throws BeansException {

        resultsPerPage = ControllerUtilities.loadDefaultResults(applicationContext, resultsPerPage, null);
        page = ControllerUtilities.loadDefaultPageNumber(applicationContext, page);
        AddressResultSegment addressResultSegment = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return addressResultSegment;
    }

    private AddressResultSegment processResultPropertiesWithAllAsDefault(
            String sortBy,
            HttpServletResponse response,
            String sortCookie,
            Integer page,
            Integer resultsPerPage,
            Integer resultsPerPageCookie) {

        resultsPerPage = ControllerUtilities.loadDefaultResults(applicationContext, resultsPerPage, null);
        page = ControllerUtilities.loadDefaultPageNumber(applicationContext, page);
        AddressResultSegment addressResultSegment = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return addressResultSegment;
    }

    private AddressResultSegment processResultProperties(String sortBy, HttpServletResponse response, String sortCookie, Integer page, Integer resultsPerPage) {
        AddressSortByEnum sortEnum = updateSortEnum(sortBy, response, sortCookie);

        AddressResultSegment addressResultSegment = new AddressResultSegment(sortEnum, page, resultsPerPage);

        updateResultsCookie(addressResultSegment.getResultsPerPage(), response);
        return addressResultSegment;
    }

    private AddressSortByEnum updateSortEnum(String sortBy, HttpServletResponse response, String sortCookie) {
        sortBy = checkForReverseRequest(sortBy, sortCookie);

        if (sortBy != null) {
            response.addCookie(new Cookie(SORT_COOKIE_NAME, sortBy));
        } else if (sortCookie != null) {
            sortBy = sortCookie;
        }

        return AddressSortByEnum.parse(sortBy);
    }

    private static String checkForReverseRequest(String sortBy, String sortCookie) {
        if (Objects.nonNull(sortBy)) {
            AddressSortByEnum sortOld = AddressSortByEnum.parse(sortCookie);
            AddressSortByEnum sortNew = AddressSortByEnum.parse(sortBy);

            if (sortOld.equals(sortNew) || sortOld.reverse().equals(sortNew)) {
                sortBy = sortOld.reverse().toString();
            }
        }
        return sortBy;
    }

    private Integer updateResultsCookie(Integer resultsPerPage, HttpServletResponse response) {
        if (resultsPerPage != null) {
            response.addCookie(new Cookie(RESULTS_COOKIE_NAME, Integer.toString(resultsPerPage)));
        }
        return resultsPerPage;
    }

    private void loadAddress(Integer contactId, Map model) {
        Address address = addressDao.get(contactId);
        model.put("address", address);
    }

    private List<Address> searchDatabase(AddressSearchRequest searchRequest, AddressResultSegment addressResultSegment) {
        return addressDao.search(searchRequest,
                addressResultSegment);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}