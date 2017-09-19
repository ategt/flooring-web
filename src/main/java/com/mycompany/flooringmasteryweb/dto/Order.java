/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author apprentice
 */
public class Order {

    private Integer id;             // OrderNumber
    private String name;        // CustomerName
    private State state;        // State
    private Product product;    // ProductType
    private double materialCost; // MaterialCost
    private double taxRate;
    private double tax;         // Tax
    private double total;       // Total

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone = "UTC")
    private Date date;          // file name
    private double laborCost;   // LaborCost
    private double area;        // Area
    private double costPerSquareFoot;
    private double laborCostPerSquareFoot;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (object instanceof Order) {
            Order otherOrder = (Order) object;

            return Objects.equals(getArea(), otherOrder.getArea())
                    && Objects.equals(getCostPerSquareFoot(), otherOrder.getCostPerSquareFoot())
                    && Objects.equals(getDate(), otherOrder.getDate())
                    && Objects.equals(getId(), otherOrder.getId())
                    && Objects.equals(getLaborCost(), otherOrder.getLaborCost())
                    && Objects.equals(getLaborCostPerSquareFoot(), otherOrder.getLaborCostPerSquareFoot())
                    && Objects.equals(getMaterialCost(), otherOrder.getMaterialCost())
                    && Objects.equals(getName(), otherOrder.getName())
                    && Objects.equals(getState(), otherOrder.getState())
                    && Objects.equals(getTax(), otherOrder.getTax())
                    && Objects.equals(getTaxRate(), otherOrder.getTaxRate())
                    && Objects.equals(getTotal(), otherOrder.getTotal())
                    && Objects.equals(getProduct(), otherOrder.getProduct());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id;
    }

//    -OrderNumber
//    -CustomerName
//    -State
//    -TaxRate
//    -ProductType
//    -Area
//    -CostPerSquareFoot
//    -LaborCostPerSquareFoot
//    -MaterialCost
//    -LaborCost
//    -Tax
//    -Total 
    /*
    OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCost
PerSquareFoot,MaterialCost,LaborCost,Tax,Total
        
     */ /**
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
    public State getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the materialCost
     */
    public double getMaterialCost() {
        return materialCost;
    }

    /**
     * @param materialCost the materialCost to set
     */
    public void setMaterialCost(double materialCost) {
        this.materialCost = materialCost;
    }

    /**
     * @return the tax
     */
    public double getTax() {
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
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
     * @return the taxRate
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxRate the taxRate to set
     */
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return the laborCost
     */
    public double getLaborCost() {
        return laborCost;
    }

    /**
     * @param laborCost the laborCost to set
     */
    public void setLaborCost(double laborCost) {
        this.laborCost = laborCost;
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

    /**
     * @return the costPerSquareFoot
     */
    public double getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    /**
     * @param costPerSquareFoot the costPerSquareFoot to set
     */
    public void setCostPerSquareFoot(double costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    /**
     * @return the laborCostPerSquareFoot
     */
    public double getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    /**
     * @param laborCostPerSquareFoot the laborCostPerSquareFoot to set
     */
    public void setLaborCostPerSquareFoot(double laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

}
