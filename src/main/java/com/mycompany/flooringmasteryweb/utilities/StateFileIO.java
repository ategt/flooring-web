/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dto.State;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author apprentice
 */
public interface StateFileIO {

    Map<String, State> decode(File stateDataFile);

    void encode(File stateDataFile, List<String> listToEncode);
    
}
