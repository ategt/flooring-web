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
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author apprentice
 */
public class OrderCommand implements BasicOrder {

    @Min(0)
    private int id;
    // OrderNumber
    @NotNull(message = "You Must Include A Name For This Order")
    @Size(min = 2, max = 45, message = "The Name For This Order Must Be Between 2 and 45 Characters")
    private String name;        // CustomerName

    @NotNull(message = "You Must Include A State For This Order")
    @Size(min = 2, max = 45, message = "The State For This Order Must Be Between 2 and 45 Characters")
    private String state;        // State

    @NotNull(message = "You Must Include A Product For This Order")
    @Size(min = 2, max = 45, message = "The Product For This Order Must Be Between 2 and 45 Characters")
    private String product;    // ProductType


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy",timezone="EST")
    @NotNull(message="You Must Include A Date For This Order")
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;          // file name
    
    @Min(0)
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
