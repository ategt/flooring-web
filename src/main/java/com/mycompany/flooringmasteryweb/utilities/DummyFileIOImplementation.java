/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.Product;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apprentice
 */
public class DummyFileIOImplementation implements com.mycompany.flooringmasteryweb.utilities.GenericMapFileIO<Product> /*, com.mycompany.flooringmasteryweb.utilities.OrderDaoFileIO */{
    
    
    public Map<String, Product> decode(File dataFile) {
    //public List<Order> decode(File dataFile){
        //List<Order> emptyList = null;
        Map<String, Product> emptyMap = new java.util.HashMap();
        try {
            Thread.sleep(231);
        } catch (InterruptedException ex) {
            Logger.getLogger(DummyFileIOImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
            return null;
    }

    //@Override
    public void encode(File dataFile, List<String> encodingList) {
        try {
            Thread.sleep(251);
        } catch (InterruptedException ex) {
            Logger.getLogger(DummyFileIOImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Order> decode(BufferedReader bufferedReader, String dateString){
        try {
            Thread.sleep(251);
        } catch (InterruptedException ex) {
            Logger.getLogger(DummyFileIOImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    return null;
    }

    public void encode(PrintWriter printWriter, List<Order> groupOfOrders){
        try {
            Thread.sleep(251);
        } catch (InterruptedException ex) {
            Logger.getLogger(DummyFileIOImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    public void doNothing(){
        try {
            Thread.sleep(1251);
        } catch (InterruptedException ex) {
            Logger.getLogger(DummyFileIOImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
