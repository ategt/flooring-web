/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author apprentice
 */
public class OrderCommand{

    @Min(0)
    private int id;

    @NotNull(message = "You Must Include A Name For This Order")
    @Size(min = 2, max = 45, message = "The Name For This Order Must Be Between 2 and 45 Characters")
    private String name;

    @NotNull(message = "You Must Include A State For This Order")
    @Size(min = 2, max = 45, message = "The State For This Order Must Be Between 2 and 45 Characters")
    private String state;

    @NotNull(message = "You Must Include A Product For This Order")
    @Size(min = 2, max = 45, message = "The Product For This Order Must Be Between 2 and 45 Characters")
    private String product;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy",timezone="EST")
    @NotNull(message="You Must Include A Date For This Order")
    private Date date;
    
    @Min(0)
    private double area;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the area
     */
    public double getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(double area) {
        this.area = area;
    }
}