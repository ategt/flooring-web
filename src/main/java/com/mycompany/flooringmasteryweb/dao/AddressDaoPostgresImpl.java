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
import java.util.ArrayList;
import java.util.Collection;
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

    private static final String SQL_SEARCH_ADDRESS_BY_FIRST_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE first_name = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(first_name) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByFirstName(String firstName, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_FIRST_NAME, resultProperties), new AddressMapper(), firstName, firstName, firstName + "%", "%" + firstName + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE last_name = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(last_name) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(last_name) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(last_name) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByLastName(String lastName, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_LAST_NAME, resultProperties), new AddressMapper(), lastName, lastName, lastName + "%", "%" + lastName + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_CITY_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE city = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(city) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(city) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(city) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByCity(String city, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_CITY_NAME, resultProperties), new AddressMapper(), city, city, city + "%", "%" + city + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_COMPANY_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE company = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(company) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(company) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(company) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByCompany(String company, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_COMPANY_NAME, resultProperties), new AddressMapper(), company, company, company + "%", "%" + company + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STATE_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE state = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(state) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(state) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(state) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByState(String state, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_STATE_NAME, resultProperties), new AddressMapper(), state, state, state + "%", "%" + state + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_ZIP_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE zip = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(zip) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(zip) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(zip) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByZip(String zip, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_ZIP_NAME, resultProperties), new AddressMapper(), zip, zip, zip + "%", "%" + zip + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_FULL_STREET_ADDRESS = "WITH firstQuery AS (SELECT id FROM addresses WHERE CONCAT_WS(' ', street_number, street_name) = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreetAddress(String streetAddress, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_FULL_STREET_ADDRESS, resultProperties), new AddressMapper(), streetAddress, streetAddress, streetAddress + "%", "%" + streetAddress + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NUMBER = "WITH firstQuery AS (SELECT id FROM addresses WHERE street_number = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(street_number) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreetNumber(String streetNumber, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_STREET_NUMBER, resultProperties), new AddressMapper(), streetNumber, streetNumber, streetNumber + "%", "%" + streetNumber + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_NAME = "WITH firstQueryInput AS (SELECT ?),"
            + " firstQueryInputLower AS (SELECT LOWER(firstQueryInput)),"
            + " secondQueryInput AS (SELECT LOWER(?)),"
            + " thirdQueryInput AS (SELECT LOWER(?)),"
            + " firstQuery AS (SELECT id FROM addresses WHERE firstQueryInput = first_name OR firstQueryInput = last_name OR firstQueryInput = CONCAT_WS(' ', first_name, last_name)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE firstQueryInputLower = LOWER(first_name) OR firstQueryInputLower = LOWER(last_name) OR firstQueryInputLower = LOWER(CONCAT_WS(' ', first_name, last_name))),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE secondQueryInput LIKE LOWER(first_name) OR secondQueryInput LIKE LOWER(last_name) OR secondQueryInput LIKE LOWER(CONCAT_WS(' ', first_name, last_name))), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE thirdQueryInput LIKE LOWER(first_name) OR thirdQueryInput LIKE LOWER(last_name) OR thirdQueryInput LIKE LOWER(CONCAT_WS(' ', first_name, last_name))) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByName(String name, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_NAME, resultProperties), new AddressMapper(), name, name + "%", "%" + name + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET = "WITH firstQueryInput AS (SELECT ?),"
            + " firstQueryInputLower AS (SELECT LOWER(firstQueryInput)),"
            + " secondQueryInput AS (SELECT LOWER(?)),"
            + " thirdQueryInput AS (SELECT LOWER(?)),"
            + " firstQuery AS (SELECT id FROM addresses WHERE firstQueryInput = street_number OR firstQueryInput = street_name OR firstQueryInput = CONCAT_WS(' ', street_number, street_name)),"
            + " secondQuery AS (SELECT id FROM addresses WHERE firstQueryInputLower = LOWER(street_number) OR firstQueryInputLower = LOWER(street_name) OR firstQueryInputLower = LOWER(CONCAT_WS(' ', street_number, street_name))),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE secondQueryInput LIKE LOWER(street_number) OR secondQueryInput LIKE LOWER(street_name) OR secondQueryInput LIKE LOWER(CONCAT_WS(' ', street_number, street_name))), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE thirdQueryInput LIKE LOWER(street_number) OR thirdQueryInput LIKE LOWER(street_name) OR thirdQueryInput LIKE LOWER(CONCAT_WS(' ', street_number, street_name))) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreet(String street, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_STREET, resultProperties), new AddressMapper(), street, street + "%", "%" + street + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY = "WITH firstQueryInput AS (SELECT ?),"
            + " firstQueryInputLower AS (SELECT LOWER(firstQueryInput)),"
            + " secondQueryInput AS (SELECT LOWER(?)),"
            + " thirdQueryInput AS (SELECT LOWER(?)),"
            + " firstQuery AS (SELECT id FROM addresses WHERE firstQueryInput = first_name OR firstQueryInput = last_name OR firstQueryInput = CONCAT_WS(' ', first_name, last_name) OR firstQueryInput = company),"
            + " secondQuery AS (SELECT id FROM addresses WHERE firstQueryInputLower = LOWER(first_name) OR firstQueryInputLower = LOWER(last_name) OR firstQueryInputLower = LOWER(CONCAT_WS(' ', first_name, last_name)) OR firstQueryInputLower = LOWER(company)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE secondQueryInput LIKE LOWER(first_name) OR secondQueryInput LIKE LOWER(last_name) OR secondQueryInput LIKE LOWER(CONCAT_WS(' ', first_name, last_name)) OR secondQueryInput LIKE LOWER(company)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE thirdQueryInput LIKE LOWER(first_name) OR thirdQueryInput LIKE LOWER(last_name) OR thirdQueryInput LIKE LOWER(CONCAT_WS(' ', first_name, last_name)) OR thirdQueryInput LIKE LOWER(company)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByNameOrCompany(String input, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY, resultProperties), new AddressMapper(), input, input + "%", "%" + input + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_ALL = "WITH firstQueryInput AS (SELECT ?),"
            + " firstQueryInputLower AS (SELECT LOWER(firstQueryInput)),"
            + " firstQuery AS (SELECT id FROM addresses WHERE firstQueryInput IN (last_name, first_name, company, street_number, street_name, city, state, zip, CONCAT_WS(' ', first_name, last_name), CONCAT_WS(' ', street_number, street_name))),"
            + " secondQuery AS (SELECT id FROM addresses WHERE firstQueryInputLower IN (LOWER(last_name), LOWER(first_name), LOWER(company), LOWER(street_number), LOWER(street_name), LOWER(city), LOWER(state), LOWER(zip), LOWER(CONCAT_WS(' ', first_name, last_name)), LOWER(CONCAT_WS(' ', street_number, street_name)))),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE (LOWER(last_name)||LOWER(first_name)||LOWER(company)||LOWER(street_number)||LOWER(street_name)||LOWER(city)||LOWER(state)||LOWER(zip)||LOWER(CONCAT_WS(' ', first_name, last_name))||LOWER(CONCAT_WS(' ', street_number, street_name))) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE (LOWER(last_name)||LOWER(first_name)||LOWER(company)||LOWER(street_number)||LOWER(street_name)||LOWER(city)||LOWER(state)||LOWER(zip)||LOWER(CONCAT_WS(' ', first_name, last_name))||LOWER(CONCAT_WS(' ', street_number, street_name))) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByAll(String input, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_ALL, resultProperties), new AddressMapper(), input, input + "%", "%" + input + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_EVERYTHING_CLOSE = "SELECT * FROM addresses WHERE id IN(SELECT id FROM addresses WHERE (LOWER(last_name)||LOWER(first_name)||LOWER(company)||LOWER(street_number)||LOWER(street_name)||LOWER(city)||LOWER(state)||LOWER(zip)||LOWER(CONCAT_WS(' ', first_name, last_name))||LOWER(CONCAT_WS(' ', street_number, street_name))) LIKE LOWER(?))";

    public List<Address> searchByAny(String input, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_EVERYTHING_CLOSE, resultProperties), new AddressMapper(), "%" + input + "%");

        return result;
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

    private static final String SQL_SEARCH_ADDRESS_BY_FULL_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE CONCAT_WS(' ', first_name, last_name) = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByFullName(String fullName, ResultProperties resultProperties) {
        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_FULL_NAME, resultProperties), new AddressMapper(), fullName, fullName, fullName + '%', '%' + fullName + '%');

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE street_name = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE LOWER(?)) "
            + SQL_BASE_SEARCH_QUERY;

    @Override
    public List<Address> searchByStreetName(String streetName, ResultProperties resultProperties) {

        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_STREET_NAME, resultProperties), new AddressMapper(), streetName, streetName, streetName + "%", "%" + streetName + "%");

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

        if (null == searchOption) {
            addresses = list(resultProperties);
        } else {
            switch (searchOption) {
                case LAST_NAME:
                    addresses = searchByLastName(queryString, resultProperties);
                    break;
                case FIRST_NAME:
                    addresses = searchByFirstName(queryString, resultProperties);
                    break;
                case COMPANY:
                    addresses = searchByCompany(queryString, resultProperties);
                    break;
                case CITY:
                    addresses = searchByCity(queryString, resultProperties);
                    break;
                case STATE:
                    addresses = searchByState(queryString, resultProperties);
                    break;
                case STREET_NAME:
                    addresses = searchByStreetName(queryString, resultProperties);
                    break;
                case STREET_NUMBER:
                    addresses = searchByStreetNumber(queryString, resultProperties);
                    break;
                case STREET:
                    addresses = searchByStreet(queryString, resultProperties);
                    break;
                case ZIP:
                    addresses = searchByZip(queryString, resultProperties);
                    break;
                case NAME:
                    addresses = searchByName(queryString, resultProperties);
                    break;
                case NAME_OR_COMPANY:
                    addresses = searchByNameOrCompany(queryString, resultProperties);
                    break;
                case ALL:
                case DEFAULT:
                    addresses = searchByAll(queryString, resultProperties);
                    break;
                default:
                    addresses = searchByAny(queryString, resultProperties);
                    break;
            }

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
                stringBuffer.append(" ORDER BY last_name ASC, first_name ASC, company ASC, id ASC");
                break;
            case SORT_BY_FIRST_NAME:
                stringBuffer.append(" ORDER BY first_name ASC, last_name ASC, company ASC, id ASC");
                break;
            case SORT_BY_COMPANY:
                stringBuffer.append(" ORDER BY company ASC, last_name ASC, first_name ASC, id ASC");
                break;
            case SORT_BY_ID:
            default:
                stringBuffer.append(" ORDER BY id ASC");
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
