/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.Timing;
import com.mycompany.flooringmasteryweb.utilities.OrderDaoFileIOImplementation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author apprentice
 */
public class TimingDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_TIMING = "INSERT INTO timing (startTime, stopTime, differenceTime, invokingClassName, invokingMethodName, modifiers) VALUES ( ?, ?, ?, ?, ?, ? ) RETURNING id;";
    private static final String SQL_DELETE_TIMING = "DELETE FROM timing WHERE id =?";
    private static final String SQL_GET_TIMING = "SELECT * FROM timing WHERE id =?";
    private static final String SQL_GET_TIMING_LIST = "SELECT * FROM timing;";
    private static final String SQL_GET_TIMING_RECENT = "SELECT * FROM timing WHERE id = (SELECT MAX(id) FROM timing);";
    private static final String SQL_GET_TIMING_COUNT = "SELECT COUNT(*) FROM timing;";
    
    private static final String SQL_CREATE_TIMING_TABLE = "CREATE TABLE IF NOT EXISTS timing (id SERIAL PRIMARY KEY, startTime bigint, stopTime bigint, differenceTime bigint, invokingClassName varchar, invokingMethodName varchar, modifiers smallint);";

    @Inject
    public TimingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        jdbcTemplate.execute(SQL_CREATE_TIMING_TABLE);
    }

    public Timing create(Timing timing) {

        if (timing == null) {
            return null;
        }

        try {

            Integer id = jdbcTemplate.queryForObject(SQL_INSERT_TIMING,
                    Integer.class,
                    timing.getStartTime(),
                    timing.getStopTime(),
                    timing.getDifferenceTime(),
                    timing.getInvokingClassName(),
                    timing.getInvokingMethodName(),
                    timing.getModifiers());

            timing.setId(id);

            return timing;

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return null;
        }
    }

    public List<Timing> getAll() {
        try {
            return jdbcTemplate.query(SQL_GET_TIMING_LIST, new TimingMapper());
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Timing getLast() {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_TIMING_RECENT, new TimingMapper());
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Timing get(Integer id) {

        if (id == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(SQL_GET_TIMING, new TimingMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void delete(Timing timing) {
        if (timing == null) {
            return;
        }

        int id = timing.getId();

        delete(id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TIMING, id);
    }

    public int size(){
        return jdbcTemplate.queryForObject(SQL_GET_TIMING_COUNT, Integer.class);
    }
    
    private static class TimingMapper implements RowMapper<Timing> {

        public TimingMapper() {
        }

        @Override
        public Timing mapRow(ResultSet rs, int i) throws SQLException {
            Timing timing = new Timing();
            
            timing.setId(rs.getInt("id"));
            timing.setStartTime(rs.getLong("startTime"));
            timing.setStopTime(rs.getLong("stopTime"));
            timing.setDifferenceTime(rs.getLong("differenceTime"));
            timing.setInvokingClassName(rs.getString("invokingClassName"));
            timing.setInvokingMethodName(rs.getString("invokingMethodName"));
            timing.setModifiers(rs.getInt("modifiers"));
     
            return timing;
        }
    }
}
