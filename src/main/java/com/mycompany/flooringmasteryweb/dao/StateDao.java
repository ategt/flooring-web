/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import java.util.List;

/**
 *
 * @author apprentice
 */
public interface StateDao {

    StateCommand buildCommandState(State state);
    List<StateCommand> buildCommandStateList(List<State> states);

    /**
     * The state Name must be the two character state postal code abbreviation
     * and must match the getState() method of the passed in state object.
     *
     * @param stateName
     * @param state
     * @return
     */
    State create(String stateName, State state);
    State create(State state);
    State create(State state, String stateName);

    State get(String name);
    void update(State state);
    void delete(State state);

    List<String> getList();
    List<State> getListOfStates();

    int size();

    List<StateCommand> sortByStateFullName(List<StateCommand> states);
    List<StateCommand> sortByStateFullNameRev(List<StateCommand> states);
    List<State> sortByStateName(List<State> states);
    List<State> sortByStateNameRev(List<State> states);
    List<State> sortByStateTax(List<State> states);
    List<State> sortByStateTaxRev(List<State> states);
}