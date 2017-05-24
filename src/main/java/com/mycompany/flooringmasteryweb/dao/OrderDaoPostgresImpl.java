/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.State;
import static com.mycompany.flooringmasteryweb.utilities.DateUtilities.isSameDay;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
public class OrderDaoPostgresImpl implements OrderDao {

    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;
    private StateDao stateDao;

    private static final String SQL_INSERT_ORDER = "INSERT INTO orders (customer_name, material_cost, tax_rate, total_tax, grand_total, date, labor_cost, area, cost_per_square_foot, labor_cost_per_square_foot, product_id, state_id ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING id;";
    private static final String SQL_UPDATE_ORDER = "UPDATE orders SET customer_name = ?, material_cost = ?, tax_rate = ?, total_tax = ?, grand_total = ?, date = ?, labor_cost = ?, area = ?, cost_per_square_foot = ?, labor_cost_per_square_foot = ?, product_id = ?, state_id = ? WHERE id = ?";
    private static final String SQL_DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
    private static final String SQL_GET_ORDER = "SELECT * FROM orders WHERE id = ?";
    private static final String SQL_GET_ORDER_LIST = "SELECT * FROM orders";
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
    public void update(Order order) {

        if (order == null) {
            return;
        }

        if (order.getId() > 0) {

            if (order.getState() == null || order.getState().getStateName() == null) {
                return;
            }

            if (order.getProduct() == null || order.getProduct().getProductName() == null) {
                return;
            }

            try {

                jdbcTemplate.update(SQL_UPDATE_ORDER,
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
                return;
            }
        }
    }

    @Override
    public void delete(Order order) {
        if (order == null) {
            return;
        }

        int id = order.getId();

        jdbcTemplate.update(SQL_DELETE_ORDER, id);
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
        return jdbcTemplate.queryForObject(SQL_COUNT_ORDERS, Integer.class);
    }

    @Override
    public java.util.List<Order> searchByDate(java.util.Date date) {
        java.util.List<Order> specificOrders = new ArrayList();

        getAllOrders().stream()
                .filter(o -> isSameDay(o.getDate(), date))
                .forEach(o -> specificOrders.add(o));

        return specificOrders;
    }

    @Override
    public java.util.List<Order> searchByProduct(Product product) {
        java.util.List<Order> specificOrders = getAllOrders().stream()
                .filter(o -> o.getProduct() != null)
                .filter(o -> o.getProduct() == product)
                .collect(Collectors.toList());

        return specificOrders;
    }

    @Override
    public java.util.List<Order> searchByOrderNumber(Integer orderNumber) {
        java.util.List<Order> specificOrders = getAllOrders().stream()
                .filter(o -> Integer.toString(o.getId()).contains(orderNumber.toString()))
                .collect(Collectors.toList());

        return specificOrders;
    }

    @Override
    public java.util.List<Order> searchByState(State state) {
        java.util.List<Order> specificOrders = getAllOrders().stream()
                .filter(o -> Objects.nonNull(o.getState()))
                .filter(o -> o.getState() == state)
                .collect(Collectors.toList());

        return specificOrders;
    }

    @Override
    public java.util.List<Integer> listOrderNumbers() {
        java.util.List<Integer> orderNumbers = new ArrayList();

        getAllOrders().stream()
                .forEach(o -> orderNumbers.add(o.getId()));

        Collections.sort(orderNumbers);

        return orderNumbers;
    }

    @Override
    public java.util.List<java.util.Date> listOrderDates() {
        java.util.List<java.util.Date> orderDates = new ArrayList();
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd-MM-");

        java.util.Map<String, java.util.Date> dateMap = new java.util.HashMap();

        for (Order order : getAllOrders()) {
            dateMap.putIfAbsent(fmt.format(order.getDate()), order.getDate());
        }

        orderDates.addAll(dateMap.values());

        java.util.Collections.sort(orderDates);

        return orderDates;
    }

    @Override
    public java.util.List<Order> searchByName(String orderName) {
        java.util.List<Order> specificOrders = new ArrayList();
        java.util.List<Order> closeOrders = new ArrayList();

        if (Objects.isNull(orderName)) {
            specificOrders.addAll(getAllOrders());
            return specificOrders;
        }

        for (Order order : getAllOrders()) {
            if (orderName.equalsIgnoreCase(order.getName())) {
                specificOrders.add(order);
            }

            if (Objects.nonNull(order)) {
                if (order.getName().toLowerCase().startsWith(orderName.toLowerCase()) || order.getName().toLowerCase().startsWith(orderName.toLowerCase())) {
                    closeOrders.add(order);
                }
            }
        }

        if (closeOrders.isEmpty()) {

            closeOrders = getAllOrders().stream()
                    .filter(o -> o.getName() != null)
                    .filter(o -> o.getName().toLowerCase().contains(orderName.toLowerCase()))
                    .collect(Collectors.toList());

        }

        if (specificOrders.isEmpty()) {
            return closeOrders;
        } else {
            return specificOrders;
        }
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
        int id = order.getId();
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