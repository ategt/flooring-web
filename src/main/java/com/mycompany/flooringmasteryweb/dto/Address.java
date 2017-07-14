/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author ATeg
 */
public class Address {

    @Min(0)
    private Integer id;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 45)
    private String firstName;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 45)
    private String lastName;

    @Size(min = 1, max = 45)
    private String company;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 45)
    private String streetName;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 45)
    private String streetNumber;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 45)
    private String city;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 45)
    private String state;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 45)
    private String zip;

    @Override
    public boolean equals(Object object){
        if (object == null){
            return false;
        }
        
        if (object instanceof Address){
            Address otherAddress = (Address)object;
            
            return Objects.equals(getCity(), otherAddress.getCity()) &&
                    Objects.equals(getCompany(), otherAddress.getCompany()) &&
                    Objects.equals(getFirstName(), otherAddress.getFirstName()) &&
                    Objects.equals(getFullName(), otherAddress.getFullName()) &&
                    Objects.equals(getId(), otherAddress.getId()) &&
                    Objects.equals(getLastName(), otherAddress.getLastName()) &&
                    Objects.equals(getState(), otherAddress.getState()) &&
                    Objects.equals(getStreetName(), otherAddress.getStreetName()) &&
                    Objects.equals(getStreetNumber(), otherAddress.getStreetNumber()) &&
                    Objects.equals(getZip(), otherAddress.getZip());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        if (id == null) return 0;
        return id;
    }
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }
    
    /**
     * @return the streetName
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * @param streetName the streetName to set
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
     * @return the streetNumber
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * @param streetNumber the streetNumber to set
     */
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }
}
