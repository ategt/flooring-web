/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Objects;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author apprentice
 */
public class ProductCommand {

    @NotEmpty(message = "{validation.productCommand.productName.empty}")
    private String productName;

    @Min(0)
    private double productCost;

    @Min(0)
    private double laborCost;

    private Integer id;


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

    public static ProductCommand buildProductCommand(Product product) {
        if (Objects.isNull(product))
            return null;
        
        ProductCommand productCommand = new ProductCommand();

        String productName = product.getProductName();
        productCommand.setProductName(productName);

        Double productCost = product.getCost();
        productCommand.setCost(productCost);

        Double laborCost = product.getLaborCost();
        productCommand.setLaborCost(laborCost);
        return productCommand;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
