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
import java.util.Date;
import java.util.List;

/**
 *
 * @author apprentice
 */
public interface OrderDao {

    Order create(Order order);
    void delete(Order order);
    Order get(Integer id);

    @Deprecated
    List<Order> getList();

    List<Date> listOrderDates();
    List<Integer> listOrderNumbers();

    Order orderBuilder(OrderCommand basicOrder);

    OrderCommand resolveOrderCommand(Order order);

    List<Order> searchByDate(Date date);
    List<Order> searchByName(String orderName);
    List<Order> searchByProduct(Product product);
    List<Order> searchByState(State state);

    int size();

    void update(Order order);
    
    java.util.List<Order> searchByOrderNumber(Integer orderNumber);    
}
