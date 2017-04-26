/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.StateFileIO;
import com.mycompany.flooringmasteryweb.utilities.StateFileIOImplementation;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author apprentice
 */
public class StateDaoImpl implements StateDao {

    private java.util.Map<String, State> statesMap;
    private File stateDataFile = new File("StatesData.txt");
    private StateFileIO fileIo;

    public StateDaoImpl(ConfigDao configDao) {

        this.fileIo = new StateFileIOImplementation(this);
        stateDataFile = configDao.get().getTaxesFile();

        statesMap = fileIo.decode(stateDataFile);

        if (statesMap == null) {
            statesMap = new java.util.HashMap();
            System.out.println("The list was empty, making a new one.");
        }
    }

    @Override
    public State create(State state) {
        if (state == null) {
            return null;
        } else {
            return create(state.getState(), state);
        }
    }

    @Override
    public State create(State state, String stateName) {
        return create(stateName, state);
    }

    /**
     * The state Name must be the two character state postal code abbreviation
     * and must match the getState() method of the passed in state object.
     *
     * @param stateName
     * @param state
     * @return
     */
    @Override
    public State create(String stateName, State state) {
        State returnedState = null;

        if (state.getState() == null) {
        } else if (stateName == null) {
        } else if (stateName.length() != 2) {
        } else if (stateName.equals(state.getState())) {

            String postalCode = stateName.toUpperCase();

            if (!statesMap.containsKey(postalCode)) {

                state.setState(postalCode);
                statesMap.put(postalCode, state);

                fileIo.encode(stateDataFile, getList());

                returnedState = state;
            }
        } else {
            // Look up how to throw exceptions and consider that here.
        }
        return returnedState;
    }

    @Override
    public State get(String name) {

        if (name == null) {
            return null;
        }

        String input = null;
        for (String stateTest : statesMap.keySet()) {
            if (name.equalsIgnoreCase(stateTest)) {
                input = stateTest;
                break;
            }
        }
        return statesMap.get(input);
    }

    @Override
    public void update(State state) {

        if (state != null) {
            State foundState = statesMap.get(state.getState());

            if (foundState != null) {

                if (foundState.getState().equals(state.getState())) {
                    statesMap.remove(foundState.getState());
                    statesMap.put(state.getState(), state);

                    fileIo.encode(stateDataFile, getList());
                } else {
                    System.out.println("Throwing a State Not Found exception!!!!");
                    // Look up exception throwing and consider putting one here, too!
                }
            } else {
                create(state);
                //System.out.println("Throwing a State is null exception!!!!");
                // Look up exception throwing and consider putting one here, too!
            }
        }
    }

    @Override
    public void delete(State state) {

        if (statesMap.containsKey(state.getState())) {
            statesMap.remove(state.getState());
            fileIo.encode(stateDataFile, getList());
        } else {
            System.out.println("Throwing a State Not Found exception!!!!");
            // Look up exception throwing and consider putting one here, too!

        }
    }

    @Override
    public List<String> getList() {
        return new ArrayList(statesMap.keySet());
    }

    @Override
    public int size() {
        return statesMap.size();
    }

    @Override
    public List<State> getListOfStates() {

        List<State> states = getList().stream()
                .map(name -> get(name))
                .collect(Collectors.toList());

        return states;
    }

    @Override
    public List<State> sortByStateName(List<State> states) {

        states.sort(
                new Comparator<State>() {
            public int compare(State c1, State c2) {
                return c1.getStateName().compareTo(c2.getStateName());
            }
        });

        return states;
    }

    @Override
    public List<State> sortByStateNameRev(List<State> states) {
        List<State> shallowCopy = sortByStateName(states).subList(0, states.size());
        Collections.reverse(shallowCopy);
        return shallowCopy;
    }

    @Override
    public List<State> sortByStateTax(List<State> states) {

        states.sort(
                new Comparator<State>() {
            public int compare(State c1, State c2) {
                return Double.compare(c1.getStateTax(), c2.getStateTax());
            }
        });

        return states;
    }

    @Override
    public List<State> sortByStateTaxRev(List<State> states) {
        List<State> shallowCopy = sortByStateName(states).subList(0, states.size());
        Collections.reverse(shallowCopy);
        return shallowCopy;
    }

    @Override
    public StateCommand buildCommandState(State state) {

        StateCommand stateCommand = new StateCommand();

        if (StateUtilities.validStateAbbr(state.getStateName())) {
            String stateAbbreviation = state.getStateName();
            String stateName = StateUtilities.stateFromAbbr(stateAbbreviation);

            stateCommand.setStateAbbreviation(stateAbbreviation);
            stateCommand.setStateName(stateName);

            stateCommand.setStateTax(state.getStateTax());

        } else if (StateUtilities.validStateInput(state.getStateName())) {
            String guessedName = StateUtilities.bestGuessStateName(state.getStateName());
            String stateAbbreviation = StateUtilities.abbrFromState(guessedName);

            stateCommand.setStateAbbreviation(stateAbbreviation);
            stateCommand.setStateName(guessedName);

            stateCommand.setStateTax(state.getStateTax());

        }

        return stateCommand;
    }

    @Override
    public List<StateCommand> buildCommandStateList(List<State> states) {
        List<StateCommand> resultsList = new ArrayList();

        for (State state : states) {

            resultsList.add(buildCommandState(state));

        }

        return resultsList;
    }

    @Override
    public List<StateCommand> sortByStateFullName(List<StateCommand> states) {

        states.sort(
                new Comparator<StateCommand>() {
            public int compare(StateCommand c1, StateCommand c2) {
                return c1.getStateName().compareTo(c2.getStateName());
            }
        });

        return states;
    }

    @Override
    public List<StateCommand> sortByStateFullNameRev(List<StateCommand> states) {
        List<StateCommand> shallowCopy = sortByStateFullName(states).subList(0, states.size());
        Collections.reverse(shallowCopy);
        return shallowCopy;
    }

}
