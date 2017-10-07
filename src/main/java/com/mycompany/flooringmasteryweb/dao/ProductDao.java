/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;

import java.util.List;

/**
 *
 * @author apprentice
 */
public interface ProductDao {

    String bestGuessProductName(String inputName);

    List<ProductCommand> buildCommandProductList();

    Product create(Product product);
    Product delete(Product product);
    Product get(String name);
    Product update(Product product);

    List<String> getList();

    List<Product> getListOfProducts();

    List<String> guessProductName(String inputName);

    int size();

    boolean validProductName(String inputName);
}
