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
        result.addAll(searchByStreetAddress(input));

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
    public Address getByCompany(String company    ) {
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

    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME = "SELECT * FROM addresses WHERE last_name = ?";
    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME_CASEINSENSITIVE = "SELECT * FROM addresses WHERE LOWER(last_name) = LOWER(?)";
    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME_PARTS = "SELECT * FROM addresses WHERE LOWER(last_name) LIKE LOWER(?)";

    @Override
    public List<Address> searchByLastName(String lastName) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_LAST_NAME, new AddressMapper(), lastName);

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_LAST_NAME_CASEINSENSITIVE, new AddressMapper(), lastName);
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_LAST_NAME_PARTS, new AddressMapper(), lastName + "%");
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_LAST_NAME_PARTS, new AddressMapper(), "%" + lastName + "%");
        }

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_FULL_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE CONCAT_WS(' ', first_name, last_name) = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?)) "
            + "SELECT * FROM addresses WHERE id IN ("
            + "SELECT id FROM firstQuery UNION SELECT id FROM secondQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) "
            + "UNION SELECT id FROM thirdQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery)"
            + "UNION SELECT id FROM fourthQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery) AND NOT EXISTS (SELECT id FROM thirdQuery)"
            + ")";

    @Override
    public List<Address> searchByFullName(String fullName) {
        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_FULL_NAME, new AddressMapper(), fullName, fullName, fullName + '%', '%' + fullName + '%');

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_FIRST_NAME = "SELECT * FROM addresses WHERE first_name = ?";
    private static final String SQL_SEARCH_ADDRESS_BY_FIRST_NAME_PARTIAL = "SELECT * FROM addresses WHERE LOWER(first_name) LIKE LOWER(?)";

    @Override
    public List<Address> searchByFirstName(String firstName) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_FIRST_NAME, new AddressMapper(), firstName);

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_FIRST_NAME_PARTIAL, new AddressMapper(), firstName);
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_FIRST_NAME_PARTIAL, new AddressMapper(), firstName + "%");
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_FIRST_NAME_PARTIAL, new AddressMapper(), "%" + firstName + "%");
        }

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NAME = "WITH firstQuery AS (SELECT id FROM addresses WHERE street_name = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE LOWER(?)) "
            + "SELECT * FROM addresses WHERE id IN ("
            + "SELECT id FROM firstQuery UNION SELECT id FROM secondQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) "
            + "UNION SELECT id FROM thirdQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery)"
            + "UNION SELECT id FROM fourthQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery) AND NOT EXISTS (SELECT id FROM thirdQuery)"
            + ")";

    public List<Address> searchByStreetName(String streetName) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STREET_NAME, new AddressMapper(), streetName, streetName, streetName + "%", "%" + streetName + "%");

        return result;
    }

    public List<Address> searchByStreetName(String streetName, ResultProperties resultProperties) {

        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_STREET_NAME, resultProperties), new AddressMapper(), streetName, streetName, streetName + "%", "%" + streetName + "%");

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_FULL_STREET_ADDRESS = "WITH firstQuery AS (SELECT id FROM addresses WHERE CONCAT_WS(' ', street_number, street_name) = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(CONCAT_WS(' ', street_number, street_name)) LIKE LOWER(?)) "
            + "SELECT * FROM addresses WHERE id IN ("
            + "SELECT id FROM firstQuery UNION SELECT id FROM secondQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) "
            + "UNION SELECT id FROM thirdQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery)"
            + "UNION SELECT id FROM fourthQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery) AND NOT EXISTS (SELECT id FROM thirdQuery)"
            + ")";

    public List<Address> searchByStreetAddress(String input) {
        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_FULL_STREET_ADDRESS, new AddressMapper(), input, input, input + '%', '%' + input + '%');

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NUMBER = "SELECT * FROM addresses WHERE street_number = ?";
    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NUMBER_PARTIAL = "SELECT * FROM addresses WHERE LOWER(street_number) LIKE LOWER(?)";

    public List<Address> searchByStreetNumber(String streetNumber) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STREET_NUMBER, new AddressMapper(), streetNumber);

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STREET_NUMBER_PARTIAL, new AddressMapper(), streetNumber);
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STREET_NUMBER_PARTIAL, new AddressMapper(), streetNumber + "%");
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STREET_NUMBER_PARTIAL, new AddressMapper(), "%" + streetNumber + "%");
        }

        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_CITY = "SELECT * FROM addresses WHERE city = ?";
    private static final String SQL_SEARCH_ADDRESS_BY_CITY_CASE_INSENSITIVE = "SELECT * FROM addresses WHERE LOWER(city) = LOWER(?)";
    private static final String SQL_SEARCH_ADDRESS_BY_CITY_WITH_LIKE = "SELECT * FROM addresses WHERE LOWER(city) LIKE LOWER(?)";

    @Override
    public List<Address> searchByCity(String city) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_CITY, new AddressMapper(), city);

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_CITY_CASE_INSENSITIVE, new AddressMapper(), city);
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_CITY_WITH_LIKE, new AddressMapper(), city + "%");
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_CITY_WITH_LIKE, new AddressMapper(), "%" + city + "%");
        }
        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_COMPANY = "SELECT * FROM addresses WHERE company = ?";
    private static final String SQL_SEARCH_ADDRESS_BY_COMPANY_WITH_LIKE = "SELECT * FROM addresses WHERE LOWER(company) LIKE LOWER(?)";

    @Override
    public List<Address> searchByCompany(String company) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_COMPANY, new AddressMapper(), company);

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_COMPANY_WITH_LIKE, new AddressMapper(), company);
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_COMPANY_WITH_LIKE, new AddressMapper(), company + "%");
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_COMPANY_WITH_LIKE, new AddressMapper(), "%" + company + "%");
        }
        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_STATE = "SELECT * FROM addresses WHERE state = ?";
    private static final String SQL_SEARCH_ADDRESS_BY_STATE_CASE_INSENSITIVE = "SELECT * FROM addresses WHERE LOWER(state) = LOWER(?)";
    private static final String SQL_SEARCH_ADDRESS_BY_STATE_WITH_LIKE = "SELECT * FROM addresses WHERE LOWER(state) LIKE LOWER(?)";

    @Override
    public List<Address> searchByState(String state) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STATE, new AddressMapper(), state);

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STATE_CASE_INSENSITIVE, new AddressMapper(), state);
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STATE_WITH_LIKE, new AddressMapper(), state + "%");
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_STATE_WITH_LIKE, new AddressMapper(), "%" + state + "%");
        }
        return result;
    }

    private static final String SQL_SEARCH_ADDRESS_BY_ZIPCODE = "SELECT * FROM addresses WHERE zip = ?";
    private static final String SQL_SEARCH_ADDRESS_BY_ZIPCODE_CASE_INSENSITIVE = "SELECT * FROM addresses WHERE LOWER(zip) = LOWER(?)";
    private static final String SQL_SEARCH_ADDRESS_BY_ZIPCODE_WITH_LIKE = "SELECT * FROM addresses WHERE LOWER(zip) LIKE LOWER(?)";

    @Override
    public List<Address> searchByZip(String zipcode) {

        List<Address> result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_ZIPCODE, new AddressMapper(), zipcode);

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_ZIPCODE_CASE_INSENSITIVE, new AddressMapper(), zipcode);
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_ZIPCODE_WITH_LIKE, new AddressMapper(), zipcode + "%");
        }

        if (result.isEmpty()) {
            result = jdbcTemplate.query(SQL_SEARCH_ADDRESS_BY_ZIPCODE_WITH_LIKE, new AddressMapper(), "%" + zipcode + "%");
        }

        return result;
    }
    
        private static final String SQL_SEARCH_ADDRESS_BASE_QUERY = "WITH firstQuery AS (SELECT id FROM addresses WHERE street_name = ?),"
            + " secondQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) = LOWER(?)),"
            + " thirdQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE LOWER(?)), "
            + " fourthQuery AS (SELECT id FROM addresses WHERE LOWER(street_name) LIKE LOWER(?)) "
            + "SELECT * FROM addresses WHERE id IN ("
            + "SELECT id FROM firstQuery UNION SELECT id FROM secondQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) "
            + "UNION SELECT id FROM thirdQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery)"
            + "UNION SELECT id FROM fourthQuery WHERE NOT EXISTS (SELECT id FROM firstQuery) AND NOT EXISTS (SELECT id FROM secondQuery) AND NOT EXISTS (SELECT id FROM thirdQuery)"
            + ")";

    public List<Address> searchBase(String streetName, ResultProperties resultProperties) {

        List<Address> result = jdbcTemplate.query(sortAndPaginateQuery(SQL_SEARCH_ADDRESS_BY_STREET_NAME, resultProperties), new AddressMapper(), streetName, streetName, streetName + "%", "%" + streetName + "%");

        return result;
    }


    @Override
    public List<Address> getAddressesSortedByParameter(ResultProperties resultProperties) {
        return list(resultProperties);
    }

    @Override
    public List<Address> search(AddressSearchRequest searchRequest, ResultProperties resultProperties){
        return search(searchRequest.getSearchText(), searchRequest.searchBy(), resultProperties);
    }
    
    public List<Address> search(String queryString,
            AddressSearchByOptionEnum searchOption,
            ResultProperties resultProperties) {

        List<Address> addresses = null;

        if (null == searchOption) {
            addresses = list(resultProperties);
        } else {
            switch (searchOption) {
                case LAST_NAME:
                    addresses = searchByLastName(queryString);
                    break;
                case FIRST_NAME:
                    addresses = searchByFirstName(queryString);
                    break;
                case COMPANY:
                    addresses = searchByCompany(queryString);
                    break;
                case CITY:
                    addresses = searchByCity(queryString);
                    break;
                case STATE:
                    addresses = searchByState(queryString);
                    break;
                case STREET_NAME:
                    addresses = searchByStreetName(queryString);
                    break;
                case STREET_NUMBER:
                    addresses = searchByStreetNumber(queryString);
                    break;
                case STREET:
                    Set<Address> tempStreetAddresses = new HashSet();
                    tempStreetAddresses.addAll(searchByStreetNumber(queryString));
                    tempStreetAddresses.addAll(searchByStreetName(queryString));
                    tempStreetAddresses.addAll(searchByStreetAddress(queryString));
                    addresses = new ArrayList(tempStreetAddresses);
                    break;
                case ZIP:
                    addresses = searchByZip(queryString);
                    break;
                case NAME:
                    Set<Address> tempNameAddresses = new HashSet();
                    tempNameAddresses.addAll(searchByFirstName(queryString));
                    tempNameAddresses.addAll(searchByLastName(queryString));
                    tempNameAddresses.addAll(searchByFullName(queryString));
                    addresses = new ArrayList(tempNameAddresses);
                    break;
                case NAME_OR_COMPANY:
                    Set<Address> tempNameCompanyAddresses = new HashSet();
                    tempNameCompanyAddresses.addAll(searchByFirstName(queryString));
                    tempNameCompanyAddresses.addAll(searchByLastName(queryString));
                    tempNameCompanyAddresses.addAll(searchByCompany(queryString));
                    addresses = new ArrayList(tempNameCompanyAddresses);
                    break;
                case ALL:
                case DEFAULT:
                    addresses = new ArrayList(getGuesses(queryString));
                    break;
                default:
                    addresses = list(null, null);
                    break;
            }

            if (resultProperties != null) {
                if (resultProperties.getPageNumber() != null && resultProperties.getResultsPerPage() != null) {
                    int startIndex = resultProperties.getPageNumber() * resultProperties.getResultsPerPage();
                    int endIndex = startIndex + resultProperties.getResultsPerPage();

                    if (addresses.size() > startIndex) {
                        if (addresses.size() < endIndex) {
                            endIndex = addresses.size();
                        }

                        addresses = addresses.subList(startIndex, endIndex);
                    } else {
                        addresses.clear();
                    }
                }
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
        return sortAndPaginateQuery(query, resultProperties.getSortByEnum(), resultProperties.getPageNumber(), resultProperties.getResultsPerPage());
    }
}
