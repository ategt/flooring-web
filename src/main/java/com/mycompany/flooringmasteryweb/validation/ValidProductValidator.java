package com.mycompany.flooringmasteryweb.validation;

import com.mycompany.flooringmasteryweb.dao.ProductDao;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidProductValidator implements ApplicationContextAware, ConstraintValidator<ValidProductConstraint, String> {

    private ApplicationContext applicationContext;

    @Override
    public void initialize(ValidProductConstraint validProductConstraint) {}

    @Override
    public boolean isValid(String productInput,
                           ConstraintValidatorContext constraintValidatorContext) {

        ProductDao productDao = applicationContext.getBean("productDao", ProductDao.class);
        return productDao.validProductName(productInput);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
