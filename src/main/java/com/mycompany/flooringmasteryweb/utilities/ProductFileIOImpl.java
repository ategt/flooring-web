/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dto.Product;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apprentice
 */
public class ProductFileIOImpl implements ProductFileIO, GenericMapFileIO<Product> {
    
    private ProductDao productDao;
    
    public ProductFileIOImpl( ProductDao productDao ) {
        this.productDao = productDao;
    }
    
    @Override
    public void encode(File productDataFile, List<String> encodingList) {

        final String TOKEN = ",";
        final String DATAHEADER = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot";
        try {

            try (PrintWriter out = new PrintWriter(new FileWriter(productDataFile))) {
                out.println(DATAHEADER);

                for (String productType : encodingList) {

                    Product product = productDao.get(productType);

                    out.print(product.getType());
                    out.print(TOKEN);
                    out.print(product.getCost());
                    out.print(TOKEN);
                    out.print(product.getLaborCost());
                    out.println("");
                }

                out.flush();
            }

        } catch (IOException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public java.util.Map<String, Product> decode(java.io.File productDataFile) {

        java.util.Map<String, Product> productMap = new java.util.HashMap();

        final String TOKEN = ",";

        final String DATAHEADER = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot";

        try {

            if (!productDataFile.exists()) {
                productDataFile.createNewFile();
            }

            try (Scanner sc = new Scanner(new BufferedReader(new FileReader(productDataFile)))) {
                while (sc.hasNextLine()) {
                    String currentLine = sc.nextLine();
                    if (currentLine.equalsIgnoreCase(DATAHEADER)) {

                    } else if (!currentLine.trim().isEmpty()) {
                        String[] stringParts = currentLine.split(TOKEN);

                        Product product = new Product();

                        String content = stringParts[0];

                        product.setType(content);

                        String costPerSquareFootString = stringParts[1];

                        try {
                            double costPerSquareFoot = Double.parseDouble(costPerSquareFootString);
                            product.setCost(costPerSquareFoot);
                        } catch (NumberFormatException numFmtEx) {

                        }

                        String laborCostPerSquareFootString = stringParts[2];

                        try {

                            double laborCostPerSquareFoot = Double.parseDouble(laborCostPerSquareFootString);

                            product.setLaborCost(laborCostPerSquareFoot);

                        } catch (NumberFormatException numFmtEx) {

                        }

                        productMap.put(product.getType(), product);
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProductDao.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProductDao.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return productMap;
    }

}
