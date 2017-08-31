/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
import com.mycompany.flooringmasteryweb.dto.ResultProperties;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ATeg
 */
public interface AddressDao extends sizableDao {
    
    public Address create(Address address);
    public void update(Address address);
    public Address get(Integer id);
    public Address get(String input);
    public Address getByCompany(String company);
    public Address delete(Integer id);

    public int size(AddressSearchRequest addressSearchRequest);
    
    public List<Address> getAddressesSortedByParameter(ResultProperties resultProperties);
    public Set<String> getCompletionGuesses(String input, int limit);    
    public List<Address> list(ResultProperties resultProperties);
    public List<Address> searchByFirstName(String firstName, ResultProperties resultProperties);
    public List<Address> searchByLastName(String lastName, ResultProperties resultProperties);
    public List<Address> searchByFullName(String fullName, ResultProperties resultProperties);
    public List<Address> searchByCity(String city, ResultProperties resultProperties);
    public List<Address> searchByCompany(String company, ResultProperties resultProperties);
    public List<Address> searchByState(String state, ResultProperties resultProperties);
    public List<Address> searchByZip(String zip, ResultProperties resultProperties);
    public List<Address> searchByStreetAddress(String streetAddress, ResultProperties resultProperties);
    public List<Address> searchByStreet(String street, ResultProperties resultProperties);
    public List<Address> searchByStreetName(String streetName, ResultProperties resultProperties);
    public List<Address> searchByStreetNumber(String streetNumber, ResultProperties resultProperties);
    public List<Address> searchByName(String name, ResultProperties resultProperties);
    public List<Address> searchByNameOrCompany(String input, ResultProperties resultProperties);
    public List<Address> searchByAll(String input, ResultProperties resultProperties);
    public List<Address> search(AddressSearchRequest addressSearchRequest, ResultProperties resultProperties);
}