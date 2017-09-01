/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.OrderResultSegment;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import com.mycompany.flooringmasteryweb.dto.OrderSortByEnum;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.dto.ResultSegement;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.ControllerUtilities;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author apprentice
 */
@Controller
@RequestMapping(value = "/orders")
public class OrdersController {

    private final ProductDao productDao;
    private final StateDao stateDao;
    private final OrderDao orderDao;

    private final ApplicationContext ctx;

    private final String RESULTS_COOKIE_NAME = "results_cookie";
    private final String SORT_COOKIE_NAME = "sort_cookie";

    @Inject
    public OrdersController(
            ProductDao productDao,
            StateDao stateDao,
            OrderDao orderDao
    ) {
        this.productDao = productDao;
        this.stateDao = stateDao;
        this.orderDao = orderDao;
        ctx = com.mycompany.flooringmasteryweb.aop.ApplicationContextProvider.getApplicationContext();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public List<Order> index(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request
    ) {

        ResultSegement resultSegment = processResultPropertiesWithAllAsDefault(
                sortBy,
                response,
                sortCookie,
                page,
                resultsPerPage,
                resultsPerPageCookie);

        return orderDao.list(resultSegment);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request,
            Map model) {

        ResultSegement resultSegment = processResultPropertiesWithContextDefaults(
                resultsPerPage,
                resultsPerPageCookie,
                page,
                sortBy,
                response,
                sortCookie);

        ControllerUtilities.generatePagingLinks(ctx, orderDao.size(), resultSegment, request, uriComponentsBuilder, model);

        loadOrdersToMap(model, resultSegment);

        ControllerUtilities.loadStateCommandsToMap(stateDao, model);
        loadProductCommandsToMap(model);

        putBlankOrder(model);
        
        return "order\\index";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Order showWithAjax(@PathVariable("id") Integer orderId, @RequestHeader(value = "Accept", required = true) String acceptHeader) {
        Order contact = orderDao.get(orderId);

        return contact;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showWithHtml(@PathVariable("id") Integer orderId, Map model, @RequestHeader(value = "Accept", required = true) String acceptHeader) {
        Order order = orderDao.get(orderId);

        if (Objects.isNull(order)) {
            return "order\\orderNotFound";
        }

        OrderCommand orderCommand = orderDao.resolveOrderCommand(order);

        model.put("orderCommand", orderCommand);
        model.put("order", order);

        return "order\\show";
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public OrderCommand updateWithAjax(@Valid @RequestBody OrderCommand orderCommand, BindingResult bindingResult) {

        validateInputs(orderCommand, bindingResult);

        if (bindingResult.hasErrors()) {
            return null;
        } else {
            Order order = orderDao.orderBuilder(orderCommand);

            Order orderTemp = order;

            if (order.getId() == 0) {
                orderTemp = orderDao.create(order);
            } else {
                orderDao.update(order);
            }

            return orderDao.resolveOrderCommand(orderTemp);
        }
    }

    private void validateInputs(OrderCommand orderCommand, BindingResult bindingResult) {
        validateState(orderCommand, bindingResult);
        validateProduct(orderCommand, bindingResult);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteWithAjax(@PathVariable("id") Integer orderId) {
        orderDao.delete(orderDao.get(orderId));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public OrderCommand createWithAjax(@Valid @RequestBody OrderCommand orderCommand, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return null;
        } else {
            Order order = orderDao.orderBuilder(orderCommand);

            Order orderTemp = order;

            if (order.getId() == 0) {
                orderTemp = orderDao.create(order);
            } else {
                orderDao.update(order);
            }

            return orderDao.resolveOrderCommand(orderTemp);
        }
    }

    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("orderCommand") OrderCommand orderCommand, BindingResult bindingResult, Map model, HttpSession session) {

        validateInputs(orderCommand, bindingResult);

        if (bindingResult.hasErrors()) {

            String[] fields = {"name", "state", "area", "date", "product"};

            java.util.List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                for (String testField : fields) {
                    if (fieldError.getField().equalsIgnoreCase(testField)) {
                        model.put(testField + "Error", true);
                    }
                }
            }

            int errorCount = bindingResult.getErrorCount();

            loadTheOrdersList(model);

            model.put("orderCommand", orderCommand);

            return "order\\index";
        } else {

            Order order = orderDao.orderBuilder(orderCommand);
            if (order.getId() == 0) {
                orderDao.create(order);
            } else {
                orderDao.update(order);
            }

            return "redirect:/";
        }
    }

    private void validateProduct(OrderCommand orderCommand, BindingResult bindingResult) {
        String productInput = orderCommand.getProduct();

        boolean productValid = productDao.validProductName(productInput);

        if (!productValid) {
            bindingResult.rejectValue("product", "error.user", "We Do not Carry That Product.");
        } else {

            String productGuess = productDao.bestGuessProductName(productInput);

            if (productDao.get(productGuess) == null) {
                bindingResult.rejectValue("product", "error.user", "We Do not Carry That Product.");
            } else {
                orderCommand.setProduct(productGuess);
            }
        }
    }

    private void validateState(OrderCommand orderCommand, BindingResult bindingResult) {
        String stateInput = orderCommand.getState();

        boolean stateValid = StateUtilities.validStateInput(stateInput);

        if (!stateValid) {
            bindingResult.rejectValue("state", "error.user", "That State Does Not Exist.");
        } else {

            String stateGuess = StateUtilities.bestGuessStateName(stateInput);
            String stateAbbreviation = StateUtilities.abbrFromState(stateGuess);

            if (stateDao.get(stateAbbreviation) == null) {
                bindingResult.rejectValue("state", "error.user", "The System Can Not Currently Handle Orders In That State. Please Call The Office To Place This Order.");
            } else {
                orderCommand.setState(stateAbbreviation);
            }

        }
    }

    private void loadTheOrdersList(Map model) {

        List<Order> orders = orderDao.getList();
        model.put("orders", orders);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer orderId, Map model) {

        Order order = orderDao.get(orderId);

        OrderCommand orderCommand = orderDao.resolveOrderCommand(order);
        model.put("orderCommand", orderCommand);
        loadTheOrdersList(model);

        return "order\\index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute OrderCommand basicOrder) {
        Order order = orderDao.orderBuilder(basicOrder);
        orderDao.update(order);

        return "redirect:/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer orderId) {

        orderDao.delete(orderDao.get(orderId));

        return "redirect:/";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editSubmit(@ModelAttribute OrderCommand basicOrder) {
        Order order = orderDao.orderBuilder(basicOrder);
        orderDao.update(order);
        return "redirect:/";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Map model) {

        loadTheOrdersList(model);

        return "order\\search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public java.lang.String searchSubmit(@RequestParam("searchBy") String searchBy, @RequestParam("searchText") String searchText, Map model) {
        List<Order> orders = orderDao.getList();
        Boolean error = false;
        Boolean dateError = false;

        if ("searchByOrderNumber".equalsIgnoreCase(searchBy)) {
            try {
                Integer inputInt = Integer.parseInt(searchText);
                orders = orderDao.searchByOrderNumber(inputInt);
            } catch (NumberFormatException numberFormatException) {
                error = true;
            }

        } else if ("searchByDate".equalsIgnoreCase(searchBy)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date orderDate = simpleDateFormat.parse(searchText);
                orders = orderDao.searchByDate(orderDate);
            } catch (ParseException ex) {
                error = true;
                dateError = true;
            }

        } else if ("searchByName".equalsIgnoreCase(searchBy)) {
            orders = orderDao.searchByName(searchText);
        } else if ("searchByProduct".equalsIgnoreCase(searchBy)) {
            String productGuess = productDao.bestGuessProductName(searchText);

            if (productGuess == null) {
                error = true;
            } else {
                Product product = productDao.get(productGuess);
                orders = orderDao.searchByProduct(product);
            }

        } else if ("searchByState".equalsIgnoreCase(searchBy)) {

            String stateGuess = StateUtilities.bestGuessStateName(searchText);

            if (stateGuess == null) {
                error = true;
            } else {
                State state = stateDao.get(stateGuess);
                orders = orderDao.searchByState(state);
            }
        }

        model.put("orders", orders);
        model.put("error", error);
        model.put("dateError", dateError);

        return "order\\search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Order> search(
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @ModelAttribute OrderSearchRequest addressSearchRequest,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            HttpServletResponse response
    ) {
        ResultSegement<OrderSortByEnum> resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);

        List<Order> orders = searchDatabase(addressSearchRequest, resultProperties);

        return orders;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Order show(@PathVariable("id") Integer addressId) {
        Order contact = orderDao.get(addressId);

        return contact;
    }

    @RequestMapping(value = "/size", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Integer size(HttpServletRequest request, HttpServletResponse response) {
        java.lang.String acceptHeader = request.getHeader("Accept");
        if (Objects.nonNull(acceptHeader) && acceptHeader.equalsIgnoreCase("application/json")) {
            return orderDao.size();
        } else {
            response.setStatus(404);
            return null;
        }
    }
    
    private void loadOrdersToMap(Map model, ResultSegement resultSegment) {
        List<Order> orders = orderDao.list(resultSegment);
        model.put("orders", orders);
    }
    
    private void loadStateCommandsToMap(Map model) {
        List<StateCommand> stateCommands = stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());

        model.put("stateCommands", stateCommands);
    }

    private List<StateCommand> stateList() {
        return stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());
    }


    private void loadProductCommandsToMap(Map model) {
        List<ProductCommand> productCommands = productDao.buildCommandProductList();
        model.put("productCommands", productCommands);
    }

    public void putBlankOrder(Map model) {
        OrderCommand orderCommand = new OrderCommand();
        orderCommand.setId(0);

        model.put("orderCommand", orderCommand);
    }

    private ResultSegement processResultPropertiesWithContextDefaults(
            Integer resultsPerPage,
            Integer resultsPerPageCookie,
            Integer page,
            String sortBy,
            HttpServletResponse response,
            String sortCookie
    ) throws BeansException {

        resultsPerPage = ControllerUtilities.loadDefaultResults(ctx, resultsPerPage, resultsPerPageCookie);
        page = ControllerUtilities.loadDefaultPageNumber(ctx, page);
        ResultSegement resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return resultProperties;
    }

    private ResultSegement processResultPropertiesWithAllAsDefault(
            String sortBy,
            HttpServletResponse response,
            String sortCookie,
            Integer page,
            Integer resultsPerPage,
            Integer resultsPerPageCookie) {

        resultsPerPage = ControllerUtilities.loadDefaultResults(ctx, resultsPerPage, resultsPerPageCookie);
        page = ControllerUtilities.loadDefaultPageNumber(ctx, page);
        ResultSegement resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return resultProperties;
    }

    private ResultSegement processResultProperties(String sortBy, HttpServletResponse response, String sortCookie, Integer page, Integer resultsPerPage) {
        OrderSortByEnum sortEnum = updateSortEnum(sortBy, response, sortCookie);

        ResultSegement resultProperties = new OrderResultSegment(sortEnum, page, resultsPerPage);

        ControllerUtilities.updateResultsCookie(resultProperties.getResultsPerPage(), RESULTS_COOKIE_NAME, response);
        return resultProperties;
    }

    private OrderSortByEnum updateSortEnum(String sortBy, HttpServletResponse response, String sortCookie) {
        sortBy = checkForReverseRequest(sortBy, sortCookie);

        if (sortBy != null) {
            response.addCookie(new Cookie(SORT_COOKIE_NAME, sortBy));
        } else if (sortCookie != null) {
            sortBy = sortCookie;
        }

        return OrderSortByEnum.parse(sortBy);
    }

    private static String checkForReverseRequest(String sortBy, String sortCookie) {
        if (Objects.nonNull(sortBy)) {
            OrderSortByEnum sortOld = OrderSortByEnum.parse(sortCookie);
            OrderSortByEnum sortNew = OrderSortByEnum.parse(sortBy);

            if (sortOld.equals(sortNew) || sortOld.reverse().equals(sortNew)) {
                sortBy = sortOld.reverse().toString();
            }
        }
        return sortBy;
    }
    
    private void loadOrder(Integer contactId, Map model) {
        Order address = orderDao.get(contactId);
        model.put("address", address);
    }

    private List<Order> searchDatabase(OrderSearchRequest searchRequest, ResultSegement<OrderSortByEnum> resultProperties) {
        return orderDao.search(searchRequest,
                resultProperties);
    }
}