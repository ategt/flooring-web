package com.mycompany.flooringmasteryweb.validation;

import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintValidator;

public class TestConstrainValidationFactory extends SpringWebConstraintValidatorFactory {

    private final WebApplicationContext ctx;

    private boolean isValid = false;

    public TestConstrainValidationFactory(WebApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public < T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        ConstraintValidator instance = super.getInstance(key);
        if (instance instanceof ValidProductValidator) {
            ValidProductValidator validProductValidator = (ValidProductValidator) instance;
            validProductValidator.setApplicationContext(ctx);
            instance = validProductValidator;
        }
        return (T) instance;
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return ctx;
    }

}