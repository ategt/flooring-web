/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author apprentice
 */
public interface GenericMapFileIO<T> {
    
    Map<String, T> decode(File dataFile);

    void encode(File dataFile, List<String> encodingList);
    
    //void doNothing();

}
