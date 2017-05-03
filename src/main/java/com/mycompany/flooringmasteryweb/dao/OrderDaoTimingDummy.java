/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.BasicOrder;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.State;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ATeg
 */
public class OrderDaoTimingDummy implements OrderDao {

    long timeToSleep;
    
    public OrderDaoTimingDummy(long timeToSleep){
        this.timeToSleep = timeToSleep;
    }
    
    @Override
    public String addLabels(Order order, String TOKEN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addLabels(String orderString, String TOKEN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addLabels(Order order, String TOKEN, String SECOND_TOKEN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addLabels(String orderString, String TOKEN, String SECOND_TOKEN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void delete(Order order) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Date extractDate(String dateString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public List<Order> getList() {
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
    public Order orderBuilder(BasicOrder basicOrder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void purgeTestFiles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public List<Order> sortByOrderNumber(List<Order> orders) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString(Order order) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString(Order order, String TOKEN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString(Order order, String TOKEN, String CSV_ESCAPE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Order order) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderDaoTimingDummy.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
