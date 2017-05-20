/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Date;

/**
 *
 * @author ATeg
 */
interface BasicOrder {
    
    /**
     * @return the area
     */
    double getArea();

    /**
     * @return the date
     */
    Date getDate();

    /**
     * @return the id
     */
    int getId();

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the product
     */
    String getProduct();

    /**
     * @return the state
     */
    String getState();

    /**
     * @param area the area to set
     */
    void setArea(double area);

    /**
     * @param date the date to set
     */
    void setDate(Date date);

    /**
     * @param id the id to set
     */
    void setId(int id);

    /**
     * @param name the name to set
     */
    void setName(String name);

    /**
     * @param product the product to set
     */
    void setProduct(String product);

    /**
     * @param state the state to set
     */
    void setState(String state);    
}
