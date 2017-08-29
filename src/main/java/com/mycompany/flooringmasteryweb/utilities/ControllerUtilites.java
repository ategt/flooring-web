/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author ATeg
 */
public class ControllerUtilites {
    public static void loadStateCommandsToMap(StateDao stateDao, Map model) {
        List<StateCommand> stateCommands = stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());

        model.put("stateCommands", stateCommands);
    }
    
    public static List<StateCommand> stateList(StateDao stateDao) {
        return stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());
    }
}
