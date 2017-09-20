/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mycompany.flooringmasteryweb.modelBinding.CustomDateDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author apprentice
 */
public class OrderCommand {

    @Min(0)
    private Integer id;

    @NotNull(message = "{validation.orderCommand.name.null}")
    @Size(min = 2, max = 45, message = "{validation.orderCommand.name.size}")
    private String name;

    @NotNull(message = "{validation.orderCommand.state.null}")
    @Size(min = 2, max = 45, message = "{validation.orderCommand.state.size}")
    private String state;

    @NotNull(message = "{validation.orderCommand.product.null}")
    @Size(min = 2, max = 45, message = "{validation.orderCommand.product.size}")
    private String product;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone = "UTC")
    @NotNull(message = "{validation.orderCommand.date.null}")
    private Date date;

    @Min(0)
    private double area;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (object instanceof OrderCommand) {
            OrderCommand otherOrderCommand = (OrderCommand) object;

            return Objects.equals(getArea(), otherOrderCommand.getArea())
                    && Objects.equals(getDate(), otherOrderCommand.getDate())
                    && Objects.equals(getId(), otherOrderCommand.getId())
                    && Objects.equals(getName(), otherOrderCommand.getName())
                    && Objects.equals(getState(), otherOrderCommand.getState())
                    && Objects.equals(getProduct(), otherOrderCommand.getProduct());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.isNull(id) ? 0 : id;
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
    @JsonDeserialize(using = CustomDateDeserializer.class)
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

    public static OrderCommand build(Order order) {

        if (order == null) {
            return null;
        }

        OrderCommand orderCommand = new OrderCommand();

        Double area = order.getArea();
        State state = order.getState();
        Date date = order.getDate();
        Integer id = order.getId();
        String name = order.getName();
        Product product = order.getProduct();

        String productName = "";
        if (product != null) {
            productName = product.getProductName();
        }

        String stateName = "";
        if (state != null) {
            stateName = state.getStateName();
        }

        orderCommand.setState(stateName);
        orderCommand.setArea(area);
        orderCommand.setDate(date);
        orderCommand.setId(id);
        orderCommand.setName(name);
        orderCommand.setProduct(productName);

        return orderCommand;
    }
}
