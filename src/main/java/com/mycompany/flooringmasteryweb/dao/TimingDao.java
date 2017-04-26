/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.utilities.OrderDaoFileIOImplementation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author apprentice
 */
public class TimingDao {
    
    
     private ApplicationContext ctx;

    //public AuditAspect() {
        //ctx = ApplicationContextProvider.getApplicationContext.getBean("BeanId", MyBean.class);
        //ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
   
     private List<Order> orders;
    private int nextId;
    private StateDao stateDao;
    private ProductDao productDao;
    private ConfigDao configDao;
    private boolean isATest;
    private com.mycompany.flooringmasteryweb.utilities.OrderDaoFileIO orderIo;
    

    public TimingDao() {
       
    }

//    public TimingDao(ProductDao productDao, StateDao stateDao, ConfigDao configDao) {
//
//             ctx = com.mycompany.flooringmasteryweb.aop.ApplicationContextProvider.getApplicationContext();
//   
//        this.productDao = productDao;
//        this.stateDao = stateDao;
//
//        if (configDao != null) {
//            this.configDao = configDao;
//            this.isATest = configDao.get().isInTestMode();
//        } else {
//            this.isATest = true;
//        }
//
//        this.orderIo = new OrderDaoFileIOImplementation(this, stateDao, productDao);
//
//        init(configDao);
//        
//        //this.orderIo = ctx.getBean(type)
//        //this.orderIo = new com.mycompany.flooringmasteryweb.utilities.OrderDaoFileIOImplementation(this, stateDao, productDao);
//        //this.orderIo = ctx.getBean(type)
//    }
//
//    private void init(ConfigDao configDao1) {
//        try {
//
//            File directoryToSearch = null;
//            if (isATest) {
//                directoryToSearch = configDao1.get().getTestDirectory();
//            } else {
//                directoryToSearch = configDao1.get().getOrdersDirectory();
//            }
//
//            List<Order> loadedOrders = new ArrayList();
//            java.io.File[] orderFiles = lookForOrders(directoryToSearch);
//            
//            for (java.io.File orderFile : orderFiles) {
//                if (!orderFile.getName().endsWith("00000000.txt")) {
//                    loadedOrders.addAll(orderIo.decode(orderFile));
//                }
//            }
//
//            java.util.Set<Integer> ids = new java.util.HashSet();
//            loadedOrders.stream().forEach(order -> {
//                ids.add(order.getId());
//            });
//
//            for (java.io.File orderFile : orderFiles) {
//                if (orderFile.getName().endsWith("00000000.txt")) {
//                    System.out.println("orderFile");
//                    for (Order order : orderIo.decode(orderFile)) {
//                        if (!ids.contains(order.getId())) {
//                            loadedOrders.add(order);
//                        }
//                    }
//                }
//            }
//
//            Set<Integer> orderNumbers = new HashSet();
//            for (Order order : loadedOrders) {
//                orderNumbers.add(order.getId());
//            }
//
//            if (orderNumbers.size() == loadedOrders.size()) {
//            } else {
//                System.out.println("ID Integrity Check Failed! \n " + orderNumbers.size() + " different order numbers but " + loadedOrders.size() + " orders.");
//            }
//
//            orders = loadedOrders;
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (orders == null) {
//            orders = new ArrayList();
//            System.out.println("The list was empty, making a new one.");
//        }
//        nextId = determineNextId();
//    }
    /*
    @Override
    public Order create(Order order) {
    order.setId(nextId);
    nextId++;
    
    orders.add(order);
    
    encode(order);
    
    return order;
    }
    
    @Override
    public Order get(Integer id) {
    
    for (Order order : orders) {
    if (order != null) {
    if (order.getId() == id) {
    return order;
    }
    }
    }
    
    return null;
    }
    
    @Override
    public void update(Order order) {
    List<Order> foundOrders = new ArrayList();
    
    orders.stream()
    .filter(currentOrder -> currentOrder.getId() == order.getId())
    .forEach(currentOrder -> {
    foundOrders.add(currentOrder);
    });
    
    foundOrders.stream()
    .forEach(f -> {
    orders.remove(f);
    encode(f.getDate());
    });
    //        for (Order foundOrder : foundOrders) {
    //            orders.remove(foundOrder);
    //            encode(foundOrder.getDate());
    //        }
    
    orders.add(order);
    encode(extractDate("Orders_00000000.txt"));
    encode(order);
    
    }
    
    @Override
    public void delete(Order order) {
    Order found = null;
    
    for (Order currentOrder : orders) {
    if (currentOrder.getId() == order.getId()) {
    found = currentOrder;
    break;
    }
    }
    
    Date oldDate = null;
    if (found != null) {
    oldDate = found.getDate();
    orders.remove(found);
    }
    
    encode(extractDate("Orders_00000000.txt"));
    
    encode(oldDate);
    
    }*/

    
}
