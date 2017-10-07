/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import com.mycompany.flooringmasteryweb.dto.AddressSortByEnum;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ATeg
 */
public class AddressDaoPostgresImpl implements AddressDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_ADDRESS = "INSERT INTO addresses (first_name, last_name, company, street_number, street_name, city, state, zip) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING id";
    private static final String SQL_UPDATE_ADDRESS = "UPDATE addresses SET first_name=?, last_name=?, company=?, street_number=?, street_name=?, city=?, state=?, zip=? WHERE id=? RETURNING *";
    private static final String SQL_DELETE_ADDRESS = "DELETE FROM addresses WHERE id = ? RETURNING *";
    private static final String SQL_GET_ADDRESS = "SELECT *, 1 AS rank FROM addresses WHERE id = ?";
    private static final String SQL_GET_ADDRESS_BY_COMPANY = "SELECT *, 1 AS rank FROM addresses WHERE company = ?";
    private static final String SQL_GET_ADDRESS_LIST = "SELECT *, 1 AS rank FROM addresses";
    private static final String SQL_GET_ADDRESS_COUNT = "SELECT COUNT(*) FROM addresses";

    private static final String SQL_CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS addresses (id SERIAL PRIMARY KEY, first_name varchar(45), last_name varchar(45), company varchar(45), street_number varchar(45), street_name varchar(45), city varchar(45), state varchar(45), zip varchar(45))";

    private static final String SQL_SORT_ADDRESSES_BY_LAST_NAME_PARTIAL = " ORDER BY rank ASC, LOWER(last_name) ASC, LOWER(first_name) ASC, LOWER(company) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_LAST_NAME_INVERSE_PARTIAL = " ORDER BY rank ASC, LOWER(last_name) DESC, LOWER(first_name) DESC, LOWER(company) DESC, id DESC";
    private static final String SQL_SORT_ADDRESSES_BY_FIRST_NAME_PARTIAL = " ORDER BY rank ASC, LOWER(first_name) ASC, LOWER(last_name) ASC, LOWER(company) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_FIRST_NAME_INVERSE_PARTIAL = " ORDER BY rank ASC, LOWER(first_name) DESC, LOWER(last_name) DESC, LOWER(company) DESC, id DESC";
    private static final String SQL_SORT_ADDRESSES_BY_COMPANY_PARTIAL = " ORDER BY rank ASC, LOWER(company) ASC, LOWER(last_name) ASC, LOWER(first_name) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_COMPANY_INVERSE_PARTIAL = " ORDER BY rank ASC, LOWER(company) DESC, LOWER(last_name) DESC, LOWER(first_name) DESC, id DESC";
    private static final String SQL_SORT_ADDRESSES_BY_ID_PARTIAL = " ORDER BY rank ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_ID_INVERSE_PARTIAL = " ORDER BY rank ASC, id DESC";

    private static final String SQL_SEARCH_ADDRESS_BY_FIRST_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	first_name = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(first_name) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(first_name) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(first_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	last_name = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(last_name) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(last_name) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(last_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' ) LIKE (SELECT CONCAT('% ', n, ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' ) LIKE (SELECT LOWER(CONCAT('% ', n, ' %')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name ) LIKE (SELECT LOWER(CONCAT('% ', n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name ) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_COMPANY = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	company = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(company) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(company) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(company) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' ') LIKE (SELECT CONCAT('% ', n, ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' ') LIKE (SELECT LOWER(CONCAT('% ', n, ' %')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' || company) LIKE (SELECT LOWER(CONCAT('% ', n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' || company) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_CITY = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	city = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(city) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(city) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(city) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STATE = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	state = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(state) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(state) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(state) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_ZIP = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	zip = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(zip) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(zip) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(zip) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NUMBER = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	street_number = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(street_number) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(street_number) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(street_number) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	street_name = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(street_name) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(street_name) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(street_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STREET = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	' ' || street_number || ' ' || street_name LIKE (SELECT CONCAT('% ', n, ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || street_number || ' ' || street_name) LIKE (SELECT LOWER(CONCAT('% ', n, ' %')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || street_number || ' ' || street_name) LIKE (SELECT LOWER(CONCAT('% ', n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || street_number || ' ' || street_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_ALL = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "     SELECT *, 1 AS rank FROM addresses WHERE "
            + "      	' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip LIKE (SELECT CONCAT('% ', n, ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, 2 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip) LIKE (SELECT LOWER(CONCAT('% ', n, ' %')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 3 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip) LIKE (SELECT LOWER(CONCAT('% ', n, '%')) FROM inputQuery)"
            + "     UNION ALL SELECT *, 4 AS rank FROM addresses WHERE "
            + "      	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)"
            + "      UNION ALL SELECT *, 5 AS rank FROM addresses WHERE "
            + "		LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE "
            + "             ALL( "
            + "                 ARRAY("
            + "                     SELECT CONCAT('%', input_column, '%') FROM ( "
            + "                         SELECT unnest(string_to_array(n, ' ')) AS input_column FROM inputQuery "
            + "                     ) AS augmented_input_table "
            + "                 ) "
            + "             ) "
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_ANY = "WITH inputQuery(n) AS (SELECT ?),"
            + "	mainQuery AS ("
            + "      SELECT *, 1 AS rank FROM addresses WHERE "
            + "       	' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ' LIKE (SELECT CONCAT('% ', n, ' %') FROM inputQuery) "
            + "      UNION ALL SELECT *, 2 AS rank FROM addresses WHERE  "
            + "       	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE (SELECT LOWER(CONCAT('% ', n, ' %')) FROM inputQuery) "
            + "      UNION ALL SELECT *, 3 AS rank FROM addresses WHERE  "
            + "       	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE (SELECT LOWER(CONCAT('% ', n, '%')) FROM inputQuery) "
            + "      UNION ALL SELECT *, 4 AS rank FROM addresses WHERE  "
            + "       	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery) "
            + "      UNION ALL SELECT *, 5 AS rank FROM addresses WHERE "
            + "		LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE "
            + "             ALL( "
            + "                 ARRAY("
            + "                     SELECT CONCAT('%', input_column, '%') FROM ( "
            + "                         SELECT unnest(string_to_array(n, ' ')) AS input_column FROM inputQuery "
            + "                     ) AS augmented_input_table "
            + "                 ) "
            + "             ) "
            + "      UNION ALL SELECT *, 6 AS rank FROM addresses WHERE "
            + "		LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip) LIKE "
            + "             ANY( "
            + "                 ARRAY("
            + "                     SELECT CONCAT('%', input_column, '%') FROM ( "
            + "                         SELECT unnest(string_to_array(n, ' ')) AS input_column FROM inputQuery "
            + "                     ) AS augmented_input_table "
            + "                 ) "
            + "             ) "
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + "	JOIN ("
            + "		SELECT id, MIN(rank) min_rank"
            + "		FROM mainQuery"
            + "		GROUP BY id"
            + "	) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_ADDRESS_NAME_COMPLETION_QUERY = "WITH inputQuery(n) AS (SELECT ?)" +
            " nameOrCompany AS (" +
            " SELECT CONCAT(' ' || first_name || ' ' || last_name || ' full') col FROM addresses" +
            " UNION SELECT company || ' comp' FROM addresses" +
            " ) " +
            " SELECT col, 1 rank FROM nameOrCompany WHERE col LIKE (SELECT CONCAT('% ', n, ' %') FROM inputQuery)" +
            " UNION ALL SELECT col, 2 rank FROM nameOrCompany WHERE col LIKE (SELECT LOWER(CONCAT('% ', n, ' %')) FROM inputQuery)" +
            " UNION ALL SELECT col, 3 rank FROM nameOrCompany WHERE col LIKE (SELECT LOWER(CONCAT('% ', n, '%')) FROM inputQuery)" +
            " UNION ALL SELECT col, 4 rank FROM nameOrCompany WHERE col LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)" +
            " UNION ALL SELECT col, 5 rank FROM nameOrCompany WHERE col LIKE " +
            "             ALL(" +
            "                 ARRAY(" +
            "                     SELECT CONCAT('%', input_column, '%') FROM ( " +
            "                         SELECT unnest(string_to_array(n, ' ')) AS input_column FROM inputQuery " +
            "                     ) AS augmented_input_table " +
            "                 ) " +
            "             ) " +
            " ORDER BY rank ASC, col ASC" +
            " LIMIT ?";

    @Inject
    public AddressDaoPostgresImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute(SQL_CREATE_ADDRESS_TABLE);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Address create(Address address) {

        if (address == null) {
            return null;
        }

        Integer id = jdbcTemplate.queryForObject(SQL_INSERT_ADDRESS,
                Integer.class,
                address.getFirstName(),
                address.getLastName(),
                address.getCompany(),
                address.getStreetNumber(),
                address.getStreetName(),
                address.getCity(),
                address.getState(),
                address.getZip());

        address.setId(id);

        return address;
    }

    @Override
    public Address get(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(SQL_GET_ADDRESS, new AddressMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Address get(String input) {
        return getBestGuess(input);
    }

    public Address getBestGuess(String input) {
        Set<Address> result = getGuesses(input);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.iterator().next();
        }
    }

    public Set<Address> getGuesses(String input) {
        if (input == null) {
            return null;
        }

        List<Address> resultList = searchByAny(input, null);

        Set<Address> result = new HashSet();

        result.addAll(resultList);

        return result;
    }

    @Override
    public List<String> getCompletionGuesses(String input, int limit) {
        if (input == null) {
            return null;
        }

        try {
            String[] results = jdbcTemplate.queryForObject(SQL_ADDRESS_NAME_COMPLETION_QUERY, String[].class, input, limit);
            return Arrays.asList(results);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Address update(Address address) {

        if (address == null) {
            return null;
        }

        if (address.getId() != null && address.getId() > 0) {

            return jdbcTemplate.queryForObject(SQL_UPDATE_ADDRESS,
                    new AddressMapper(),
                    address.getFirstName(),
                    address.getLastName(),
                    address.getCompany(),
                    address.getStreetNumber(),
                    address.getStreetName(),
                    address.getCity(),
                    address.getState(),
                    address.getZip(),
                    address.getId());
        }
        return null;
    }

    @Override
    public Address delete(Integer id) {
        if (id == null) {
            return null;
        }

        try {
            return jdbcTemplate.queryForObject(SQL_DELETE_ADDRESS, new AddressMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<Address> list(Integer page, Integer resultsPerPage) {
        return list(new AddressResultSegment(AddressSortByEnum.SORT_BY_LAST_NAME, page, resultsPerPage));
    }

    @Override
    public List<Address> list(AddressResultSegment addressResultSegment) {
        return jdbcTemplate.query(sortAndPaginateQuery(SQL_GET_ADDRESS_LIST, addressResultSegment), new AddressMapper());
    }

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_GET_ADDRESS_COUNT, Integer.class);
    }

    private List<Address> search(String stringToSearchFor, String sqlQueryToUse, AddressResultSegment addressResultSegment) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(sqlQueryToUse, addressResultSegment), new AddressMapper(), stringToSearchFor);

        return result;
    }

    public List<Address> searchByAny(String input, AddressResultSegment addressResultSegment) {
        List<Address> result = search(input, SQL_SEARCH_ADDRESS_BY_ANY, addressResultSegment);

        return result;
    }

    @Override
    public int size(AddressSearchRequest addressSearchRequest) {
        if (addressSearchRequest == null) {
            return size();
        }

        String sqlQuery = determineSqlSearchQuery(addressSearchRequest.searchBy());

        final String SQL_ADDRESS_SEARCH_COUNT = new StringBuffer().append("SELECT COUNT(*) FROM (")
                .append(sqlQuery)
                .append(") AS countingQuery")
                .toString();

        return jdbcTemplate.queryForObject(SQL_ADDRESS_SEARCH_COUNT, Integer.class, addressSearchRequest.getSearchText());
    }

    private static final class AddressMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet rs, int i) throws SQLException {

            Address address = new Address();

            address.setId(rs.getInt("id"));

            address.setFirstName(rs.getString("first_name"));
            address.setLastName(rs.getString("last_name"));
            address.setCompany(rs.getString("company"));
            address.setStreetNumber(rs.getString("street_number"));
            address.setStreetName(rs.getString("street_name"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setZip(rs.getString("zip"));

            return address;
        }

    }

    @Override
    public List<Address> search(AddressSearchRequest searchRequest, AddressResultSegment addressResultSegment) {
        return search(searchRequest.getSearchText(), searchRequest.searchBy(), addressResultSegment);
    }

    public List<Address> search(String queryString,
                                AddressSearchByOptionEnum searchOption,
                                AddressResultSegment addressResultSegment) {

        List<Address> addresses;
        String sqlSearchQuery;

        if (null == searchOption) {
            addresses = list(addressResultSegment);
        } else {
            sqlSearchQuery = determineSqlSearchQuery(searchOption);
            addresses = search(queryString, sqlSearchQuery, addressResultSegment);

        }
        return addresses;
    }

    private String determineSqlSearchQuery(AddressSearchByOptionEnum searchOption) {
        String sqlSearchQuery;
        switch (searchOption) {
            case LAST_NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_LAST_NAME;
                break;
            case FIRST_NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_FIRST_NAME;
                break;
            case COMPANY:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_COMPANY;
                break;
            case CITY:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_CITY;
                break;
            case STATE:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STATE;
                break;
            case STREET_NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET_NAME;
                break;
            case STREET_NUMBER:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET_NUMBER;
                break;
            case STREET:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET;
                break;
            case ZIP:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ZIP;
                break;
            case NAME:
            case FULL_NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_NAME;
                break;
            case NAME_OR_COMPANY:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY;
                break;
            case ALL:
            case DEFAULT:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ALL;
                break;
            default:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ANY;
                break;
        }
        return sqlSearchQuery;
    }

    private String paginateQuery(String query, Integer page, Integer resultsPerPage) {
        if (page == null || resultsPerPage == null) {
            return query;
        }

        if (page < 0 || resultsPerPage < 0) {
            return query;
        }

        if (query.contains(";")) {
            throw new UnsupportedOperationException("Pagination Method can not handle semi-colons(;)");
        }

        long offset = (long) resultsPerPage * (long) page;

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("SELECT * FROM (");
        stringBuffer.append(query);
        stringBuffer.append(") AS innerQuery OFFSET ");
        stringBuffer.append(offset);
        stringBuffer.append(" LIMIT ");
        stringBuffer.append(resultsPerPage);

        return stringBuffer.toString();
    }

    private String sortQuery(String query, AddressSortByEnum sortByEnum) {
        if (sortByEnum == null) {
            return query;
        }

        if (query.contains(";")) {
            throw new UnsupportedOperationException("Sorting Method can not handle semi-colons(;)");
        }

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("SELECT * FROM (");
        stringBuffer.append(query);
        stringBuffer.append(") AS preSortedQuery");

        switch (sortByEnum) {
            case SORT_BY_LAST_NAME:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_LAST_NAME_PARTIAL);
                break;
            case SORT_BY_LAST_NAME_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_LAST_NAME_INVERSE_PARTIAL);
                break;
            case SORT_BY_FIRST_NAME:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_FIRST_NAME_PARTIAL);
                break;
            case SORT_BY_FIRST_NAME_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_FIRST_NAME_INVERSE_PARTIAL);
                break;
            case SORT_BY_COMPANY:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_COMPANY_PARTIAL);
                break;
            case SORT_BY_COMPANY_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_COMPANY_INVERSE_PARTIAL);
                break;
            case SORT_BY_ID_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_ID_INVERSE_PARTIAL);
                break;
            case SORT_BY_ID:
            default:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_ID_PARTIAL);
                break;
        }

        return stringBuffer.toString();
    }

    private String sortAndPaginateQuery(String query, AddressSortByEnum sortByEnum, Integer page, Integer resultsPerPage) {
        return paginateQuery(sortQuery(query, sortByEnum), page, resultsPerPage);
    }

    private String sortAndPaginateQuery(String query, AddressResultSegment addressResultSegment) {
        if (addressResultSegment == null) {
            return query;
        }
        return sortAndPaginateQuery(query, addressResultSegment.getSortByEnum(), addressResultSegment.getPageNumber(), addressResultSegment.getResultsPerPage());
    }
}
