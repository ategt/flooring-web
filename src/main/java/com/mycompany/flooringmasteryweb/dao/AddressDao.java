/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ATeg
 */
public interface AddressDao {
    
    public Address create(Address address);
    public void update(Address address);
    public Address get(Integer id);
    public Address get(String input);
    public Address delete(Integer id);

    public int size();
    
    public List<Address> getAddressesSortedByParameter(String sortBy);
    public Set<String> getCompletionGuesses(String input, int limit);
    public List<Address> list();
    public List<Address> list(AddressSortByEnum sortByEnum);
    public List<Address> searchByFirstName(String firstName);
    public List<Address> searchByLastName(String lastName);
    public List<Address> searchByFullName(String lastName);
    public List<Address> searchByCity(String city);
    public List<Address> searchByCompany(String company);
    public List<Address> searchByState(String state);
    public List<Address> searchByZip(String zip);
    public List<Address> search(String queryString, AddressSearchByOptionEnum searchOption);
    public List<Address> search(AddressSearchRequest searchRequest, AddressResultSegment resultSegment);
}
