/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.validation;

import java.util.List;
import java.util.Locale;

import com.google.common.base.Strings;
import com.mycompany.flooringmasteryweb.aop.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author apprentice
 */
@ControllerAdvice
public class RestValidationHandler {

    private ApplicationContext applicationContext;

    public RestValidationHandler(){
        applicationContext = ApplicationContextProvider.getApplicationContext();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        MethodParameter methodParameter = ex.getParameter();

        List<FieldError> fieldErrors = result.getFieldErrors();

        ValidationErrorContainer container = new ValidationErrorContainer();

        populateFieldErrors(fieldErrors, container);

        List<ObjectError> objectErrors = result.getGlobalErrors();

        populateObjectErrors(objectErrors, container);

        return container;
    }

    private ValidationErrorContainer populateFieldErrors(List<FieldError> fieldErrors, ValidationErrorContainer container) {
        for (FieldError error : fieldErrors) {

            ValidationError valError = new ValidationError();
            valError.setFieldName(error.getField());

            valError.setMessage(produceMessage(error));

            container.addError(valError);
        }

        return container;
    }

    private ValidationErrorContainer populateObjectErrors(List<ObjectError> fieldErrors, ValidationErrorContainer container) {
        for (ObjectError error : fieldErrors) {

            ValidationError valError = new ValidationError();
            valError.setFieldName(null);

            valError.setMessage(produceMessage(error));

            container.addError(valError);
        }

        return container;
    }

    private String produceMessage(ObjectError error) {
        try {
            String message = applicationContext.getMessage(error.getCode(), error.getArguments(), Locale.getDefault());
            return Strings.isNullOrEmpty(message) ? error.getDefaultMessage() : message;
        } catch (org.springframework.context.NoSuchMessageException ex){
            String defaultMessageCode = applicationContext.getBean("defaultMessageCode", String.class);
            return applicationContext.getMessage(defaultMessageCode, error.getArguments(), Locale.getDefault());
        }
    }
}