/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Audit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author apprentice
 */
public class AuditDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_CREATE_AUDIT_TABLE = "CREATE TABLE IF NOT EXISTS audit (id SERIAL PRIMARY KEY, date timestamp, orderid integer, actionPerformed varchar(45), logDate timestamp, orderName varchar(145), orderTotal decimal(12,2))";
    private static final String SQL_INSERT_AUDIT = "INSERT INTO audit (date, orderid, actionPerformed, logDate, orderName, orderTotal) VALUES (?, ?, ?, ?, ?, ?) RETURNING id;";
    private static final String SQL_QUERY_AUDIT_BY_ID = "SELECT * FROM audit WHERE id = ?;";
    private static final String SQL_QUERY_AUDIT_ALL = "SELECT * FROM audit;";
    private static final String SQL_QUERY_AUDIT_COUNT = "SELECT COUNT(*) FROM audit;";
    
    @Inject
    public AuditDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute(SQL_CREATE_AUDIT_TABLE);
    }

    public Audit create(Audit audit) {

        if (audit == null)
            return null;
        
        Integer id = jdbcTemplate.queryForObject(SQL_INSERT_AUDIT,
                Integer.class,
                audit.getDate(),
                audit.getOrderid(),
                audit.getActionPerformed(),
                audit.getLogDate(),
                audit.getOrderName(),
                audit.getOrderTotal());

        audit.setId(id);
        return audit;
    }

    public Audit get(int id) {
        try {
            return jdbcTemplate.queryForObject(SQL_QUERY_AUDIT_BY_ID, new AuditMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<Audit> get() {
        try {
            return jdbcTemplate.query(SQL_QUERY_AUDIT_ALL, new AuditMapper());
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }
    
    public int getSize(){
        return jdbcTemplate.queryForObject(SQL_QUERY_AUDIT_COUNT, Integer.class);
    }

    private final class AuditMapper implements RowMapper<Audit> {

        @Override
        public Audit mapRow(ResultSet rs, int i) throws SQLException {

            Audit audit = new Audit();

            audit.setId(rs.getInt("id"));
            audit.setDate(rs.getTimestamp("date"));
            audit.setOrderid(rs.getInt("orderid"));
            audit.setActionPerformed(rs.getString("actionPerformed"));
            audit.setLogDate(rs.getTimestamp("logDate"));
            audit.setOrderName(rs.getString("orderName"));
            audit.setOrderTotal(rs.getDouble("orderTotal"));

            return audit;
        }
    }
}
