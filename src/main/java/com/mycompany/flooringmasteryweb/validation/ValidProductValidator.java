package com.mycompany.flooringmasteryweb.validation;

import com.mycompany.flooringmasteryweb.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidProductValidator implements ConstraintValidator<ValidProductConstraint, String> {

    @Autowired
    private ProductDao productDao;

    @Override
    public void initialize(ValidProductConstraint validProductConstraint) {}

    @Override
    public boolean isValid(String productInput,
                           ConstraintValidatorContext constraintValidatorContext) {
        return productDao.validProductName(productInput);
    }
}
