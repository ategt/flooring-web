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
import com.mycompany.flooringmasteryweb.dto.ResultProperties;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 *
 * @author ATeg
 */
public class AddressDaoPostgresImpl implements AddressDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_ADDRESS = "INSERT INTO addresses (first_name, last_name, company, street_number, street_name, city, state, zip) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING id";
    private static final String SQL_UPDATE_ADDRESS = "UPDATE addresses SET first_name=?, last_name=?, company=?, street_number=?, street_name=?, city=?, state=?, zip=? WHERE id=?";
    private static final String SQL_DELETE_ADDRESS = "DELETE FROM addresses WHERE id = ? RETURNING *";
    private static final String SQL_GET_ADDRESS = "SELECT * FROM addresses WHERE id = ?";
    private static final String SQL_GET_ADDRESS_BY_COMPANY = "SELECT * FROM addresses WHERE company = ?";
    private static final String SQL_GET_ADDRESS_LIST = "SELECT * FROM addresses";
    private static final String SQL_GET_ADDRESS_COUNT = "SELECT COUNT(*) FROM addresses";

    private static final String SQL_CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS addresses (id SERIAL PRIMARY KEY, first_name varchar(45), last_name varchar(45), company varchar(45), street_number varchar(45), street_name varchar(45), city varchar(45), state varchar(45), zip varchar(45))";

    private static final String SQL_BASE_SEARCH_QUERY = "SELECT * FROM addresses WHERE id IN ("
            + "SELECT id FROM firstQuery UNION SELECT id FROM secondQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) "
            + "UNION SELECT id FROM thirdQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery)"
            + "UNION SELECT id FROM fourthQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery) AND NOT EXISTS (SELECT id FROM thirdQuery)"
            + ")";

    private static final String SQL_SORT_ADDRESSES_BY_LAST_NAME_PARTIAL = " ORDER BY LOWER(last_name) ASC, LOWER(first_name) ASC, LOWER(company) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_FIRST_NAME_PARTIAL = " ORDER BY LOWER(first_name) ASC, LOWER(last_name) ASC, LOWER(company) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_COMPANY_PARTIAL = " ORDER BY LOWER(company) ASC, LOWER(last_name) ASC, LOWER(first_name) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_ID_PARTIAL = " ORDER BY id ASC";

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
    public Set<String> getCompletionGuesses(String input, int limit) {
        if (input == null) {
            return null;
        }

        Set<String> result = new HashSet();

        result.addAll(searchByFullName(input, null).stream().map(addressToFullName()).collect(Collectors.toSet()));

        result.addAll(searchByFirstName(input, null).stream().map(addressToFullName()).collect(Collectors.toSet()));

        result.addAll(searchByLastName(input, null).stream().map(addressToFullName()).collect(Collectors.toSet()));

        result.addAll(searchByCompany(input, null).stream().map(address -> address.getCompany()).collect(Collectors.toSet()));

        return result.stream().limit(limit).collect(Collectors.toSet());
    }

    private static Function<Address, String> addressToFullName() {
        return address -> new StringBuffer()
                .append(address.getFirstName())
                .append(" ")
                .append(address.getLastName()).toString();
    }

    @Override
    public Address getByCompany(String company) {
        if (company == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(SQL_GET_ADDRESS_BY_COMPANY, new AddressMapper(), company);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void update(Address address) {

        if (address == null) {
            return;
        }

        if (address.getId() != null && address.getId() > 0) {

            jdbcTemplate.update(SQL_UPDATE_ADDRESS,
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
        return list(new ResultProperties(AddressSortByEnum.SORT_BY_LAST_NAME, page, resultsPerPage));
    }

    @Override
    public List<Address> list(ResultProperties resultProperties) {
        return jdbcTemplate.query(sortAndPaginateQuery(SQL_GET_ADDRESS_LIST, resultProperties), new AddressMapper());
    }

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_GET_ADDRESS_COUNT, Integer.class);
    }

    private List<Address> search(String stringToSearchFor, String sqlQueryToUse, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(sqlQueryToUse, resultProperties), new AddressMapper(), stringToSearchFor);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_FIRST_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + "firstQuery AS (SELECT id FROM addresses WHERE first_name = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) LIKE (SELECT LOWER(CONCAT(n,'%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) LIKE (SELECT LOWER(CONCAT('%',n,'%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByFirstName(String firstName, ResultProperties resultProperties) {
        List<Address> result = search(firstName, SQL_SEARCH_ADDRESS_BY_FIRST_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE last_name = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(last_name) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(last_name) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(last_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByLastName(String lastName, ResultProperties resultProperties) {
        List<Address> result = search(lastName, SQL_SEARCH_ADDRESS_BY_LAST_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_CITY_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE city = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(city) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(city) LIKE (SELECT LOWER(CONCAT(n,'%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(city) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByCity(String city, ResultProperties resultProperties) {
        List<Address> result = search(city, SQL_SEARCH_ADDRESS_BY_CITY_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_COMPANY_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE company = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(company) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByCompany(String company, ResultProperties resultProperties) {
        List<Address> result = search(company, SQL_SEARCH_ADDRESS_BY_COMPANY_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STATE_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE state = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(state) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(state) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(state) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByState(String state, ResultProperties resultProperties) {
        List<Address> result = search(state, SQL_SEARCH_ADDRESS_BY_STATE_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_ZIP_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE zip = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(zip) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(zip) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(zip) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByZip(String zip, ResultProperties resultProperties) {
        List<Address> result = search(zip, SQL_SEARCH_ADDRESS_BY_ZIP_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_FULL_STREET_ADDRESS = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE CONCAT_WS(' ', street_number, street_name) = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreetAddress(String streetAddress, ResultProperties resultProperties) {
        List<Address> result = search(streetAddress, SQL_SEARCH_ADDRESS_BY_FULL_STREET_ADDRESS, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NUMBER = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE street_number = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreetNumber(String streetNumber, ResultProperties resultProperties) {
        List<Address> result = search(streetNumber, SQL_SEARCH_ADDRESS_BY_STREET_NUMBER, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQueryInput(n) AS (SELECT n FROM sourceQuery),"
            + " firstQueryInputLower(n) AS (SELECT LOWER(n) FROM firstQueryInput),"
            + " secondQueryInput(n) AS (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery),"
            + " thirdQueryInput(n) AS (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery),"
            + " firstQuery AS (SELECT id FROM addresses WHERE (SELECT n FROM firstQueryInput) IN (first_name||last_name||CONCAT_WS(' ', first_name, last_name))),"
            + " secondQuery AS (SELECT id FROM addresses WHERE (SELECT n FROM firstQueryInputLower) IN (first_name||last_name||CONCAT_WS(' ', first_name, last_name))),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) LIKE (SELECT n FROM secondQueryInput) OR LOWER(last_name) LIKE (SELECT n FROM secondQueryInput) OR LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT n FROM secondQueryInput)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) LIKE (SELECT n FROM thirdQueryInput) OR LOWER(last_name) LIKE (SELECT n FROM thirdQueryInput) OR LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT n FROM thirdQueryInput)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByName(String name, ResultProperties resultProperties) {
        List<Address> result = search(name, SQL_SEARCH_ADDRESS_BY_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQueryInput(n) AS (SELECT n FROM sourceQuery),"
            + " firstQueryInputLower(n) AS (SELECT LOWER(n) FROM firstQueryInput),"
            + " secondQueryInput(n) AS (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery),"
            + " thirdQueryInput(n) AS (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery),"
            + " firstQuery AS (SELECT id FROM addresses WHERE (SELECT n FROM firstQueryInput) = street_number OR (SELECT n FROM firstQueryInput) = street_name OR (SELECT n FROM firstQueryInput) = CONCAT_WS(' ', street_number, street_name)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE (SELECT n FROM firstQueryInputLower) = LOWER(street_number) OR (SELECT n FROM firstQueryInputLower) = LOWER(street_name) OR (SELECT n FROM firstQueryInputLower) = LOWER(CONCAT_WS(' ', street_number, street_name))),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) LIKE (SELECT n FROM secondQueryInput) OR LOWER(street_name) LIKE (SELECT n FROM secondQueryInput) OR LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE (SELECT n FROM secondQueryInput)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) LIKE (SELECT n FROM thirdQueryInput) OR LOWER(street_name) LIKE (SELECT n FROM thirdQueryInput) OR LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE (SELECT n FROM thirdQueryInput)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreet(String street, ResultProperties resultProperties) {
        List<Address> result = search(street, SQL_SEARCH_ADDRESS_BY_STREET, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY = "WITH firstQueryInput(n) AS (SELECT ?),"
            + "		firstQueryInputLower(n) AS (SELECT LOWER(n) FROM firstQueryInput),"
            + "		 secondQueryInput(n) AS (SELECT LOWER(CONCAT(n, '%')) FROM firstQueryInput),"
            + "		  thirdQueryInput(n) AS (SELECT LOWER(CONCAT('%', n, '%')) FROM firstQueryInput),"
            + "		 firstQuery AS ("
            + "		 SELECT id FROM addresses "
            + "		 WHERE first_name = (SELECT n FROM firstQueryInput)"
            + "		  OR (SELECT n FROM firstQueryInput) = last_name "
            + "		  OR (SELECT n FROM firstQueryInput) = CONCAT_WS(' ', first_name, last_name)"
            + "		   OR (SELECT n FROM firstQueryInput) = company),"
            + "			 secondQuery AS ("
            + "			 SELECT id FROM addresses "
            + "			 WHERE (SELECT n FROM firstQueryInputLower) = LOWER(first_name) "
            + "			 OR (SELECT n FROM firstQueryInputLower) = LOWER(last_name) "
            + "			 OR (SELECT n FROM firstQueryInputLower) = LOWER(CONCAT_WS(' ', first_name, last_name)) "
            + "			 OR (SELECT n FROM firstQueryInputLower) = LOWER(company)),"
            + "			  thirdQuery AS ("
            + "			  SELECT id FROM addresses "
            + "			  WHERE LOWER(first_name) LIKE (SELECT n FROM secondQueryInput)"
            + "			  OR LOWER(last_name) LIKE (SELECT n FROM secondQueryInput) "
            + "			  OR LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT n FROM secondQueryInput)"
            + "			  OR LOWER(company) LIKE (SELECT n FROM secondQueryInput)),"
            + "			    fourthQuery AS ("
            + "				 SELECT id FROM addresses "
            + "				 WHERE LOWER(first_name) LIKE (SELECT n FROM thirdQueryInput)"
            + "				 OR LOWER(last_name) LIKE (SELECT n FROM thirdQueryInput)"
            + "				 OR LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT n FROM thirdQueryInput)"
            + "				 OR LOWER(company) LIKE (SELECT n FROM thirdQueryInput))"
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByNameOrCompany(String input, ResultProperties resultProperties) {
        List<Address> result = search(input, SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_ALL = "WITH firstQueryInput(n) AS (SELECT ?),"
            + " firstQueryInputLower(n) AS (SELECT LOWER(n) FROM firstQueryInput),"
            + " firstQuery AS (SELECT id FROM addresses WHERE (SELECT n FROM firstQueryInput) IN (last_name, first_name, company, street_number, street_name, city, state, zip, CONCAT_WS(' ', first_name, last_name), CONCAT_WS(' ', street_number, street_name))),"
            + " secondQuery AS (SELECT id FROM addresses WHERE (SELECT n FROM firstQueryInputLower) IN (LOWER(last_name), LOWER(first_name), LOWER(company), LOWER(street_number), LOWER(street_name), LOWER(city), LOWER(state), LOWER(zip), LOWER(CONCAT_WS(' ', first_name, last_name)), LOWER(CONCAT_WS(' ', street_number, street_name)))),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE (LOWER(last_name)||LOWER(first_name)||LOWER(company)||LOWER(street_number)||LOWER(street_name)||LOWER(city)||LOWER(state)||LOWER(zip)||LOWER(CONCAT_WS(' ', first_name, last_name))||LOWER(CONCAT_WS(' ', street_number, street_name))) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM firstQueryInput)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE (LOWER(last_name)||LOWER(first_name)||LOWER(company)||LOWER(street_number)||LOWER(street_name)||LOWER(city)||LOWER(state)||LOWER(zip)||LOWER(CONCAT_WS(' ', first_name, last_name))||LOWER(CONCAT_WS(' ', street_number, street_name))) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM firstQueryInput)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByAll(String input, ResultProperties resultProperties) {
        List<Address> result = search(input, SQL_SEARCH_ADDRESS_BY_ALL, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_EVERYTHING_CLOSE = "SELECT * FROM addresses WHERE id IN(SELECT id FROM addresses WHERE (LOWER(last_name)||LOWER(first_name)||LOWER(company)||LOWER(street_number)||LOWER(street_name)||LOWER(city)||LOWER(state)||LOWER(zip)||LOWER(CONCAT_WS(' ', first_name, last_name))||LOWER(CONCAT_WS(' ', street_number, street_name))) LIKE LOWER('%'||?||'%'))";

    public List<Address> searchByAny(String input, ResultProperties resultProperties) {
        List<Address> result = search(input, SQL_SEARCH_ADDRESS_BY_EVERYTHING_CLOSE, resultProperties);

        return result;
    }

    @Override
    public int size(AddressSearchRequest addressSearchRequest) {
        return jdbcTemplate.queryForObject(SQL_GET_ADDRESS_COUNT, Integer.class);
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

    private static final String SQL_SEARCH_ADDRESS_BY_FULL_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE CONCAT_WS(' ', first_name, last_name) = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByFullName(String fullName, ResultProperties resultProperties) {
        List<Address> result = search(fullName, SQL_SEARCH_ADDRESS_BY_FULL_NAME, resultProperties);

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NAME = "WITH sourceQuery(n) AS (SELECT ?),"
            + " firstQuery AS (SELECT id FROM addresses WHERE street_name = (SELECT n FROM sourceQuery)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) = (SELECT LOWER(n) FROM sourceQuery)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM sourceQuery)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM sourceQuery)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreetName(String streetName, ResultProperties resultProperties) {
        List<Address> result = search(streetName, SQL_SEARCH_ADDRESS_BY_STREET_NAME, resultProperties);

        return result;
    }

    @Override
    public List<Address> getAddressesSortedByParameter(ResultProperties resultProperties) {
        return list(resultProperties);
    }

    @Override
    public List<Address> search(AddressSearchRequest searchRequest, ResultProperties resultProperties) {
        return search(searchRequest.getSearchText(), searchRequest.searchBy(), resultProperties);
    }

    public List<Address> search(String queryString,
            AddressSearchByOptionEnum searchOption,
            ResultProperties resultProperties) {

        List<Address> addresses;
        String sqlSearchQuery;

        if (null == searchOption) {
            addresses = list(resultProperties);
        } else {
            switch (searchOption) {
                case LAST_NAME:
                    addresses = searchByLastName(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_LAST_NAME;
                    break;
                case FIRST_NAME:
                    addresses = searchByFirstName(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_FIRST_NAME;
                    break;
                case COMPANY:
                    addresses = searchByCompany(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_COMPANY_NAME;
                    break;
                case CITY:
                    addresses = searchByCity(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_CITY_NAME;
                    break;
                case STATE:
                    addresses = searchByState(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STATE_NAME;
                    break;
                case STREET_NAME:
                    addresses = searchByStreetName(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET_NAME;
                    break;
                case STREET_NUMBER:
                    addresses = searchByStreetNumber(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET_NUMBER;
                    break;
                case STREET:
                    addresses = searchByStreet(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET;
                    break;
                case ZIP:
                    addresses = searchByZip(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ZIP_NAME;
                    break;
                case NAME:
                    addresses = searchByName(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_NAME;
                    break;
                case NAME_OR_COMPANY:
                    addresses = searchByNameOrCompany(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY;
                    break;
                case ALL:
                case DEFAULT:
                    addresses = searchByAll(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ALL;
                    break;
                default:
                    addresses = searchByAny(queryString, resultProperties);
                    sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_EVERYTHING_CLOSE;
                    break;
            }
            addresses = search(queryString, sqlSearchQuery, resultProperties);

        }
        return addresses;
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
            case SORT_BY_FIRST_NAME:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_FIRST_NAME_PARTIAL);
                break;
            case SORT_BY_COMPANY:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_COMPANY_PARTIAL);
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

    private String sortAndPaginateQuery(String query, ResultProperties resultProperties) {
        if (resultProperties == null) {
            return query;
        }
        return sortAndPaginateQuery(query, resultProperties.getSortByEnum(), resultProperties.getPageNumber(), resultProperties.getResultsPerPage());
    }
}
