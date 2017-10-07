/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ATeg
 */
public interface AddressDao extends SizeableDao {
    
    public Address create(Address address);
    public Address update(Address address);
    public Address get(Integer id);
    public Address get(String input);
    public Address delete(Integer id);

    public int size(AddressSearchRequest addressSearchRequest);
    
    public List<String> getCompletionGuesses(String input, int limit);
    public List<Address> list(AddressResultSegment addressResultSegment);
    public List<Address> search(AddressSearchRequest addressSearchRequest, AddressResultSegment addressResultSegment);
}