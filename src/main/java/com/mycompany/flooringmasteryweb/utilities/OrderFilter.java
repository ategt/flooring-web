/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import java.io.*;

/**
 *
 * @author apprentice
 */
public class OrderFilter implements FileFilter {

    private final String extension = "txt";

    private final String orderPrefix = "orders_";

    @Override
    public boolean accept(File file) {        
        return (file.getName().toLowerCase().endsWith(extension) && file.getName().toLowerCase().startsWith(orderPrefix));
    }
}
