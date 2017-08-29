/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Objects;

/**
 *
 * @author apprentice
 */
public class Product {

    private String type;
    private double cost;
    private double laborCost;
    private Integer id;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (object instanceof Product) {
            Product product = (Product) object;

            return Objects.equals(getCost(), product.getCost())
                    && Objects.equals(getId(), product.getId())
                    && Objects.equals(getLaborCost(), product.getLaborCost())
                    && Objects.equals(getProductName(), product.getProductName())
                    && Objects.equals(getType(), product.getType());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getProductName() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setProductName(String type) {
        this.type = type;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
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

    public static Product buildProduct(ProductCommand productCommand) {

        Product product = new Product();

        String productName = productCommand.getProductName();
        product.setProductName(productName);

        Double productCost = productCommand.getCost();
        product.setCost(productCost);

        Double laborCost = productCommand.getLaborCost();
        product.setLaborCost(laborCost);

        return product;
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
}
