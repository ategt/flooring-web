/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.ControllerUtilites;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author apprentice
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminPanelController {

    ProductDao productDao;
    StateDao stateDao;
    OrderDao orderDao;

    @Inject
    public AdminPanelController(
            ProductDao productDao,
            StateDao stateDao,
            OrderDao orderDao
    ) {

        this.productDao = productDao;
        this.stateDao = stateDao;
        this.orderDao = orderDao;

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String blank(Map model) {

        List<ProductCommand> productCommands = productCommandList();
        List<StateCommand> stateCommands = stateList();

        model.put("states", stateCommands);
        model.put("productCommands", productCommands);

        return "admin\\admin";
    }
  
    private List<StateCommand> stateList() {
        return ControllerUtilites.stateList(stateDao);
    }

    private List<ProductCommand> productCommandList() {
        List<ProductCommand> productCommands = productDao.buildCommandProductList();
        return productCommands;
    }

    @RequestMapping(value = "/editProduct/{productName}", method = RequestMethod.GET)
    public String editProduct(@PathVariable("productName") String productName, Map model) {

        model.put("productCommands", productCommandList());
        model.put("productCommand", ProductCommand.buildProductCommand(productDao.get(productName)));
        return "product\\edit";
    }

    @RequestMapping(value = "/editState/{stateName}", method = RequestMethod.GET)
    public String edit(@PathVariable("stateName") String stateName, Map model) {

        model.put("states", stateList());
        model.put("stateCommand", StateCommand.buildCommandState(stateDao.get(stateName)));

        return "state\\edit";
    }
}