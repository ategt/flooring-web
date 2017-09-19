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
    public void update(Address address);
    public Address get(Integer id);
    public Address get(String input);
    public Address getByCompany(String company);
    public Address delete(Integer id);

    public int size(AddressSearchRequest addressSearchRequest);
    
    public List<Address> getAddressesSortedByParameter(AddressResultSegment addressResultSegment);
    public Set<String> getCompletionGuesses(String input, int limit);    
    public List<Address> list(AddressResultSegment addressResultSegment);
    public List<Address> searchByFirstName(String firstName, AddressResultSegment addressResultSegment);
    public List<Address> searchByLastName(String lastName, AddressResultSegment addressResultSegment);
    public List<Address> searchByFullName(String fullName, AddressResultSegment addressResultSegment);
    public List<Address> searchByCity(String city, AddressResultSegment addressResultSegment);
    public List<Address> searchByCompany(String company, AddressResultSegment addressResultSegment);
    public List<Address> searchByState(String state, AddressResultSegment addressResultSegment);
    public List<Address> searchByZip(String zip, AddressResultSegment addressResultSegment);
    public List<Address> searchByStreetAddress(String streetAddress, AddressResultSegment addressResultSegment);
    public List<Address> searchByStreet(String street, AddressResultSegment addressResultSegment);
    public List<Address> searchByStreetName(String streetName, AddressResultSegment addressResultSegment);
    public List<Address> searchByStreetNumber(String streetNumber, AddressResultSegment addressResultSegment);
    public List<Address> searchByName(String name, AddressResultSegment addressResultSegment);
    public List<Address> searchByNameOrCompany(String input, AddressResultSegment addressResultSegment);
    public List<Address> searchByAll(String input, AddressResultSegment addressResultSegment);
    public List<Address> search(AddressSearchRequest addressSearchRequest, AddressResultSegment addressResultSegment);
}