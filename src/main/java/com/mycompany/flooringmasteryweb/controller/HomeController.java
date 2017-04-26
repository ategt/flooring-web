/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.ConfigDao;
import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author apprentice
 */
@Controller
public class HomeController {

    ProductDao productDao;
    StateDao stateDao;
    OrderDao orderDao;
    ConfigDao configDao;

    @Inject
    public HomeController(
            ProductDao productDao,
            StateDao stateDao,
            OrderDao orderDao,
            ConfigDao configDao
    ) {

        this.productDao = productDao;
        this.stateDao = stateDao;
        this.orderDao = orderDao;
        this.configDao = configDao;

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Map model) {

        loadOrdersToMap(model);
        loadStateCommandsToMap(model);
        loadProductCommandsToMap(model);

        putBlankOrder(model);

        return "home";
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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search() {

        return "redirect:/FlooringMaster/search";
    }

    @RequestMapping(value = "/adminPanel", method = RequestMethod.GET)
    public String redirectToAdminPanel() {

        return "redirect:/adminPanel/";
    }

    @RequestMapping(value = "/adminStatePanel", method = RequestMethod.GET)
    public String stateBlank() {
        return "redirect:/adminStatePanel/";
    }

    @RequestMapping(value = "/adminProductPanel", method = RequestMethod.GET)
    public String productBlank() {
        return "redirect:/adminProductPanel/";
    }

}
