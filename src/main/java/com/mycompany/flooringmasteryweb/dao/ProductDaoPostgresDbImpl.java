/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.utilities.TextUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author apprentice
 */
public class ProductDaoPostgresDbImpl implements ProductDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_PRODUCT = "INSERT INTO products ( product_name, labor_cost, material_cost ) VALUES ( ?, ?, ? ) RETURNING id;";
    //private static final String SQL_UPDATE_PRODUCT = "UPDATE products SET product_name=?, labor_cost=?, material_cost=? WHERE product_name=?";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE products SET labor_cost = ?, material_cost = ? WHERE product_name = ?;";
    private static final String SQL_DELETE_PRODUCT = "DELETE FROM products WHERE product_name = ?;";
    private static final String SQL_GET_PRODUCT = "SELECT * FROM products WHERE product_name = ?;";
    private static final String SQL_GET_PRODUCT_ID = "SELECT * FROM products WHERE product_name = ?;";
    private static final String SQL_GET_PRODUCT_LIST = "SELECT * FROM products;";

    private static final String SQL_CREATE_PRODUCTS = "CREATE TABLE IF NOT EXISTS products(id serial PRIMARY KEY, product_name varchar(145) NOT NULL UNIQUE CHECK(product_name <> ''), labor_cost decimal(8,4) NOT NULL CHECK(labor_cost >= 0), material_cost decimal(8,4));";

    @Inject
    public ProductDaoPostgresDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        jdbcTemplate.execute(SQL_CREATE_PRODUCTS);
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

//    @Override
//    public Product create(String productName, Product product) {
//
//        if (product == null) {
//            return null;
//        } else if (product.getType() == null) {
//            return null;
//        } else if (productName.equals(product.getType())) {
//
//            String titleCaseName = com.mycompany.flooringmasteryweb.utilities.TextUtilities.toTitleCase(productName);
//
//            if (!productsMap.containsKey(titleCaseName)) {
//                productsMap.put(titleCaseName, product);
//                product.setType(titleCaseName);
//                fileIo.encode(productDataFile, getList());
//
//                return product;
//            } else {
//                return null;
//            }
//        } else {
//            return null;  // Look up how to throw exceptions and consider that instead.
//        }
//    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Product create(String productName, Product product) {

        if (product == null) {
            return null;
        } else if (product.getType() == null) {
            return null;
        } else if (productName.equals(product.getType())) {

            String titleCaseName = com.mycompany.flooringmasteryweb.utilities.TextUtilities.toTitleCase(productName);

            product.setProductName(titleCaseName);

            try {
                Integer id = jdbcTemplate.queryForObject(SQL_INSERT_PRODUCT,
                        Integer.class,
                        product.getProductName(),
                        product.getLaborCost(),
                        product.getCost());

                product.setId(id);
                return product;

            } catch (org.springframework.dao.DuplicateKeyException ex) {
                return null;
            }

//            if (!productsMap.containsKey(titleCaseName)) {
//                productsMap.put(titleCaseName, product);
//                product.setType(titleCaseName);
//                fileIo.encode(productDataFile, getList());
//
//                return product;
//            } else {
//                return null;
//            }
        } else {
            return null;  // Look up how to throw exceptions and consider that instead.
        }
    }

    @Override
    public Product get(String name) {

        String input = null;

        if (name == null) {
            return null;
        }

        try {
            return jdbcTemplate.queryForObject(SQL_GET_PRODUCT, new ProductMapper(), TextUtilities.toTitleCase(name));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }

//        for (String productTest : productsMap.keySet()) {
//            if (name.equalsIgnoreCase(productTest)) {
//                input = productTest;
//                break;
//            }
//        }
//        return productsMap.get(input);
    }

    @Override
    public void update(Product product) {

        if (product == null) {
            return;
        }

        convertProductNameToTitleCase(product);

        if (get(product.getProductName()) == null) {
            create(product);
        } else {

            //if (product.getId() > 0) {
            jdbcTemplate.update(SQL_UPDATE_PRODUCT,
                    product.getLaborCost(),
                    product.getCost(),
                    product.getProductName());
            //}
        }
    }

    private void convertProductNameToTitleCase(Product product) {
        String titleCaseName = com.mycompany.flooringmasteryweb.utilities.TextUtilities.toTitleCase(product.getProductName());
        product.setProductName(titleCaseName);
    }

    @Override
    public void delete(Product product) {

        if (product == null) {
            return;
        }

        convertProductNameToTitleCase(product);

        //int id = product.getId();
        //int id = product.getId();
        String name = product.getProductName();
        try {
            jdbcTemplate.update(SQL_DELETE_PRODUCT, name);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {

        }

//        if (productsMap.containsKey(product.getType())) {
//            productsMap.remove(product.getType());
//            fileIo.encode(productDataFile, getList());
//
//        } else {
//            System.out.println("Throwing a Product Not Found exception!!!!");
//            // Look up exception throwing and consider putting one here, too!
//
//        }
    }

    private static final String SQL_GET_PRODUCT_NAMES = "SELECT product_name FROM products";

    @Override
    public List<String> getList() {
        return jdbcTemplate.query(SQL_GET_PRODUCT_NAMES, new ProductNameMapper());

        // return new ArrayList(productsMap.keySet());
    }

    private static final String SQL_GET_PRODUCT_NAMES_SIZE = "SELECT COUNT(product_name) FROM products";

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_GET_PRODUCT_NAMES_SIZE, Integer.class);
    }

    private final class ProductMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int i) throws SQLException {

            //product_name, product_name, tax_rate
            Product product = new Product();

            product.setLaborCost(rs.getDouble("labor_cost"));
            product.setProductName(rs.getString("product_name"));
            product.setCost(rs.getDouble("material_cost"));
            //product.setReleaseDate(rs.getDate("product_name"));

//            try {
//                String costStr = rs.getString("material_cost");
//
//                double cost = Double.parseDouble(costStr);
//                product..setProductTax(cost);
//
//            } catch (NullPointerException | NumberFormatException ex) {
//                product.setProductTax(0.0d);
//            }
//            product.setDirectorsName(rs.getString("directors_name"));
//            product.setStudio(rs.getString("studio"));
//
//            List<Note> notes = noteDao.getNotesForProduct(product);
            //product.setNotes(notes);
            return product;
        }

    }

    private final class ProductNameMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int i) throws SQLException {

            //product_name, product_name, tax_rate
            String productName = rs.getString("product_name");

//            product.setId(rs.getInt("id"));
//            product.setProductName(rs.getString("product_name"));
//            //product.setReleaseDate(rs.getDate("product_name"));
//
//            try {
//                String taxString = rs.getString("tax_rate");
//
//                double tax = Double.parseDouble(taxString);
//                product.setProductTax(tax);
//
//            } catch (NullPointerException | NumberFormatException ex) {
//                product.setProductTax(0.0d);
//            }
//            product.setDirectorsName(rs.getString("directors_name"));
//            product.setStudio(rs.getString("studio"));
//
//            List<Note> notes = noteDao.getNotesForProduct(product);
            //product.setNotes(notes);
            return productName;
        }

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
