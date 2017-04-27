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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author apprentice
 */
public class StateDaoPostgresDbImpl implements StateDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_STATE = "INSERT INTO states ( state_name, state_abbreviation, tax_rate ) VALUES ( ?, ?, ? ) RETURNING id";
    //private static final String SQL_UPDATE_STATE = "UPDATE states SET state_name=?, state_abbreviation=?, tax_rate=? WHERE state_abbreviation=?";
    private static final String SQL_UPDATE_STATE = "UPDATE states SET tax_rate=? WHERE state_abbreviation=?";
    private static final String SQL_DELETE_STATE = "DELETE FROM states WHERE state_abbreviation =?";
    private static final String SQL_GET_STATE = "SELECT * FROM states WHERE state_abbreviation = ? LIMIT 1";
    private static final String SQL_GET_STATE_ID = "SELECT * FROM states WHERE id = ? LIMIT 1";
    private static final String SQL_GET_STATE_LIST = "SELECT * FROM states";

    //private static final String SQL_CREATE_STATES = "CREATE TABLE IF NOT EXISTS states(id serial PRIMARY KEY, state_name varchar(45), state_abbreviation varchar(2) NOT NULL, tax_rate decimal(6,4), UNIQUE(state_name, state_abbreviation, tax_rate));";
    private static final String SQL_CREATE_STATES = "CREATE TABLE IF NOT EXISTS states(id serial PRIMARY KEY, state_name varchar(45), state_abbreviation varchar(2) NOT NULL, tax_rate decimal(6,4));";

    @Inject
    public StateDaoPostgresDbImpl(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

        initDatabase(jdbcTemplate);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public State create(String stateName, State state) {
        State returnedState = null;

        if (state.getState() == null) {
        } else if (stateName == null) {
        } else if (stateName.length() != 2) {
        } else if (stateName.equals(state.getState())) {

            String postalCode = stateName.toUpperCase();

            //state_name, state_abbreviation, tax_rate
            //first_name, last_name, street_number, street_name, city, state, zip
            try {
                Integer id = jdbcTemplate.queryForObject(SQL_INSERT_STATE, Integer.class, null, state.getStateName(), state.getStateTax());
//                jdbcTemplate.update(SQL_INSERT_STATE,
//                        null,
//                        state.getStateName(),
//                        state.getStateTax());
//                
//                Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

                state.setId(id);
                return state;

            } catch (org.springframework.dao.DuplicateKeyException ex) {
                return null;
            }

//            if (!statesMap.containsKey(postalCode)) {
//
//                state.setState(postalCode);
//                statesMap.put(postalCode, state);
//
//                fileIo.encode(stateDataFile, getList());
//
//                returnedState = state;
//            }
        } else {
            // Look up how to throw exceptions and consider that here.
        }
        return returnedState;
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
//    public State create(State state) {
//
//        if (state == null) {
//            return null;
//        }
//
//        //first_name, last_name, street_number, street_name, city, state, zip
//        jdbcTemplate.update(SQL_INSERT_STATE,
//                state.getTitle(),
//                state.getReleaseDate(),
//                state.getRating(),
//                state.getDirectorsName(),
//                state.getStudio());
//
//        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
//
//        state.setId(id);
//
//        return state;
//    }
    //@Override
//    public State get(Integer id) {
//
//        //if (id == null) {
//            return null;
//        //}
//        
////        try {
////            return jdbcTemplate.queryForObject(SQL_GET_STATE_ID, new StateMapper(), id);
////        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
////            return null;
////        }
//    }
    public State get(String name) {

        if (name == null) {
            return null;
        }

        try {
            return jdbcTemplate.queryForObject(SQL_GET_STATE, new StateMapper(), name);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void update(State state) {

        //"UPDATE states SET tax_rate=? WHERE state_abbreviation=?";
        if (state == null) {
            return;
        }

        if (state.getId() > 0) {

            jdbcTemplate.update(SQL_UPDATE_STATE,
                    state.getStateTax(),
                    state.getStateName());
        }

    }

    @Override
    public void delete(State state) {
        if (state == null) {
            return;
        }

        //int id = state.getId();
        //int id = state.getId();
        String name = state.getStateName();

        try {
            jdbcTemplate.update(SQL_DELETE_STATE, name);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {

        }

    }

    private static final String SQL_GET_STATE_NAMES = "SELECT state_abbreviation FROM states";

    @Override
    public List<String> getList() {
        return jdbcTemplate.query(SQL_GET_STATE_NAMES, new StateNameMapper());
        //return getListOfStates();
    }

    private void initDatabase(JdbcTemplate jdbcTemplate) {
        //final String SQL_CREATE_STATES = "CREATE TABLE states(id serial PRIMARY KEY, state_name varchar(45), state_abbreviation varchar(2) NOT NULL, tax_rate decimal(6,4), UNIQUE(state_name, state_abbreviation, tax_rate));";

        jdbcTemplate.execute(SQL_CREATE_STATES);
    }

//    public void setNoteDao(NoteDao noteDao) {
//        this.noteDao = noteDao;
//    }
    private final class StateMapper implements RowMapper<State> {

        @Override
        public State mapRow(ResultSet rs, int i) throws SQLException {

            //state_name, state_abbreviation, tax_rate
            State state = new State();

            //state.setId(rs.getInt("id"));
            state.setStateName(rs.getString("state_abbreviation"));
//            state.setStateName(rs.getString("state_name"));
            //state.setReleaseDate(rs.getDate("state_abbreviation"));

            try {
                String taxString = rs.getString("tax_rate");

                double tax = Double.parseDouble(taxString);
                state.setStateTax(tax);

            } catch (NullPointerException | NumberFormatException ex) {
                state.setStateTax(0.0d);
            }

//            state.setDirectorsName(rs.getString("directors_name"));
//            state.setStudio(rs.getString("studio"));
//
//            List<Note> notes = noteDao.getNotesForState(state);
            //state.setNotes(notes);
            return state;
        }

    }

    private final class StateNameMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int i) throws SQLException {

            //state_name, state_abbreviation, tax_rate
            String stateName = rs.getString("state_abbreviation");

//            state.setId(rs.getInt("id"));
//            state.setStateName(rs.getString("state_name"));
//            //state.setReleaseDate(rs.getDate("state_abbreviation"));
//
//            try {
//                String taxString = rs.getString("tax_rate");
//
//                double tax = Double.parseDouble(taxString);
//                state.setStateTax(tax);
//
//            } catch (NullPointerException | NumberFormatException ex) {
//                state.setStateTax(0.0d);
//            }
//            state.setDirectorsName(rs.getString("directors_name"));
//            state.setStudio(rs.getString("studio"));
//
//            List<Note> notes = noteDao.getNotesForState(state);
            //state.setNotes(notes);
            return stateName;
        }

    }

    @Override
    public List<State> getListOfStates() {

        return jdbcTemplate.query(SQL_GET_STATE_LIST, new StateMapper());
    }

    private static final String SQL_COUNT_STATES = "SELECT COUNT(*) FROM states";

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_COUNT_STATES, Integer.class);
    }
//
//
//    @Override
//    public State get(String name) {
//
//        if (name == null) {
//            return null;
//        }
//
//        String input = null;
//        for (String stateTest : statesMap.keySet()) {
//            if (name.equalsIgnoreCase(stateTest)) {
//                input = stateTest;
//                break;
//            }
//        }
//        return statesMap.get(input);
//    }
//
//    @Override
//    public void update(State state) {
//
//        if (state != null) {
//            State foundState = statesMap.get(state.getState());
//
//            if (foundState != null) {
//
//                if (foundState.getState().equals(state.getState())) {
//                    statesMap.remove(foundState.getState());
//                    statesMap.put(state.getState(), state);
//
//                    fileIo.encode(stateDataFile, getList());
//                } else {
//                    System.out.println("Throwing a State Not Found exception!!!!");
//                    // Look up exception throwing and consider putting one here, too!
//                }
//            } else {
//                create(state);
//                //System.out.println("Throwing a State is null exception!!!!");
//                // Look up exception throwing and consider putting one here, too!
//            }
//        }
//    }
//
//    @Override
//    public void delete(State state) {
//
//        if (statesMap.containsKey(state.getState())) {
//            statesMap.remove(state.getState());
//            fileIo.encode(stateDataFile, getList());
//        } else {
//            System.out.println("Throwing a State Not Found exception!!!!");
//            // Look up exception throwing and consider putting one here, too!
//
//        }
//    }
//
//    @Override
//    public List<String> getList() {
//        return new ArrayList(statesMap.keySet());
//    }
//
//    @Override
//    public int size() {
//        return statesMap.size();
//    }
//
//    @Override
//    public List<State> getListOfStates() {
//
//        List<State> states = getList().stream()
//                .map(name -> get(name))
//                .collect(Collectors.toList());
//
//        return states;
//    }

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

        if (state == null) {
            return null;
        }

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
