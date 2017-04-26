/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author apprentice
 */
public interface OrderDaoFileIO {

    List<Order> decode(File orderFile) throws FileNotFoundException, IOException;

    List<Order> decode(BufferedReader bufferedReader, String dateString);

    void encode(PrintWriter printWriter, List<Order> groupOfOrders);
    
}
