/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
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
import org.springframework.http.HttpRequest;
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

/**
 *
 * @author ATeg
 */
@Controller
@RequestMapping(value = "/address")
public class AddressController {

    private final AddressDao addressDao;
    private ApplicationContext ctx;

    @Inject
    public AddressController(AddressDao addressDao) {
        this.addressDao = addressDao;
        ctx = com.mycompany.flooringmasteryweb.aop.ApplicationContextProvider.getApplicationContext();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(
            @CookieValue(value = "sort_cookie", defaultValue = "last_name") String sortCookie,
            @CookieValue(value = "results_per_page_cookie", required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            HttpServletResponse response,
            Map model) {

        AddressResultSegment addressResultSegment = buildAddressResultSegment(resultsPerPage, response, resultsPerPageCookie, sortBy, sortCookie, page);

        int lastPageNumber = addressDao.size() / addressResultSegment.getResultsPerPage();

        int nextPage = addressResultSegment.getPage() + 1;
        int prevPage = addressResultSegment.getPage() - 1;

        boolean showingNext = false, showingPrev = false;

        if (addressResultSegment.getPage() != lastPageNumber) {
            model.put("lastPageNumber", lastPageNumber);
        }

        if (nextPage <= lastPageNumber) {
            model.put("nextPage", nextPage);
            showingNext = true;
        }

        if (prevPage >= 0) {
            model.put("prevPage", prevPage);
            model.put("firstPage", 0);
            showingPrev = true;
        }

        if (showingNext && showingPrev) {
            model.put("currentPage", addressResultSegment.getPage() + 1);
        }

        List<Address> addresses = addressDao.list(addressResultSegment);

        model.put("addresses", addresses);
        return "address\\index";
    }

    private AddressResultSegment buildAddressResultSegment(Integer resultsPerPage, HttpServletResponse response, Integer resultsPerPageCookie, String sortBy, String sortCookie, Integer page) throws BeansException {
        if (resultsPerPage != null) {
            response.addCookie(new Cookie("results_per_page_cookie", resultsPerPage.toString()));
        } else if (resultsPerPageCookie != null) {
            resultsPerPage = resultsPerPageCookie;
        } else {
            resultsPerPage = ctx.getBean("defaultItemsPerPage", Integer.class);
        }
        if (sortBy != null) {
            response.addCookie(new Cookie("sort_cookie", sortBy));
        } else {
            sortBy = sortCookie;
        }
        AddressResultSegment addressResultSegment = new AddressResultSegment(page, resultsPerPage, AddressSortByEnum.parse(sortBy));
        return addressResultSegment;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Address[] index(
            @CookieValue(value = "sort_cookie", defaultValue = "id") String sortCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @CookieValue(value = "results_per_page_cookie", required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            HttpServletResponse response) {

        AddressResultSegment addressResultSegment = buildAddressResultSegment(resultsPerPage, response, resultsPerPageCookie, sortBy, sortCookie, page);

        List<Address> addresses = addressDao.list(addressResultSegment);

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

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @ModelAttribute AddressSearchRequest addressSearchRequest,
            Map model) {
        List<Address> addresses = searchDatabase(addressSearchRequest);

        model.put("addresses", addresses);

        return "address\\search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = {"Content-type=application/json", "Accept=application/json"})
    @ResponseBody
    public List<Address> search(
            @Valid @RequestBody AddressSearchRequest addressSearchRequest,
            HttpServletRequest request
    ) {
        List<Address> addresses = searchDatabase(addressSearchRequest);

        return addresses;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Address> search(
            @ModelAttribute AddressSearchRequest addressSearchRequest
    ) {
        List<Address> addresses = searchDatabase(addressSearchRequest);

        return addresses;
    }

    private List<Address> searchDatabase(AddressSearchRequest searchRequest) {
        return addressDao.search(searchRequest.getSearchText(),
                AddressSearchByOptionEnum.parse(searchRequest.getSearchBy()));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String blankSearch(Map model) {

        List<Address> addresses = addressDao.list();

        model.put("addresses", addresses);

        return "address\\search";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Integer addressId, Map model) {

        Address address = addressDao.get(addressId);
        List<Address> addresses = addressDao.list();

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
}
