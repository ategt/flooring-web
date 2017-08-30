/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Arrays;
import java.util.Optional;

/**
 **
 * @author ATeg
 */
public enum OrderSearchByOptionEnum {
    DATE,
    NAME,
    PRODUCT,
    STATE,
    ORDER_NUMBER,
    EVERYTHING;

    public static OrderSearchByOptionEnum parse(String input) {
        
        Optional<OrderSearchByOptionEnum> result = Arrays.stream(OrderSearchByOptionEnum.values())
                .filter(option -> option.toString().equalsIgnoreCase(input))
                .findAny();
        
        if (!result.isPresent()) {
            result = Arrays.stream(values()).filter(option -> input.toLowerCase().contains(option.toString().toLowerCase())).findAny();
        }

        if (!result.isPresent()) {
            result = Arrays.stream(values()).filter(option -> Integer.compare(option.ordinal(), Integer.parseInt(input)) == 0).findAny();
        }

        if (!result.isPresent()) {
            if ("number".equalsIgnoreCase(input) || input.toLowerCase().contains("number")) {
                result = Optional.of(ORDER_NUMBER);
            }            
        }
        
        return result.orElse(EVERYTHING);
    }
}
