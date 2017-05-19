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
public class StateDaoPostgresImpl implements StateDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_STATE = "INSERT INTO states ( state_name, state_abbreviation, tax_rate ) VALUES ( ?, ?, ? ) RETURNING id";
    private static final String SQL_UPDATE_STATE = "UPDATE states SET tax_rate=? WHERE state_abbreviation=?";
    private static final String SQL_DELETE_STATE = "DELETE FROM states WHERE state_abbreviation = ?";
    private static final String SQL_GET_STATE = "SELECT * FROM states WHERE state_abbreviation = ? LIMIT 1";
    private static final String SQL_GET_STATE_LIST = "SELECT * FROM states";
    private static final String SQL_COUNT_STATES = "SELECT COUNT(*) FROM states";
    private static final String SQL_GET_STATE_NAMES = "SELECT state_abbreviation FROM states";

    private static final String SQL_CREATE_STATES = "CREATE TABLE IF NOT EXISTS states(id serial PRIMARY KEY, state_name varchar(45), state_abbreviation varchar(2) NOT NULL UNIQUE, tax_rate decimal(6,4));";

    @Inject
    public StateDaoPostgresImpl(JdbcTemplate jdbcTemplate) {
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

            try {
                Integer id = jdbcTemplate.queryForObject(SQL_INSERT_STATE, Integer.class, null, state.getStateName(), state.getStateTax());

                state.setId(id);
                return state;

            } catch (org.springframework.dao.DuplicateKeyException ex) {
                return null;
            }
        } else {
            //TODO: Look up how to throw exceptions and consider that here.
        }
        return returnedState;
    }

    public State get(String name) {
        if (name == null) {
            return null;
        }

        name = name.toUpperCase();

        try {
            return jdbcTemplate.queryForObject(SQL_GET_STATE, new StateMapper(), name);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void update(State state) {

        if (state == null) {
            return;
        }

        jdbcTemplate.update(SQL_UPDATE_STATE,
                state.getStateTax(),
                state.getStateName());
    }

    @Override
    public void delete(State state) {
        if (state == null) {
            return;
        }

        String name = state.getStateName();

        try {
            jdbcTemplate.update(SQL_DELETE_STATE, name);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {

        }
    }

    @Override
    public List<String> getList() {
        return jdbcTemplate.query(SQL_GET_STATE_NAMES, new StateNameMapper());
    }

    private void initDatabase(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(SQL_CREATE_STATES);
    }

    private final class StateMapper implements RowMapper<State> {

        @Override
        public State mapRow(ResultSet rs, int i) throws SQLException {
            State state = new State();
            state.setStateName(rs.getString("state_abbreviation"));

            try {
                String taxString = rs.getString("tax_rate");

                double tax = Double.parseDouble(taxString);
                state.setStateTax(tax);

            } catch (NullPointerException | NumberFormatException ex) {
                state.setStateTax(0.0d);
            }

            return state;
        }
    }

    private final class StateNameMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int i) throws SQLException {
            String stateName = rs.getString("state_abbreviation");
            return stateName;
        }
    }

    @Override
    public List<State> getListOfStates() {
        return jdbcTemplate.query(SQL_GET_STATE_LIST, new StateMapper());
    }

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_COUNT_STATES, Integer.class);
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