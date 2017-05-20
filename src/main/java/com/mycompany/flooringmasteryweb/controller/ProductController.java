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
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author apprentice
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController {

    ProductDao productDao;
    StateDao stateDao;
    OrderDao orderDao;

    @Inject
    public ProductController(
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

        model.put("productCommands", productCommands);
        model.put("productCommand", new ProductCommand());

        return "product\\edit";
    }

    private List<ProductCommand> productCommandList() {
        List<Product> products = productDao.getListOfProducts();
        products = productDao.sortByProductName(products);
        List<ProductCommand> productCommands = productDao.buildCommandProductList(products);
        return productCommands;
    }

    @RequestMapping(value = "/edit/{productName}", method = RequestMethod.GET)
    public String edit(@PathVariable("productName") String productName, Map model) {

        model.put("productCommands", productCommandList());
        model.put("productCommand", productDao.buildCommandProduct(productDao.get(productName)));

        return "product\\edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute ProductCommand productCommand, BindingResult bindingResult, Map model) {

        if (bindingResult.hasErrors()) {

            model.put("productCommand", productCommand);
            model.put("productCommands", productCommandList());

            return "product\\edit";

        } else {

            Product product = productDao.resolveCommandProduct(productCommand);

            if (productDao.get(product.getProductName()) == null) {
                productDao.create(product);
            } else {
                productDao.update(product);
            }

            return "redirect:/product/";
        }

    }

    @RequestMapping(value = "/delete/{productName}", method = RequestMethod.GET)
    public String delete(@PathVariable("productName") String productName) {

        //orderDao.delete(orderDao.get(orderId));
        productDao.delete(productDao.get(productName));

        return "redirect:/product/";
    }

}
