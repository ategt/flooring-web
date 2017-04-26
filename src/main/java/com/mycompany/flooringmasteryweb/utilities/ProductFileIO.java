/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dto.Product;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author apprentice
 */
public interface ProductFileIO {

    Map<String, Product> decode(File productDataFile);

    void encode(File productDataFile, List<String> encodingList);
    
}
