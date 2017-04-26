/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Date;

/**
 *
 * @author apprentice
 */
public class BasicOrderImpl implements BasicOrder {

    private int id;             // OrderNumber
    private String name;        // CustomerName
    private String state;        // State
    private String product;    // ProductType
    private Date date;          // file name
    private double area;        // Area
//    private double materialCost; // MaterialCost
//    private double taxRate;
//    private double tax;         // Tax
//    private double total;       // Total
//    private double laborCost;   // LaborCost
//    private double costPerSquareFoot;
 //   private double laborCostPerSquareFoot;

    /**
     * @return the id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the state
     */
    @Override
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    @Override
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the product
     */
    @Override
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    @Override
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the date
     */
    @Override
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the area
     */
    @Override
    public double getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    @Override
    public void setArea(double area) {
        this.area = area;
    }
}
