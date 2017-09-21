/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.OrderResultSegment;
import com.mycompany.flooringmasteryweb.dto.OrderSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import com.mycompany.flooringmasteryweb.dto.OrderSortByEnum;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ResultSegment;
import com.mycompany.flooringmasteryweb.dto.State;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author apprentice
 */
public class OrderDaoPostgresImpl implements OrderDao {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;
    private StateDao stateDao;

    private static final String SQL_INSERT_ORDER = "INSERT INTO orders (customer_name, material_cost, tax_rate, total_tax, grand_total, date, labor_cost, area, cost_per_square_foot, labor_cost_per_square_foot, product_id, state_id ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING id;";
    private static final String SQL_UPDATE_ORDER = "UPDATE orders SET customer_name = ?, material_cost = ?, tax_rate = ?, total_tax = ?, grand_total = ?, date = ?, labor_cost = ?, area = ?, cost_per_square_foot = ?, labor_cost_per_square_foot = ?, product_id = ?, state_id = ? WHERE id = ? RETURNING *";
    private static final String SQL_DELETE_ORDER = "DELETE FROM orders WHERE id = ? RETURNING *";
    private static final String SQL_GET_ORDER = "SELECT * FROM orders WHERE id = ?";
    private static final String SQL_GET_ORDER_LIST = "SELECT *, 1 AS rank FROM orders";
    private static final String SQL_GET_ORDER_ID_LIST_SPECIALTY = "SELECT id FROM orders";
    private static final String SQL_GET_ORDER_DATE_LIST_SPECIALTY = "SELECT date FROM orders";
    private static final String SQL_COUNT_ORDERS = "SELECT COUNT(*) FROM orders";

    private static final String SQL_CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS orders (id SERIAL PRIMARY KEY, customer_name varchar(145), material_cost decimal(10,2), tax_rate decimal(6,4), total_tax decimal(10,2), grand_total decimal(10,2), date date, labor_cost decimal(10,2), area decimal(16,4), cost_per_square_foot decimal(10,3), labor_cost_per_square_foot decimal(10,3), product_id varchar(145), state_id varchar(3));";

    @Inject
    public OrderDaoPostgresImpl(JdbcTemplate jdbcTemplate, StateDao stateDao, ProductDao productDao) {
        this.productDao = productDao;
        this.stateDao = stateDao;
        this.jdbcTemplate = jdbcTemplate;

        jdbcTemplate.execute(SQL_CREATE_ORDER_TABLE);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Order create(Order order) {

        if (order == null) {
            return null;
        }

        if (order.getState() == null || order.getState().getStateName() == null) {
            return null;
        }

        if (order.getProduct() == null || order.getProduct().getProductName() == null) {
            return null;
        }

        try {

            Integer id = jdbcTemplate.queryForObject(SQL_INSERT_ORDER,
                    Integer.class,
                    order.getName(),
                    order.getMaterialCost(),
                    order.getTaxRate(),
                    order.getTax(),
                    order.getTotal(),
                    order.getDate(),
                    order.getLaborCost(),
                    order.getArea(),
                    order.getCostPerSquareFoot(),
                    order.getLaborCostPerSquareFoot(),
                    order.getProduct().getProductName(),
                    order.getState().getStateName());

            order.setId(id);

            return order;

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return null;
        }
    }

    @Override
    public Order get(Integer id) {

        if (id == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(SQL_GET_ORDER, new OrderMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Order update(Order order) {

        if (order == null) {
            return null;
        }

        if (order.getId() > 0) {

            if (order.getState() == null || order.getState().getStateName() == null) {
                return null;
            }

            if (order.getProduct() == null || order.getProduct().getProductName() == null) {
                return null;
            }

            try {

                return jdbcTemplate.queryForObject(SQL_UPDATE_ORDER,
                        new OrderMapper(),
                        order.getName(),
                        order.getMaterialCost(),
                        order.getTaxRate(),
                        order.getTax(),
                        order.getTotal(),
                        order.getDate(),
                        order.getLaborCost(),
                        order.getArea(),
                        order.getCostPerSquareFoot(),
                        order.getLaborCostPerSquareFoot(),
                        order.getProduct().getProductName(),
                        order.getState().getStateName(),
                        order.getId());

            } catch (org.springframework.dao.DataIntegrityViolationException ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Order delete(Order order) {
        if (order == null) {
            return null;
        }

        Integer id = order.getId();

        return delete(id);
    }

    @Override
    public Order delete(Integer id) {
        try {
            return jdbcTemplate.queryForObject(SQL_DELETE_ORDER, new OrderMapper(), id);
        } catch (EmptyResultDataAccessException ignored) {
        }
        return null;
    }

    @Override
    public List<Order> list(ResultSegment<OrderSortByEnum> resultSegment) {
        return jdbcTemplate.query(sortAndPaginateQuery(SQL_GET_ORDER_LIST, resultSegment), new OrderMapper());
    }

    @Override
    public List<Order> search(OrderSearchRequest searchRequest, ResultSegment<OrderSortByEnum> resultSegment) {
        if (searchRequest == null) {
            return Collections.emptyList();
        }

        return search(searchRequest.getSearchText(), searchRequest.getSearchBy(), resultSegment);
    }

    private List<Order> search(String queryString,
                               OrderSearchByOptionEnum searchOption,
                               ResultSegment<OrderSortByEnum> resultProperties) {

        List<Order> orders;
        String sqlSearchQuery;

        if (null == searchOption) {
            orders = Collections.emptyList();
        } else {
            sqlSearchQuery = determineSqlSearchQuery(searchOption);
            orders = search(queryString, sqlSearchQuery, resultProperties);
        }
        return orders;
    }

    private List<Order> search(String stringToSearchFor, String sqlQueryToUse, ResultSegment<OrderSortByEnum> resultProperties) {
        List<Order> result = jdbcTemplate.query(sortAndPaginateQuery(sqlQueryToUse, resultProperties), new OrderMapper(), stringToSearchFor);

        return result;
    }

    private final class OrderMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int i) throws SQLException {

            Order order = new Order();

            order.setId(rs.getInt("id"));
            order.setName(rs.getString("customer_name"));
            order.setMaterialCost(rs.getDouble("material_cost"));
            order.setTaxRate(rs.getDouble("tax_rate"));
            order.setTax(rs.getDouble("total_tax"));
            order.setTotal(rs.getDouble("grand_total"));
            order.setDate(rs.getDate("date"));
            order.setLaborCost(rs.getDouble("labor_cost"));
            order.setArea(rs.getDouble("area"));

            order.setCostPerSquareFoot(rs.getDouble("cost_per_square_foot"));
            order.setLaborCostPerSquareFoot(rs.getDouble("labor_cost_per_square_foot"));

            String productName = rs.getString("product_id");

            Product product = productDao.get(productName);

            order.setProduct(product);

            String stateName = rs.getString("state_id");

            State state = stateDao.get(stateName);

            order.setState(state);

            return order;
        }
    }

    public List<Order> getAllOrders() {
        return getList();
    }

    @Override
    public List<Order> getList() {
        return jdbcTemplate.query(SQL_GET_ORDER_LIST, new OrderMapper());
    }

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_COUNT_ORDERS, Integer.class
        );
    }

    @Override
    public java.util.List<Order> searchByDate(java.util.Date date) {
        if (Objects.isNull(date)) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String dateString = year + "-" + month + "-" + day;

        return search(new OrderSearchRequest(dateString, OrderSearchByOptionEnum.DATE), new OrderResultSegment(OrderSortByEnum.SORT_BY_NAME, 0, Integer.MAX_VALUE));
    }

    @Override
    public java.util.List<Order> searchByProduct(Product product) {
        if (Objects.isNull(product)) {
            return null;
        }
        return search(new OrderSearchRequest(product.getProductName(), OrderSearchByOptionEnum.PRODUCT), new OrderResultSegment(OrderSortByEnum.SORT_BY_NAME, 0, Integer.MAX_VALUE));
    }

    @Override
    public java.util.List<Order> searchByOrderNumber(Integer orderNumber) {
        if (Objects.isNull(orderNumber)) {
            return null;
        }
        return search(
                new OrderSearchRequest(Integer.toString(orderNumber), OrderSearchByOptionEnum.ORDER_NUMBER),
                new OrderResultSegment(OrderSortByEnum.SORT_BY_NAME, Integer.MAX_VALUE, 0)
        );
    }

    @Override
    public java.util.List<Order> searchByState(State state) {
        if (Objects.isNull(state)) {
            return null;
        }
        return search(
                new OrderSearchRequest(state.getStateName(), OrderSearchByOptionEnum.STATE),
                new OrderResultSegment(OrderSortByEnum.SORT_BY_NAME, Integer.MAX_VALUE, 0)
        );
    }

    @Override
    public java.util.List<Integer> listOrderNumbers() {
        return Arrays.asList(jdbcTemplate.queryForObject(SQL_GET_ORDER_ID_LIST_SPECIALTY, Integer[].class));
    }

    @Override
    public java.util.List<java.util.Date> listOrderDates() {
        return Arrays.asList(jdbcTemplate.queryForObject(SQL_GET_ORDER_DATE_LIST_SPECIALTY, Date[].class));
    }

    @Override
    public java.util.List<Order> searchByName(String orderName) {
        if (Objects.isNull(orderName)) {
            return null;
        }
        return search(
                new OrderSearchRequest(orderName, OrderSearchByOptionEnum.NAME),
                new OrderResultSegment(OrderSortByEnum.SORT_BY_NAME, Integer.MAX_VALUE, 0)
        );
    }

    @Override
    public Order orderBuilder(OrderCommand basicOrder) {
        Order newOrder = new Order();

        if (basicOrder == null) {
            return null;
        }

        newOrder.setName(basicOrder.getName());
        newOrder.setId(basicOrder.getId());
        newOrder.setDate(basicOrder.getDate());

        State state = stateDao.get(basicOrder.getState());
        newOrder.setState(state);

        if (newOrder.getState() != null) {
            newOrder.setTaxRate(stateDao.get(newOrder.getState().getState()).getStateTax());
            double taxRate = newOrder.getState().getStateTax();
            newOrder.setTaxRate(taxRate);
        }

        Product product = productDao.get(basicOrder.getProduct());
        newOrder.setProduct(product);
        newOrder.setArea(basicOrder.getArea());

        double laborCostPerFoot = 0.0;
        double materialCost = 0.0;
        double taxRate = 0.0;

        if (newOrder.getProduct() != null) {
            newOrder.setCostPerSquareFoot(newOrder.getProduct().getCost());

            materialCost = newOrder.getProduct().getCost() * newOrder.getArea();
            newOrder.setMaterialCost(materialCost);

            laborCostPerFoot = newOrder.getProduct().getLaborCost();
            newOrder.setLaborCostPerSquareFoot(laborCostPerFoot);
        }

        if (newOrder.getState() != null) {
            taxRate = newOrder.getState().getStateTax();
        }

        double totalLaborCost = laborCostPerFoot * newOrder.getArea();
        newOrder.setLaborCost(totalLaborCost);

        double subTotal = totalLaborCost + materialCost;

        double totalTax = (subTotal * (taxRate / 100));

        newOrder.setTax(totalTax);

        double totalCost = subTotal + totalTax;

        newOrder.setTotal(totalCost);

        return newOrder;
    }

    @Override
    public OrderCommand resolveOrderCommand(Order order) {

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

    private static final String SQL_SEARCH_ORDERS_BY_DATE = "WITH inputQuery(n) AS (SELECT ?), "
            + "firstQuery AS ( "
            + "	SELECT * FROM orders "
            + " 		WHERE CONCAT(DATE_PART('month', date), '-', DATE_PART('day', date), '-', DATE_PART('year', date)) "
            + "		LIKE (SELECT REPLACE(REPLACE(n  ,'/', '-'), '\\', '-') FROM inputQuery) "
            + "	), "
            + "secondQuery AS ( "
            + "	SELECT * FROM orders "
            + " 		WHERE CONCAT(DATE_PART('year', date), '-', DATE_PART('month', date), '-', DATE_PART('day', date)) "
            + "		LIKE (SELECT REPLACE(REPLACE(n  ,'/', '-'), '\\', '-') FROM inputQuery) "
            + "	) "
            + "	SELECT *, 1 AS rank FROM firstQuery "
            + "	UNION ALL SELECT *, 1 AS rank FROM secondQuery WHERE NOT EXISTS (SELECT * FROM firstQuery)";

    private static final String SQL_SEARCH_ORDERS_BY_ORDER_NUMBER = "SELECT *, 1 AS rank FROM orders WHERE CONCAT(id, '') LIKE ?";
    private static final String SQL_SEARCH_ORDERS_BY_STATE = "SELECT *, 1 AS rank FROM orders WHERE state_id = ?";

    private static final String SQL_SEARCH_ORDERS_BY_PRODUCT = "WITH inputQuery(n) AS (SELECT ?), "
            + "	mainQuery AS ( "
            + "		SELECT *, 1 AS rank FROM orders WHERE product_id = (SELECT n FROM inputQuery) "
            + "		UNION ALL SELECT *, 2 AS rank FROM orders WHERE LOWER(product_id) = (SELECT LOWER(n) FROM inputQuery) "
            + "		UNION ALL SELECT *, 3 AS rank FROM orders WHERE LOWER(product_id) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery) "
            + "		UNION ALL SELECT *, 4 AS rank FROM orders WHERE LOWER(product_id) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery) "
            + "		UNION ALL SELECT *, 5 AS rank FROM orders WHERE  "
            + "			LOWER(product_id) LIKE  "
            + "			  ALL(  "
            + "			    ARRAY( "
            + "				   SELECT CONCAT('%', input_column, '%') FROM (SELECT unnest(string_to_array(n, ' ')) AS input_column FROM inputQuery) AS augmented_input_table  "
            + "				 )  "
            + "			   )  "
            + "			) "
            + "SELECT t1.* FROM mainQuery t1 "
            + "JOIN ( "
            + "	SELECT id, MIN(rank) min_rank "
            + "   FROM mainQuery "
            + "   GROUP BY id "
            + ") t2  "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ORDERS_BY_NAME = "WITH inputQuery(n) AS (SELECT ?), "
            + "	mainQuery AS ( "
            + "		SELECT *, 1 AS rank FROM orders WHERE customer_name = (SELECT n FROM inputQuery) "
            + "		UNION ALL SELECT *, 2 AS rank FROM orders WHERE LOWER(customer_name) = (SELECT LOWER(n) FROM inputQuery) "
            + "		UNION ALL SELECT *, 3 AS rank FROM orders WHERE LOWER(customer_name) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery) "
            + "		UNION ALL SELECT *, 4 AS rank FROM orders WHERE LOWER(customer_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery) "
            + "		UNION ALL SELECT *, 5 AS rank FROM orders WHERE "
            + "			LOWER(customer_name) LIKE  "
            + "			  ALL(  "
            + "			    ARRAY( "
            + "				   SELECT CONCAT('%', input_column, '%') FROM (SELECT unnest(string_to_array(n, ' ')) AS input_column FROM inputQuery) AS augmented_input_table  "
            + "				 )  "
            + "			   )  "
            + "			) "
            + "SELECT t1.* FROM mainQuery t1 "
            + "JOIN ( "
            + "	SELECT id, MIN(rank) min_rank "
            + "   FROM mainQuery "
            + "   GROUP BY id "
            + ") t2  "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ORDERS_BY_EVERYTHING = "WITH inputQuery(n) AS (SELECT ?), "
            + "	mainQuery AS ( "
            + "		SELECT *, 1 AS rank FROM orders WHERE CONCAT('', id) LIKE (SELECT CONCAT('', n) FROM inputQuery) "
            + "		UNION ALL SELECT *, 1 AS rank FROM orders WHERE DATE_PART('month', date) || '-' || DATE_PART('day', date) || '-' || DATE_PART('year', date) LIKE (SELECT REPLACE(REPLACE(n, '\\', '-'), '/', '-') FROM inputQuery) "
            + "		UNION ALL SELECT *, 2 AS rank FROM orders WHERE CONCAT(' ', product_id, ' ', state_id, ' ', customer_name, ' ') LIKE (SELECT CONCAT('% ', n, ' %') FROM inputQuery) "
            + "		UNION ALL SELECT *, 3 AS rank FROM orders WHERE LOWER(CONCAT(' ', product_id, ' ', state_id, ' ', customer_name, ' ')) LIKE (SELECT LOWER(CONCAT('% ', n, ' %')) FROM inputQuery) "
            + "		UNION ALL SELECT *, 4 AS rank FROM orders WHERE LOWER(CONCAT(' ', product_id, ' ', state_id, ' ', customer_name, ' ')) LIKE (SELECT LOWER(CONCAT('% ', n, '%')) FROM inputQuery) "
            + "		UNION ALL SELECT *, 5 AS rank FROM orders WHERE LOWER(CONCAT(' ', product_id, ' ', state_id, ' ', customer_name, ' ')) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery) "
            + "		UNION ALL SELECT *, 6 AS rank FROM orders WHERE  "
            + "			LOWER(CONCAT(' ', product_id, ' ', state_id, ' ', customer_name, ' ')) LIKE  "
            + "			  ALL(  "
            + "			    ARRAY( "
            + "				   SELECT CONCAT('%', input_column, '%') FROM (SELECT unnest(string_to_array(n, ' ')) AS input_column FROM inputQuery) AS augmented_input_table  "
            + "				 )  "
            + "			   )  "
            + "			) "
            + "SELECT t1.* FROM mainQuery t1 "
            + "JOIN ( "
            + "	SELECT id, MIN(rank) min_rank "
            + "   FROM mainQuery "
            + "   GROUP BY id "
            + ") t2  "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private String determineSqlSearchQuery(OrderSearchByOptionEnum searchOption) {
        String sqlSearchQuery;
        switch (searchOption) {
            case DATE:
                sqlSearchQuery = SQL_SEARCH_ORDERS_BY_DATE;
                break;
            case NAME:
                sqlSearchQuery = SQL_SEARCH_ORDERS_BY_NAME;
                break;
            case ORDER_NUMBER:
                sqlSearchQuery = SQL_SEARCH_ORDERS_BY_ORDER_NUMBER;
                break;
            case PRODUCT:
                sqlSearchQuery = SQL_SEARCH_ORDERS_BY_PRODUCT;
                break;
            case STATE:
                sqlSearchQuery = SQL_SEARCH_ORDERS_BY_STATE;
                break;
            case EVERYTHING:
            default:
                sqlSearchQuery = SQL_SEARCH_ORDERS_BY_EVERYTHING;
                break;
        }
        return sqlSearchQuery;
    }

    private String paginateQuery(String query, Integer page, Integer resultsPerPage) {
        if (page == null || resultsPerPage == null) {
            return query;
        }

        if (page < 0 || resultsPerPage < 0) {
            return query;
        }

        if (query.contains(";")) {
            throw new UnsupportedOperationException("Pagination Method can not handle semi-colons(;)");
        }

        long offset = (long) resultsPerPage * (long) page;

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("SELECT * FROM (");
        stringBuffer.append(query);
        stringBuffer.append(") AS innerQuery OFFSET ");
        stringBuffer.append(offset);
        stringBuffer.append(" LIMIT ");
        stringBuffer.append(resultsPerPage);

        return stringBuffer.toString();
    }

    private static final String SQL_SORT_ORDERS_BY_DATE_PARTIAL = " ORDER BY rank ASC, date ASC, customer_name ASC, state_id ASC, product_id ASC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_DATE_INVERSE_PARTIAL = " ORDER BY rank ASC, date DESC, customer_name ASC, state_id ASC, product_id ASC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_ID_PARTIAL = " ORDER BY rank ASC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_ID_INVERSE_PARTIAL = " ORDER BY rank ASC, id DESC";
    private static final String SQL_SORT_ORDERS_BY_NAME_PARTIAL = " ORDER BY rank ASC, customer_name ASC, date ASC, state_id ASC, product_id ASC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_NAME_INVERSE_PARTIAL = " ORDER BY rank ASC, customer_name DESC, date ASC, state_id ASC, product_id ASC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_PRODUCT_PARTIAL = " ORDER BY rank ASC, product_id ASC, customer_name ASC, date ASC, state_id ASC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_PRODUCT_INVERSE_PARTIAL = " ORDER BY rank ASC, product_id DESC, customer_name ASC, date ASC, state_id ASC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_STATE_PARTIAL = " ORDER BY rank ASC, state_id ASC, customer_name ASC, date ASC, product_id DESC, id ASC";
    private static final String SQL_SORT_ORDERS_BY_STATE_INVERSE_PARTIAL = " ORDER BY rank ASC, state_id DESC, customer_name ASC, date ASC, product_id DESC, id ASC";

    private String sortQuery(String query, OrderSortByEnum sortByEnum) {
        if (sortByEnum == null) {
            return query;
        }

        if (query.contains(";")) {
            throw new UnsupportedOperationException("Sorting Method can not handle semi-colons(;)");
        }

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("SELECT * FROM (");
        stringBuffer.append(query);
        stringBuffer.append(") AS preSortedQuery");

        switch (sortByEnum) {
            case SORT_BY_DATE:
                stringBuffer.append(SQL_SORT_ORDERS_BY_DATE_PARTIAL);
                break;
            case SORT_BY_DATE_INVERSE:
                stringBuffer.append(SQL_SORT_ORDERS_BY_DATE_INVERSE_PARTIAL);
                break;
            case SORT_BY_ID:
                stringBuffer.append(SQL_SORT_ORDERS_BY_ID_PARTIAL);
                break;
            case SORT_BY_NAME:
                stringBuffer.append(SQL_SORT_ORDERS_BY_NAME_PARTIAL);
                break;
            case SORT_BY_NAME_INVERSE:
                stringBuffer.append(SQL_SORT_ORDERS_BY_NAME_INVERSE_PARTIAL);
                break;
            case SORT_BY_PRODUCT:
                stringBuffer.append(SQL_SORT_ORDERS_BY_PRODUCT_PARTIAL);
                break;
            case SORT_BY_PRODUCT_INVERSE:
                stringBuffer.append(SQL_SORT_ORDERS_BY_PRODUCT_INVERSE_PARTIAL);
                break;
            case SORT_BY_STATE:
                stringBuffer.append(SQL_SORT_ORDERS_BY_STATE_PARTIAL);
                break;
            case SORT_BY_STATE_INVERSE:
                stringBuffer.append(SQL_SORT_ORDERS_BY_STATE_INVERSE_PARTIAL);
                break;
            case SORT_BY_ID_INVERSE:
            default:
                stringBuffer.append(SQL_SORT_ORDERS_BY_ID_INVERSE_PARTIAL);
                break;
        }

        return stringBuffer.toString();
    }

    private String sortAndPaginateQuery(String query, OrderSortByEnum sortByEnum, Integer page, Integer resultsPerPage) {
        return paginateQuery(sortQuery(query, sortByEnum), page, resultsPerPage);
    }

    private String sortAndPaginateQuery(String query, ResultSegment<OrderSortByEnum> resultProperties) {
        if (resultProperties == null) {
            return query;
        }
        return sortAndPaginateQuery(query, resultProperties.getSortByEnum(), resultProperties.getPageNumber(), resultProperties.getResultsPerPage());
    }
}
