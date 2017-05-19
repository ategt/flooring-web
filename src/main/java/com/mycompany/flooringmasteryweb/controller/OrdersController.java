/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.BasicOrderImpl;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author apprentice
 */
@Controller
@RequestMapping(value = "/orders")
public class OrdersController {

    ProductDao productDao;
    StateDao stateDao;
    OrderDao orderDao;

    @Inject
    public OrdersController(
            ProductDao productDao,
            StateDao stateDao,
            OrderDao orderDao
    ) {
        this.productDao = productDao;
        this.stateDao = stateDao;
        this.orderDao = orderDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map model) {
        loadOrdersToMap(model);
        loadStateCommandsToMap(model);
        loadProductCommandsToMap(model);

        putBlankOrder(model);

        return "order\\index";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}, headers = "Accept=application/json")
    @ResponseBody
    public Order showWithAjax(@PathVariable("id") Integer orderId) {
        Order contact = orderDao.get(orderId);

        return contact;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces=MediaType.TEXT_HTML_VALUE) //, headers = "Accept=text/html")
    public String showWithHtml(@PathVariable("id") Integer orderId, Map model) {
        Order order = orderDao.get(orderId);

        OrderCommand orderCommand = orderDao.resolveOrderCommand(order);

        model.put("orderCommand", orderCommand);
        model.put("order", order);
        loadTheOrdersList(model);

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
        orders = orderDao.sortByOrderNumber(orders);
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
    public String update(@ModelAttribute BasicOrderImpl basicOrder) {
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
    public String editSubmit(@ModelAttribute BasicOrderImpl basicOrder) {
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
    public String searchSubmit(@RequestParam("searchBy") String searchBy, @RequestParam("searchText") String searchText, Map model) {
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

    private void loadOrdersToMap(Map model) {
        List<Order> orders = orderDao.getList();
        orders = orderDao.sortByOrderNumber(orders);
        model.put("orders", orders);
    }

    private void loadProductCommandsToMap(Map model) {
        List<Product> products = productDao.getListOfProducts();
        List<ProductCommand> productCommands = productDao.buildCommandProductList(products);
        model.put("productCommands", productCommands);
    }

    private void loadStateCommandsToMap(Map model) {
        List<State> states = stateDao.getListOfStates();
        List<StateCommand> stateCommands = stateDao.buildCommandStateList(states);
        model.put("stateCommands", stateCommands);
    }

    public void putBlankOrder(Map model) {
        OrderCommand orderCommand = new OrderCommand();
        orderCommand.setId(0);

        model.put("orderCommand", orderCommand);
    }
}