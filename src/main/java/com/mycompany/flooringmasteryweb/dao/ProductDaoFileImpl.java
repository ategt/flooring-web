/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.ProductFileIO;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author apprentice
 */
public class ProductDaoFileImpl implements ProductDao {

    java.util.Map<String, Product> productsMap;
    int nextId;
    File productDataFile;
    private ProductFileIO fileIo;

    public ProductDaoFileImpl(ConfigDao configDao) {

        this.fileIo = new com.mycompany.flooringmasteryweb.utilities.ProductFileIOImpl(this);

        productDataFile = configDao.get().getProductFile();

        productsMap = fileIo.decode(productDataFile);

        if (productsMap == null) {
            productsMap = new java.util.HashMap();
            System.out.println("The list was empty, making a new one.");
        }

    }

    @Override
    public Product create(Product product) {
        if (product != null) {
            return create(product.getType(), product);
        } else {
            return null;
        }
    }

    @Override
    public Product create(Product product, String productName) {
        return create(productName, product);
    }

    @Override
    public Product create(String productName, Product product) {

        if (product == null) {
            return null;
        } else if (product.getType() == null) {
            return null;
        } else if (productName.equals(product.getType())) {

            String titleCaseName = com.mycompany.flooringmasteryweb.utilities.TextUtilities.toTitleCase(productName);

            if (!productsMap.containsKey(titleCaseName)) {
                productsMap.put(titleCaseName, product);
                product.setType(titleCaseName);
                fileIo.encode(productDataFile, getList());

                return product;
            } else {
                return null;
            }
        } else {
            return null;  // Look up how to throw exceptions and consider that instead.
        }
    }

    @Override
    public Product get(String name) {

        String input = null;

        if ( name == null)
            return null;
            
        for (String productTest : productsMap.keySet()) {
            if (name.equalsIgnoreCase(productTest)) {
                input = productTest;
                break;
            }
        }
        return productsMap.get(input);

    }

    @Override
    public void update(Product product) {
        Product foundProduct = productsMap.get(product.getType());

        if (foundProduct != null) {

            if (foundProduct.getType().equals(product.getType())) {
                productsMap.remove(foundProduct.getType());
                productsMap.put(product.getType(), product);

                fileIo.encode(productDataFile, getList());

            } else {

                System.out.println("Throwing a Product Not Found exception!!!!");
                // Look up exception throwing and consider putting one here, too!
            }
        } else {
            create(product);

            //System.out.println("Throwing a Product is null exception!!!!");
            // Look up exception throwing and consider putting one here, too!
        }
    }

    @Override
    public void delete(Product product) {

        if (productsMap.containsKey(product.getType())) {
            productsMap.remove(product.getType());
            fileIo.encode(productDataFile, getList());

        } else {
            System.out.println("Throwing a Product Not Found exception!!!!");
            // Look up exception throwing and consider putting one here, too!

        }
    }

    @Override
    public List<String> getList() {
        return new ArrayList(productsMap.keySet());
    }

    @Override
    public int size() {
        return productsMap.size();
    }

    @Override
    public boolean validProductName(String inputName) {
        return (bestGuessProductName(inputName) != null);
    }

    @Override
    public String bestGuessProductName(String inputName) {
        if (inputName == null) {
            return null;
        }

        List<String> productGuesses = guessProductName(inputName);

        if (productGuesses.isEmpty()) {
            return null;
        }

        return productGuesses.get(0);
    }

    @Override
    public List<String> guessProductName(String inputName) {

        if (inputName == null) {
            return null;
        }

        if (getList() == null) {
            return null;
        }

        List<String> productNames = getList().stream()
                .filter(a -> a != null)
                .filter(a -> a.equalsIgnoreCase(inputName))
                .collect(Collectors.toList());

        if (productNames.isEmpty()) {
            productNames = getList().stream()
                    .filter(a -> a != null)
                    .filter(a -> a.toLowerCase().startsWith(inputName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (productNames.isEmpty()) {
            productNames = getList().stream()
                    .filter(a -> a != null)
                    .filter(a -> a.toLowerCase().contains(inputName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return productNames;
    }

    @Override
    public List<Product> getListOfProducts() {

        List<Product> products = getList().stream()
                .map(name -> get(name))
                .collect(Collectors.toList());

        return products;
    }

    @Override
    public List<Product> sortByProductName(List<Product> products) {

        products.sort(
                new Comparator<Product>() {
            public int compare(Product c1, Product c2) {
                return c1.getProductName().compareTo(c2.getProductName());
            }
        });

        return products;
    }

    @Override
    public List<Product> sortByProductNameRev(List<Product> products) {
        List<Product> shallowCopy = sortByProductName(products).subList(0, products.size());
        Collections.reverse(shallowCopy);
        return shallowCopy;
    }

    @Override
    public List<Product> sortByProductCost(List<Product> products) {

        products.sort(
                new Comparator<Product>() {
            public int compare(Product c1, Product c2) {
                return Double.compare(c1.getCost(), c2.getCost());
            }
        });

        return products;
    }

    @Override
    public List<Product> sortByProductCostRev(List<Product> products) {
        List<Product> shallowCopy = sortByProductName(products).subList(0, products.size());
        Collections.reverse(shallowCopy);
        return shallowCopy;
    }

    @Override
    public ProductCommand buildCommandProduct(Product product) {

        ProductCommand productCommand = new ProductCommand();

        String productName = product.getProductName();
        productCommand.setProductName(productName);

        Double productCost = product.getCost();
        productCommand.setCost(productCost);

        Double laborCost = product.getLaborCost();
        productCommand.setLaborCost(laborCost);

        return productCommand;
    }

    @Override
    public Product resolveCommandProduct(ProductCommand productCommand) {

        Product product = new Product();

        String productName = productCommand.getProductName();
        product.setProductName(productName);

        Double productCost = productCommand.getCost();
        product.setCost(productCost);

        Double laborCost = productCommand.getLaborCost();
        product.setLaborCost(laborCost);

        return product;
    }

    @Override
    public List<ProductCommand> buildCommandProductList(List<Product> products) {
        List<ProductCommand> resultsList = new ArrayList();

        for (Product product : products) {

            resultsList.add(buildCommandProduct(product));

        }

        return resultsList;
    }

    @Override
    public List<ProductCommand> sortByProductCommandName(List<ProductCommand> products) {

        products.sort(
                new Comparator<ProductCommand>() {
            public int compare(ProductCommand c1, ProductCommand c2) {
                return c1.getProductName().compareTo(c2.getProductName());
            }
        });

        return products;
    }

    @Override
    public List<ProductCommand> sortByProductCommandNameRev(List<ProductCommand> products) {
        List<ProductCommand> shallowCopy = sortByProductCommandName(products).subList(0, products.size());
        Collections.reverse(shallowCopy);
        return shallowCopy;
    }

}
