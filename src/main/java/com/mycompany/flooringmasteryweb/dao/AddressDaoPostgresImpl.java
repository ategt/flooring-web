/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Address;
import com.mycompany.flooringmasteryweb.dto.AddressResultSegment;
import com.mycompany.flooringmasteryweb.dto.AddressSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.AddressSearchRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private static final String SQL_INSERT_ADDRESS = "INSERT INTO addresses (first_name, last_name, company, street_number, street_name, city, state, zip) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING id;";
    private static final String SQL_UPDATE_ADDRESS = "UPDATE addresses SET first_name=?, last_name=?, company=?, street_number=?, street_name=?, city=?, state=?, zip=? WHERE id=?";
    private static final String SQL_DELETE_ADDRESS = "DELETE FROM addresses WHERE id = ? RETURNING *;";
    private static final String SQL_GET_ADDRESS = "SELECT * FROM addresses WHERE id = ?";
    private static final String SQL_GET_ADDRESS_COUNT = "SELECT COUNT(*) FROM addresses;";

    private static final String SQL_CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS addresses (id SERIAL PRIMARY KEY, first_name varchar(45), last_name varchar(45), company varchar(45), street_number varchar(45), street_name varchar(45), city varchar(45), state varchar(45), zip varchar(45))";

    private static final String SQL_SEARCH_ADDRESS_BASE_QUERY = "SELECT * FROM addresses WHERE id IN ("
            + "SELECT id FROM firstQuery UNION SELECT id FROM secondQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) "
            + "UNION SELECT id FROM thirdQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery)"
            + "UNION SELECT id FROM fourthQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery) AND NOT EXISTS (SELECT id FROM thirdQuery)"
            + ")";

    private static final String SQL_SEARCH_ADDRESS_BY_FIRST_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE first_name = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE first_name = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE first_name LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE first_name LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE last_name = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE last_name = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE last_name LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE last_name LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_FULL_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE CONCAT_WS(' ', first_name, last_name) = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       (CONCAT_WS(' ', first_name, last_name) || first_name || last_name) = (SELECT n FROM inputQuery)), "
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', first_name, last_name)||last_name||first_name) = (SELECT LOWER(n) FROM inputQuery)), "
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', first_name, last_name)||last_name||first_name) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)), "
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', first_name, last_name)||last_name||first_name) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_COMPANY = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE company = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE company = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE company LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE company LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       (CONCAT_WS(' ', first_name, last_name) || first_name || last_name || company) = (SELECT n FROM inputQuery)), "
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', first_name, last_name)||last_name||first_name || company) = (SELECT LOWER(n) FROM inputQuery)), "
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', first_name, last_name)||last_name||first_name || company) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)), "
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', first_name, last_name)||last_name||first_name || company) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_CITY = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE city = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE city = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE city LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE city LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_STATE = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE state = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE state = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE state LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE state LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_ZIP = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE zip = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE zip = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE zip LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE zip LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NUMBER = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE street_number = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE street_number = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE street_number LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE street_number LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE street_name = (SELECT n FROM inputQuery)),"
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE street_name = (SELECT LOWER(n) FROM inputQuery)),"
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE street_name LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)),"
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE street_name LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_STREET = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       (CONCAT_WS(' ', street_number, street_name) || street_number || street_name ) = (SELECT n FROM inputQuery)), "
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', street_number, street_name) || street_number || street_name ) = (SELECT LOWER(n) FROM inputQuery)), "
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', street_number, street_name) || street_number || street_name ) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)), "
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER(CONCAT_WS(' ', street_number, street_name) || street_number || street_name ) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_ALL = "WITH inputQuery(n) AS (SELECT ?),"
            + " firstQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       ((CONCAT_WS(' ', first_name, last_name) || CONCAT_WS(' ', street_number, street_name) ||first_name||last_name||company|| city || state || zip || street_number || street_name ) = (SELECT n FROM inputQuery)), "
            + " secondQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER((CONCAT_WS(' ', first_name, last_name) || CONCAT_WS(' ', street_number, street_name) ||first_name||last_name||company|| city || state || zip || street_number || street_name ) = (SELECT LOWER(n) FROM inputQuery)), "
            + " thirdQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER((CONCAT_WS(' ', first_name, last_name) || CONCAT_WS(' ', street_number, street_name) ||first_name||last_name||company|| city || state || zip || street_number || street_name ) LIKE (SELECT LOWER(CONCAT(n, '%')) FROM inputQuery)), "
            + " fourthQuery(id) AS (SELECT id FROM addresses WHERE "
            + "       LOWER((CONCAT_WS(' ', first_name, last_name) || CONCAT_WS(' ', street_number, street_name) ||first_name||last_name||company|| city || state || zip || street_number || street_name ) LIKE (SELECT LOWER(CONCAT('%', n, '%')) FROM inputQuery)) "
            + SQL_SEARCH_ADDRESS_BASE_QUERY;

    private static final String SQL_SEARCH_ADDRESS_BY_ANY = "SELECT DISTINCT * FROM addresses WHERE"
            + " (LOWER(CONCAT_WS(' ', first_name, last_name))||CONCAT_WS(' ', street_number, street_name)||"
            + "first_name||last_name||company||city||state||zip||street_number||street_name)"
            + " LIKE (SELECT LOWER(CONCAT('%', ?, '%')))";

    @Inject
    public AddressDaoPostgresImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute(SQL_CREATE_ADDRESS_TABLE);
    }

    @Transactional(propagation = Propagation.REQUIRED)
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

        Set<Address> result = new HashSet();

        result.addAll(searchByFirstName(input));
        result.addAll(searchByLastName(input));
        result.addAll(searchByFullName(input));
        result.addAll(searchByCity(input));
        result.addAll(searchByCompany(input));
        result.addAll(searchByState(input));
        result.addAll(searchByZip(input));
        result.addAll(searchByStreetName(input));
        result.addAll(searchByStreetNumber(input));

        return result;
    }

    @Override
    public Set<String> getCompletionGuesses(String input, int limit) {
        if (input == null) {
            return null;
        }

        Set<String> result = new HashSet();

        result.addAll(searchByFullName(input).stream().map(addressToFullName()).collect(Collectors.toSet()));

        result.addAll(searchByFirstName(input).stream().map(addressToFullName()).collect(Collectors.toSet()));

        result.addAll(searchByLastName(input).stream().map(addressToFullName()).collect(Collectors.toSet()));

        result.addAll(searchByCompany(input).stream().map(address -> address.getCompany()).collect(Collectors.toSet()));

        return result.stream().limit(limit).collect(Collectors.toSet());
    }

    private static Function<Address, String> addressToFullName() {
        return address -> new StringBuffer()
                .append(address.getFirstName())
                .append(" ")
                .append(address.getLastName()).toString();
    }

    @Override
    public Address getByCompany(String company
    ) {
        if (company == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(SQL_GET_ADDRESS_BY_COMPANY, new AddressMapper(), company);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

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

    @Override
    public List<Address> list() {
        return list(SORT_BY_LAST_NAME);
    }

    @Override
    public List<Address> list(Integer sortBy) {
        switch (sortBy) {
            case SORT_BY_COMPANY:
                return jdbcTemplate.query(SQL_GET_ADDRESS_LIST_SORT_BY_COMPANY, new AddressMapper());
            case SORT_BY_FIRST_NAME:
                return jdbcTemplate.query(SQL_GET_ADDRESS_LIST_SORT_BY_FIRST_NAME, new AddressMapper());
            case SORT_BY_ID:
                return jdbcTemplate.query(SQL_GET_ADDRESS_LIST_SORT_BY_ID, new AddressMapper());
            case SORT_BY_LAST_NAME:
            default:
                return jdbcTemplate.query(SQL_GET_ADDRESS_LIST_SORT_BY_LAST_NAME, new AddressMapper());
        }
    }

    @Override
    public int size() {
        return jdbcTemplate.queryForObject(SQL_GET_ADDRESS_COUNT, Integer.class);
    }

    private String getSearchQuery(AddressSearchByOptionEnum searchBy) {
        switch (searchBy) {
            case FIRST_NAME:
                return SQL_SEARCH_ADDRESS_BY_FIRST_NAME;
            case LAST_NAME:
                return SQL_SEARCH_ADDRESS_BY_LAST_NAME;
            case NAME:
                return SQL_SEARCH_ADDRESS_BY_NAME;
            case FULLNAME:
                return SQL_SEARCH_ADDRESS_BY_FULL_NAME;
            case STREET_NUMBER:
                return SQL_SEARCH_ADDRESS_BY_STREET_NUMBER;
            case STREET_NAME:
                return SQL_SEARCH_ADDRESS_BY_STREET_NAME;
            case STREET:
                return SQL_SEARCH_ADDRESS_BY_STREET;
            case CITY:
                return SQL_SEARCH_ADDRESS_BY_CITY;
            case STATE:
                return SQL_SEARCH_ADDRESS_BY_STATE;
            case ZIP:
                return SQL_SEARCH_ADDRESS_BY_ZIP;
            case COMPANY:
                return SQL_SEARCH_ADDRESS_BY_COMPANY;
            case NAME_OR_COMPANY:
                return SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY;
            case ALL:
                return SQL_SEARCH_ADDRESS_BY_ALL;
            case ANY:
            case DEFAULT:
            default:
                return SQL_SEARCH_ADDRESS_BY_ANY;
        }
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
    public List<Address> searchByLastName(String lastName) {
        return search(new AddressSearchRequest(lastName, AddressSearchByOptionEnum.LAST_NAME), null);
    }

    @Override
    public List<Address> searchByFullName(String fullName) {
        return search(new AddressSearchRequest(fullName, AddressSearchByOptionEnum.FULLNAME), null);
    }

    @Override
    public List<Address> searchByFirstName(String firstName) {
        return search(new AddressSearchRequest(firstName, AddressSearchByOptionEnum.FIRST_NAME), null);
    }

    public List<Address> searchByStreetName(String streetName) {
        return search(new AddressSearchRequest(streetName, AddressSearchByOptionEnum.STREET_NAME), null);
    }

    public List<Address> searchByStreetNumber(String streetNumber) {
        return search(new AddressSearchRequest(streetNumber, AddressSearchByOptionEnum.STREET_NUMBER), null);
    }

    @Override
    public List<Address> searchByCity(String city) {
        return search(new AddressSearchRequest(city, AddressSearchByOptionEnum.CITY), null);
    }

    @Override
    public List<Address> searchByCompany(String company) {
        return search(new AddressSearchRequest(company, AddressSearchByOptionEnum.COMPANY), null);
    }

    @Override
    public List<Address> searchByState(String state) {
        return search(new AddressSearchRequest(state, AddressSearchByOptionEnum.STATE), null);
    }

    @Override
    public List<Address> searchByZip(String zipcode) {
        return search(new AddressSearchRequest(zipcode, AddressSearchByOptionEnum.ZIP), null);
    }

    @Override
    public List<Address> getAddressesSortedByParameter(String sortBy) {
        List<Address> addresses;
        if (sortBy.equalsIgnoreCase("company")) {
            addresses = list(AddressDao.SORT_BY_COMPANY);
        } else if (sortBy.equalsIgnoreCase("id")) {
            addresses = list(AddressDao.SORT_BY_ID);
        } else if (sortBy.equalsIgnoreCase("first_name")) {
            addresses = list(AddressDao.SORT_BY_FIRST_NAME);
        } else if (sortBy.equalsIgnoreCase("last_name")) {
            addresses = list(AddressDao.SORT_BY_LAST_NAME);
        } else {
            addresses = list();
        }
        return addresses;
    }

    public List<Address> search(String queryString, AddressSearchByOptionEnum searchOption) {
        return search(new AddressSearchRequest(queryString, searchOption), null);
    }

    @Override
    public List<Address> search(AddressSearchRequest searchRequest, AddressResultSegment resultSegment) {
        if (searchRequest == null) {
            return new ArrayList<>();
        }

        String searchQuery = getSearchQuery(searchRequest.searchBy());
        String improvedQuery = sortAndPaginateQuery(searchQuery, resultSegment);

        List<Address> result = jdbcTemplate.query(improvedQuery, new AddressMapper(), searchRequest.getSearchText());

        return result;
    }

    private final String SORT_BY_COMPANY = "company ASC, first_name ASC, last_name ASC, id desc";
    private final String SORT_BY_FIRST_NAME = "first_name ASC, last_name ASC, company ASC, id desc";
    private final String SORT_BY_LAST_NAME = "last_name ASC, first_name ASC, company ASC, id desc";
    private final String SORT_BY_ID = "id desc";
    private final String SORT_BY_COMPANY_INVERSE = "company ASC, first_name ASC, last_name ASC, id desc";
    private final String SORT_BY_FIRST_NAME_INVERSE = "first_name ASC, last_name ASC, company ASC, id desc";
    private final String SORT_BY_LAST_NAME_INVERSE = "last_name ASC, first_name ASC, company ASC, id desc";
    private final String SORT_BY_ID_INVERSE = "id desc";

    private String sortQuery(final String query, final AddressResultSegment resultSegment) {
        String sortByString = null;

        switch (resultSegment.getSortBy()) {
            case COMPANY:
                sortByString = SORT_BY_COMPANY;
                break;
            case FIRST_NAME:
                sortByString = SORT_BY_FIRST_NAME;
                break;
            case LAST_NAME:
                sortByString = SORT_BY_LAST_NAME;
                break;
            case ID:
                sortByString = SORT_BY_ID;
                break;
            case COMPANY_INVERSE:
                sortByString = SORT_BY_COMPANY_INVERSE;
                break;
            case FIRST_NAME_INVERSE:
                sortByString = SORT_BY_FIRST_NAME_INVERSE;
                break;
            case LAST_NAME_INVERSE:
                sortByString = SORT_BY_LAST_NAME_INVERSE;
                break;
            case ID_INVERSE:
                sortByString = SORT_BY_ID_INVERSE;
                break;
            default:
                sortByString = SORT_BY_ID;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM (");
        stringBuilder.append(query);
        stringBuilder.append(") AS presortedQuery ORDER BY ");
        stringBuilder.append(sortByString);

        return stringBuilder.toString();
    }

    private String paginateQuery(final String query, final AddressResultSegment resultSegment) {
        if (resultSegment == null
                || resultSegment.getPage() == null
                || resultSegment.getPage() < 0
                || resultSegment.getResultsPerPage() == null
                || resultSegment.getResultsPerPage() < 0) {

            return query;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM (");
        stringBuilder.append(query);
        stringBuilder.append(") AS prepaginatedQuery LIMIT ");
        stringBuilder.append(resultSegment.getResultsPerPage());
        stringBuilder.append(" OFFSET ");
        stringBuilder.append(resultSegment.getPage() * resultSegment.getResultsPerPage());

        return stringBuilder.toString();
    }

    private String sortAndPaginateQuery(final String query, final AddressResultSegment resultSegment) {
        if (resultSegment == null) {
            return query;
        }

        return sortQuery(paginateQuery(query, resultSegment), resultSegment);
    }
}
