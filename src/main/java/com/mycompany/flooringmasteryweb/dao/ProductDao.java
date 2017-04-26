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

    ProductCommand buildCommandProduct(Product product);

    List<ProductCommand> buildCommandProductList(List<Product> products);

    Product create(Product product);

    Product create(Product product, String productName);

    Product create(String productName, Product product);

    void delete(Product product);

    Product get(String name);

    List<String> getList();

    List<Product> getListOfProducts();

    List<String> guessProductName(String inputName);

    Product resolveCommandProduct(ProductCommand productCommand);

    int size();

    List<ProductCommand> sortByProductCommandName(List<ProductCommand> products);

    List<ProductCommand> sortByProductCommandNameRev(List<ProductCommand> products);

    List<Product> sortByProductCost(List<Product> products);

    List<Product> sortByProductCostRev(List<Product> products);

    List<Product> sortByProductName(List<Product> products);

    List<Product> sortByProductNameRev(List<Product> products);

    void update(Product product);

    boolean validProductName(String inputName);
    
}
