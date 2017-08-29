/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dto.Product;
import java.util.Objects;

/**
 *
 * @author ATeg
 */
public class ProductUtilities {

    public static Product titleCaseProductName(Product product) {
        if (Objects.isNull(product)) {
            return null;
        }
        
        String productName = product.getProductName();
        productName = TextUtilities.toTitleCase(productName);
        product.setProductName(productName);
        return product;
    }
}
