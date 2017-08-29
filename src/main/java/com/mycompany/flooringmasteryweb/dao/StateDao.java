/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.State;
import java.util.List;

/**
 *
 * @author apprentice
 */
public interface StateDao {

    State create(State state);
    State get(String name);
    State update(State state);
    State delete(State state);

    List<String> getList();
    List<State> getListOfStates();

    int size();
}