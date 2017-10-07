/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ATeg
 */
public class OrderDaoTimingDummy implements OrderDao {

    long timeToSleep;

    public OrderDaoTimingDummy(long timeToSleep) {
        this.timeToSleep = timeToSleep;
    }

    @Override
    public Order create(Order order) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return order;
    }

    @Override
    public Order delete(Order order) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Order delete(Integer id) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Order get(Integer id) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Date> listOrderDates() {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> listOrderNumbers() {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public OrderCommand resolveOrderCommand(Order order) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> searchByDate(Date date) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Order> searchByName(String orderName) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Order> searchByProduct(Product product) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Order> searchByState(State state) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int size() {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public int size(OrderSearchRequest searchRequest) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public Order update(Order order) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Order> searchByOrderNumber(Integer orderNumber) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Order orderBuilder(OrderCommand basicOrder) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Order> list(ResultSegment<OrderSortByEnum> resultSegment) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Order> search(OrderSearchRequest searchRequest, ResultSegment<OrderSortByEnum> resultSegment) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}