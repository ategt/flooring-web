/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.OrderCommand;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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

    @Inject
    public HomeController(
            ProductDao productDao,
            StateDao stateDao,
            OrderDao orderDao
    ) {
        this.productDao = productDao;
        this.stateDao = stateDao;
        this.orderDao = orderDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Map model, HttpServletRequest httpServletRequest) {

        return "redirect:/orders/";
    }

    public void putBlankOrder(Map model) {
        OrderCommand orderCommand = new OrderCommand();
        orderCommand.setId(0);

        model.put("orderCommand", orderCommand);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search() {

        return "redirect:/orders/search";
    }

    @RequestMapping(value = "/adminPanel", method = RequestMethod.GET)
    public String redirectToAdminPanel() {

        return "redirect:/adminPanel/";
    }

    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public String stateBlank() {
        return "redirect:/state/";
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String productBlank() {
        return "redirect:/product/";
    }
}
