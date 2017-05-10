/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.AddressDao;
import com.mycompany.flooringmasteryweb.dao.AddressDaoPostgresImpl;
import com.mycompany.flooringmasteryweb.dto.Address;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
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

    private AddressDao addressDao;

    @Inject
    public AddressController(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@RequestParam(name = "sort_by", required = false) String sortBy, Map model) {
        List<Address> addresses = null;

        if (sortBy != null) {
            if (sortBy.equalsIgnoreCase("company")) {
                addresses = addressDao.list(AddressDao.SORT_BY_COMPANY);
            } else if (sortBy.equalsIgnoreCase("id")) {
                addresses = addressDao.list(AddressDao.SORT_BY_ID);
            } else if (sortBy.equalsIgnoreCase("first_name")) {
                addresses = addressDao.list(AddressDao.SORT_BY_FIRST_NAME);
            } else if (sortBy.equalsIgnoreCase("last_name")) {
                addresses = addressDao.list(AddressDao.SORT_BY_LAST_NAME);
            } else {
                addresses = addressDao.list();
            }
        } else {
            addresses = addressDao.list();
        }

        model.put("addresses", addresses);
        return "address\\index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Address createWithAjax(@Valid @RequestBody Address address) {
        return addressDao.create(address);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Address showWithAjax(@PathVariable("id") String addressInput) {
        Address contact = null;

        try {
            int addressId = Integer.parseInt(addressInput);
            contact = addressDao.get(addressId);
        } catch (NumberFormatException ex) {
            contact = addressDao.get(addressInput);
        }

        return contact;
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public Address editSubmitWithAjax(@Valid @RequestBody Address address) {
        addressDao.update(address);
        return address;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteWithAjax(@PathVariable("id") Integer addressId) {
        addressDao.delete(addressId);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer contactId, Map model) {
        Address address = addressDao.get(contactId);
        model.put("address", address);
        return "address\\edit";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @RequestParam("searchBy") String searchBy,
            @RequestParam("searchText") String searchText,
            Map model) {

        List<Address> addresses = null;

        if ("searchByLastName".equalsIgnoreCase(searchBy)) {
            addresses = addressDao.searchByLastName(searchText);

        } else if ("searchByFirstName".equalsIgnoreCase(searchBy)) {
            addresses = addressDao.searchByFirstName(searchText);

        } else if ("searchByCity".equalsIgnoreCase(searchBy)) {
            addresses = addressDao.searchByCity(searchText);

        } else if ("searchByState".equalsIgnoreCase(searchBy)) {
            addresses = addressDao.searchByState(searchText);

        } else if ("searchByZip".equalsIgnoreCase(searchBy)) {
            addresses = addressDao.searchByZip(searchText);

        } else {
            addresses = addressDao.list();
        }

        model.put("addresses", addresses);

        return "address\\search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String blankSearch(Map model) {

        List<Address> addresses = addressDao.list();

        model.put("addresses", addresses);

        return "address\\search";
    }

    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Integer addressId, Map model) {

        Address address = addressDao.get(addressId);
        List<Address> addresses = addressDao.list();

        model.put("address", address);
        model.put("addresses", addresses);

        return "address\\show";
    }
}
