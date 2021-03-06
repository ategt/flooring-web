/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.*;

import java.util.Date;
import java.util.List;

/**
 *
 * @author apprentice
 */
public interface OrderDao extends SizeableDao{

    Order get(Integer id);
    Order create(Order order);
    Order update(Order order);
    Order delete(Order order);
    Order delete(Integer id);

    List<Order> list(ResultSegment<OrderSortByEnum> resultSegment);
    
    List<Date> listOrderDates();
    List<Integer> listOrderNumbers();

    Order orderBuilder(OrderCommand basicOrder);

    OrderCommand resolveOrderCommand(Order order);

    List<Order> searchByDate(Date date);
    List<Order> searchByName(String orderName);
    List<Order> searchByProduct(Product product);
    List<Order> searchByState(State state);
    List<Order> searchByOrderNumber(Integer orderNumber);    
    List<Order> search(OrderSearchRequest searchRequest, ResultSegment<OrderSortByEnum> resultSegment);

    int size();
    int size(OrderSearchRequest searchRequest);
}
