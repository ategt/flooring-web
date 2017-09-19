/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.*;
import com.mycompany.flooringmasteryweb.modelBinding.CustomModelBinder;
import com.mycompany.flooringmasteryweb.utilities.ControllerUtilities;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;
import com.mycompany.flooringmasteryweb.validation.RestValidationHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author apprentice
 */
@Controller
@RequestMapping(value = "/orders")
public class OrdersController implements ApplicationContextAware{

    private final ProductDao productDao;
    private final StateDao stateDao;
    private final OrderDao orderDao;

    private ApplicationContext applicationContext;

    @Inject
    public OrdersController(
            ProductDao productDao,
            StateDao stateDao,
            OrderDao orderDao,
            RestValidationHandler restValidationHandler
    ) {
        this.productDao = productDao;
        this.stateDao = stateDao;
        this.orderDao = orderDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public List<Order> index(
            @CustomModelBinder OrderResultSegment resultSegment,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request
    ) {

        return orderDao.list(resultSegment);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(
            @CustomModelBinder OrderResultSegment resultSegment,
            UriComponentsBuilder uriComponentsBuilder,
            HttpServletResponse response,
            HttpServletRequest request,
            Map model) {

        ControllerUtilities.generatePagingLinks(applicationContext, orderDao.size(), resultSegment, request, uriComponentsBuilder, model);

        loadOrdersToMap(model, resultSegment);

        ControllerUtilities.loadStateCommandsToMap(stateDao, model);
        loadProductCommandsToMap(model);

        putBlankOrder(model);

        return "order\\index";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Order show(@PathVariable("id") Integer orderId) {
        Order contact = orderDao.get(orderId);

        return contact;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(
            @PathVariable("id") Integer orderId,
            Map model
    ) {
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
    public Order update(@Valid @RequestBody OrderCommand orderCommand, BindingResult bindingResult)
            throws  MethodArgumentNotValidException {

        validateInputs(orderCommand, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        } else {
            Order order = orderDao.orderBuilder(orderCommand);

            if (Objects.isNull(order.getId()) || order.getId() == 0) {
                order = orderDao.create(order);
            } else {
                order = orderDao.update(order);
            }

            return order;
        }
    }

    private void validateInputs(OrderCommand orderCommand, BindingResult bindingResult) {
        validateState(orderCommand, bindingResult);
        validateProduct(orderCommand, bindingResult);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Order create(@Valid @RequestBody OrderCommand orderCommand, BindingResult bindingResult) throws MethodArgumentNotValidException {

        validateInputs(orderCommand, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        } else {
            Order order = orderDao.orderBuilder(orderCommand);

            if (Objects.nonNull(order))
                order = orderDao.create(order);

            return order;
        }
    }

    private void validateProduct(OrderCommand orderCommand, BindingResult bindingResult) {
        String productInput = orderCommand.getProduct();

        boolean productValid = productDao.validProductName(productInput);

        if (!productValid) {
            bindingResult.rejectValue("product", "product.invalid", "-We Do not Carry This Item.");
        } else {

            String productGuess = productDao.bestGuessProductName(productInput);

            if (productDao.get(productGuess) == null) {
                bindingResult.rejectValue("product", "product.notFound", "-We Do not Carry That Item.");
            } else {
                orderCommand.setProduct(productGuess);
            }
        }

    }

    private void validateState(OrderCommand orderCommand, BindingResult bindingResult) {
        String stateInput = orderCommand.getState();

        boolean stateValid = StateUtilities.validStateInput(stateInput);

        if (!stateValid) {
            bindingResult.rejectValue("state", "validation.orderCommand.state.invalid");
        } else {

            String stateGuess = StateUtilities.bestGuessStateName(stateInput);
            String stateAbbreviation = StateUtilities.abbrFromState(stateGuess);

            if (stateDao.get(stateAbbreviation) == null) {
                bindingResult.rejectValue("state", "state.doNotDoBusinessThere", "-The System Can Not Currently Handle Orders In That State. Please Call The Office To Place This Order.");
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

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    @ResponseBody
    public Order delete(@PathVariable("id") Integer orderId) {
        return orderDao.delete(orderId);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer orderId,
                         Map model) {

        orderDao.delete(orderId);

        return "redirect:/orders/";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Map model) {

        loadTheOrdersList(model);

        return "order\\search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(
            @CustomModelBinder OrderResultSegment resultSegment,
            @CustomModelBinder OrderSearchRequest addressSearchRequest,
            HttpServletResponse response,
            Map model) {

        List<Order> orders = searchDatabase(addressSearchRequest, resultSegment);

        model.put("orders", orders);

        return "order\\search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Order> search(
            @CustomModelBinder OrderResultSegment resultSegment,
            @CustomModelBinder OrderSearchRequest addressSearchRequest,
            HttpServletResponse response
    ) {
        List<Order> orders = searchDatabase(addressSearchRequest, resultSegment);

        System.out.println("");
        System.out.println("Dates list: ");
        System.out.println();

        for (Order order : orders){
            System.out.println(order.getDate().toString());
        }

        System.out.println();

        return orders;
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

    private void loadOrdersToMap(Map model, OrderResultSegment resultSegment) {
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

    private void loadOrder(Integer contactId, Map model) {
        Order address = orderDao.get(contactId);
        model.put("address", address);
    }

    private List<Order> searchDatabase(OrderSearchRequest searchRequest, OrderResultSegment resultProperties) {
        return orderDao.search(searchRequest,
                resultProperties);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}