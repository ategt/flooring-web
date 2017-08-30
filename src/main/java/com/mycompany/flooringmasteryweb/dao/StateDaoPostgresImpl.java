/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.State;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
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
    private static final String SQL_UPDATE_STATE = "UPDATE states SET tax_rate = ? WHERE LOWER(state_abbreviation) = LOWER(?) RETURNING *;";
    private static final String SQL_DELETE_STATE = "DELETE FROM states WHERE LOWER(state_abbreviation) = LOWER(?) RETURNING *;";
    private static final String SQL_GET_STATE = "SELECT * FROM states WHERE LOWER(state_abbreviation) = LOWER(?) LIMIT 1;";
    private static final String SQL_GET_STATE_LIST = "SELECT * FROM states ORDER BY state_abbreviation ASC";
    private static final String SQL_COUNT_STATES = "SELECT COUNT(*) FROM states";
    private static final String SQL_GET_STATE_NAMES = "SELECT state_abbreviation FROM states ORDER BY state_abbreviation ASC";

    private static final String SQL_CREATE_STATES = "CREATE TABLE IF NOT EXISTS states(id serial PRIMARY KEY, state_name varchar(45), state_abbreviation varchar(2) NOT NULL UNIQUE CHECK(length(state_abbreviation) = 2), tax_rate decimal(6,4));";

    @Inject
    public StateDaoPostgresImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        initDatabase(jdbcTemplate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public State create(State state) {
        State returnedState = null;

        if (state == null)
            return null;
        
        if (Objects.nonNull(state.getState()) && state.getState().length() == 2) {

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

        try {
            return jdbcTemplate.queryForObject(SQL_GET_STATE, new StateMapper(), name);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public State update(State state) {

        if (state == null) {
            return null;
        }

        return jdbcTemplate.queryForObject(SQL_UPDATE_STATE,
                new StateMapper(),
                state.getStateTax(),
                state.getStateName());
    }

    @Override
    public State delete(State state) {
        if (state == null) {
            return null;
        }

        String name = state.getStateName();

        try {
            return jdbcTemplate.queryForObject(SQL_DELETE_STATE, new StateMapper(), name);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {

        }
        return null;
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
            state.setId(rs.getInt("id"));
            
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
}