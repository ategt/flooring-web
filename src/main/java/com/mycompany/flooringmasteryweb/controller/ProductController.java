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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Product[] index() {
        List<Product> products = productDao.getListOfProducts();
        return products.toArray(new Product[products.size()]);
    }

    private List<ProductCommand> productCommandList() {
        List<ProductCommand> productCommands = productDao.buildCommandProductList();
        return productCommands;
    }

    @RequestMapping(value = "/edit/{productName}", method = RequestMethod.GET)
    public String edit(@PathVariable("productName") String productName, Map model) {

        model.put("productCommands", productCommandList());
        model.put("productCommand", ProductCommand.buildProductCommand(productDao.get(productName)));

        return "product\\edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute ProductCommand productCommand, BindingResult bindingResult, Map model) {

        if (bindingResult.hasErrors()) {
            model.put("productCommand", productCommand);
            model.put("productCommands", productCommandList());

            return "product\\edit";
        } else {
            Product product = Product.buildProduct(productCommand);

            if (productDao.get(product.getProductName()) == null) {
                productDao.create(product);
            } else {
                productDao.update(product);
            }

            return "redirect:/product/";
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, headers = "Accept=application/json")
    @ResponseBody
    public Product update(@Valid @RequestBody ProductCommand productCommand) {

        Product product = Product.buildProduct(productCommand);

        if (productDao.get(product.getProductName()) == null) {
            product = productDao.create(product);
        } else {
            product = productDao.update(product);
        }

        return product;
    }

    @RequestMapping(value = "/{productName}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Product show(@PathVariable("productName") String productName) {
        
        
        return productDao.get(productName);
    }

    @RequestMapping(value = "/{productName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    @ResponseBody
    public Product delete(@PathVariable("productName") String productName) {
        Product productToDelete = productDao.get(productName);
        return productDao.delete(productToDelete);
    }

    @RequestMapping(value = "/delete/{productName}", method = RequestMethod.GET)
    public String delete(@PathVariable("productName") String productName, Map model) {
        productDao.delete(productDao.get(productName));
        return "redirect:/product/";
    }
}
