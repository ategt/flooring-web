/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
public class ProductDaoPostgresImpl implements ProductDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_PRODUCT = "INSERT INTO products ( product_name, labor_cost, material_cost ) VALUES ( ?, ?, ? ) RETURNING id;";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE products SET labor_cost = ?, material_cost = ? WHERE LOWER(product_name) = LOWER(?) RETURNING *";
    private static final String SQL_DELETE_PRODUCT = "DELETE FROM products WHERE LOWER(product_name) = LOWER(?) RETURNING *";
    private static final String SQL_GET_PRODUCT = "SELECT * FROM products WHERE LOWER(product_name) = LOWER(?);";
    private static final String SQL_GET_PRODUCT_NAMES_SIZE = "SELECT COUNT(product_name) FROM products";
    private static final String SQL_GET_PRODUCT_NAMES = "SELECT product_name FROM products";
    private static final String SQL_GET_PRODUCTS = "SELECT * FROM products";

    private static final String SQL_CREATE_PRODUCTS = "CREATE TABLE IF NOT EXISTS products(id serial PRIMARY KEY, product_name varchar(145) NOT NULL UNIQUE CHECK(product_name <> ''), labor_cost decimal(8,4) NOT NULL CHECK(labor_cost >= 0), material_cost decimal(8,4));";

    private static final String SQL_GUESS_PRODUCT_NAMES = "WITH inputQuery(n) AS (SELECT ?), "
            + "mainQuery AS ("
            + "              SELECT product_name, 1 AS rank FROM products WHERE product_name = (SELECT n FROM inputQuery) "
            + "              UNION ALL SELECT product_name, 2 AS rank FROM products WHERE product_name = (SELECT LOWER(n) FROM inputQuery) "
            + "              UNION ALL SELECT product_name, 3 AS rank FROM products WHERE product_name LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery) "
            + "              UNION ALL SELECT product_name, 4 AS rank FROM products WHERE product_name LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + "          )"
            + " SELECT t1.product_name FROM mainQuery t1"
            + " JOIN ("
            + "	SELECT product_name, MIN(rank) min_rank"
            + "	FROM mainQuery"
            + "	GROUP BY product_name"
            + ") t2"
            + " ON t1.product_name = t2.product_name AND t1.rank = t2.min_rank"
            + " ORDER BY t1.rank DESC";

    @Inject
    public ProductDaoPostgresImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        jdbcTemplate.execute(SQL_CREATE_PRODUCTS);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Product create(Product product) {
        if (product != null) {
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

        } else {
            return null;
        }
    }

    @Override
    public Product get(String name) {
        String input = null;

        if (name == null) {
            return null;
        }

        try {
            return jdbcTemplate.queryForObject(SQL_GET_PRODUCT, new ProductRowMapper(), name);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Product update(Product product) {

        if (product == null) {
            return null;
        }

        convertProductNameToTitleCase(product);

        if (get(product.getProductName()) == null) {
            return create(product);
        } else {
            return jdbcTemplate.queryForObject(SQL_UPDATE_PRODUCT,
                    new ProductRowMapper(),
                    product.getLaborCost(),
                    product.getCost(),
                    product.getProductName());
        }
    }

    private void convertProductNameToTitleCase(Product product) {
        String titleCaseName = com.mycompany.flooringmasteryweb.utilities.TextUtilities.toTitleCase(product.getProductName());
        product.setProductName(titleCaseName);
    }

    @Override
    public Product delete(Product product) {

        if (product == null) {
            return null;
        }

        convertProductNameToTitleCase(product);

        String name = product.getProductName();
        try {
            return jdbcTemplate.queryForObject(SQL_DELETE_PRODUCT, new ProductRowMapper(), name);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {

        }
        
        return null;
    }

    @Override
    public List<String> getList() {
        return jdbcTemplate.query(SQL_GET_PRODUCT_NAMES, new ProductNameMapper());
    }

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_GET_PRODUCT_NAMES_SIZE, Integer.class);
    }

    private final class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int i) throws SQLException {

            Product product = new Product();

            product.setId(rs.getInt("id"));
            product.setLaborCost(rs.getDouble("labor_cost"));
            product.setProductName(rs.getString("product_name"));
            product.setCost(rs.getDouble("material_cost"));

            return product;
        }
    }

    private final class ProductNameMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int i) throws SQLException {
            String productName = rs.getString("product_name");
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

        String[] productNames = jdbcTemplate.queryForObject(SQL_GUESS_PRODUCT_NAMES, String[].class, inputName);

        return Arrays.asList(productNames);
    }

    @Override
    public List<Product> getListOfProducts() {
        return jdbcTemplate.query(SQL_GET_PRODUCTS, new ProductRowMapper());
    }   

    @Override
    public List<ProductCommand> buildCommandProductList() {
        return getListOfProducts().stream()
                .map(product -> ProductCommand.buildProductCommand(product))
                .collect(Collectors.toList());
    }
}
