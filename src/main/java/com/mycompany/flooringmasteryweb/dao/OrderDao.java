/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.OrderResultSegment;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import com.mycompany.flooringmasteryweb.dto.OrderSortByEnum;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ResultProperties;
import com.mycompany.flooringmasteryweb.dto.ResultSegement;
import com.mycompany.flooringmasteryweb.dto.State;
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

    @Deprecated
    List<Order> getList();
    List<Order> list(ResultSegement<OrderSortByEnum> resultSegment);
    
    List<Date> listOrderDates();
    List<Integer> listOrderNumbers();

    Order orderBuilder(OrderCommand basicOrder);

    OrderCommand resolveOrderCommand(Order order);

    List<Order> searchByDate(Date date);
    List<Order> searchByName(String orderName);
    List<Order> searchByProduct(Product product);
    List<Order> searchByState(State state);
    List<Order> searchByOrderNumber(Integer orderNumber);    
    List<Order> search(OrderSearchRequest searchRequest, ResultSegement<OrderSortByEnum> resultSegment);    

    int size();    
}
