/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author apprentice
 */
public class ProductCommand {

    @NotEmpty(message = "You must enter a product name.")
    private String productName;

    @Min(0)
    private double productCost;

    @Min(0)
    private double laborCost;
    private int id;

    /**
     * @return the productName
     */
    public String getType() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setType(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productCost
     */
    public double getCost() {
        return productCost;
    }

    /**
     * @param productCost the productCost to set
     */
    public void setCost(double productCost) {
        this.productCost = productCost;
    }

    /**
     * @return the productCost
     */
    public double getProductCost() {
        return productCost;
    }

    /**
     * @param productCost the productCost to set
     */
    public void setProductCost(double productCost) {
        this.productCost = productCost;
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

    public static ProductCommand buildProductCommand(Product product) {
        ProductCommand productCommand = new ProductCommand();

        String productName = product.getProductName();
        productCommand.setProductName(productName);

        Double productCost = product.getCost();
        productCommand.setCost(productCost);

        Double laborCost = product.getLaborCost();
        productCommand.setLaborCost(laborCost);
        return productCommand;
    }
}
