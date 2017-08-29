/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.aop;

import com.mycompany.flooringmasteryweb.dao.AuditDao;
import com.mycompany.flooringmasteryweb.dto.Audit;
import com.mycompany.flooringmasteryweb.dto.Order;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.utilities.ProductUtilities;
import org.aspectj.lang.JoinPoint;

/**
 *
 * @author ATeg
 */
public class ProductTitleCasingAspect {

    public void titleCaseProduct(JoinPoint jp) throws Throwable {

        Object[] args = jp.getArgs();
        Product product = (Product) args[0];

        product = ProductUtilities.titleCaseProductName(product);

        args[0] = product;
    }
}
