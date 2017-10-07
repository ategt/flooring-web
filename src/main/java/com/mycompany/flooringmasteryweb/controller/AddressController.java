/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.modelBinding.CustomModelBinder;
import com.mycompany.flooringmasteryweb.utilities.ControllerUtilities;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ATeg
 */
@Controller
@RequestMapping(value = "/address")
public class AddressController implements ApplicationContextAware {

    private final AddressDao addressDao;
    private ApplicationContext applicationContext;

    @Inject
    public AddressController(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(
            @CustomModelBinder AddressResultSegment addressResultSegment,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request,
            Map model) {

        List<Address> addresses = addressDao.list(addressResultSegment);

        ControllerUtilities.generatePagingLinks(applicationContext, addressDao.size(), addressResultSegment, request, uriComponentsBuilder, model);

        model.put("addresses", addresses);
        return "address\\index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Address[] index(
            @CustomModelBinder AddressResultSegment addressResultSegment,
            HttpServletResponse response) {

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
    public List<String> listNamesWithAjax(
            @PathVariable("input") String addressInput,
            @RequestParam(name = "limit", required = false) Integer addressLimit) {

        if (addressLimit == null) {
            addressLimit = 30;
        }

        List<String> names = addressDao.getCompletionGuesses(addressInput, addressLimit);

        return names;
    }

    @RequestMapping(value = "/name_completion", method = RequestMethod.GET)
    @ResponseBody
    public List<String> altListNamesWithAjax(
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

        List<String> names = addressDao.getCompletionGuesses(input, addressLimit);

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
    public String update(@PathVariable("showOnFail") Integer contactNumber, @ModelAttribute Address address, Map model, BindingResult bindingResult) {
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
            @CustomModelBinder AddressResultSegment addressResultSegment,
            @Valid @RequestBody AddressSearchRequest addressSearchRequest,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        List<Address> addresses = searchDatabase(addressSearchRequest, addressResultSegment);

        return addresses;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Address> search(
            @CustomModelBinder AddressResultSegment addressResultSegment,
            @ModelAttribute AddressSearchRequest addressSearchRequest,
            HttpServletResponse response
    ) {
        List<Address> addresses = searchDatabase(addressSearchRequest, addressResultSegment);

        return addresses;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @CustomModelBinder AddressResultSegment addressResultSegment,
            @ModelAttribute AddressSearchRequest addressSearchRequest,
            HttpServletResponse response,
            HttpServletRequest request,
            UriComponentsBuilder uriComponentsBuilder,
            Map model
    ) {

        List<Address> addresses = searchDatabase(addressSearchRequest, addressResultSegment);

        ControllerUtilities.generatePagingLinks(applicationContext, addressDao.size(addressSearchRequest), addressResultSegment, request, uriComponentsBuilder, model, addressSearchRequest);

        model.put("addresses", addresses);

        return "address\\search";

    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(
            @CustomModelBinder AddressResultSegment addressResultSegment,
            @Valid @ModelAttribute AddressSearchRequest addressSearchRequest,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request,
            Map model) {

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
                       @CustomModelBinder AddressResultSegment addressResultSegment,
                       HttpServletResponse response,
                       Map model) {

        Address address = addressDao.get(addressId);

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