/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.validation;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    public RestValidationHandler() {
        applicationContext = ApplicationContextProvider.getApplicationContext();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(MethodArgumentNotValidException ex) {

        if (Objects.isNull(applicationContext)) {
            applicationContext = ApplicationContextProvider.getApplicationContext();
        }

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
        String resultMessage = null;

        Locale locale = Locale.getDefault();
        String choosenCode = error.getCode();
        String[] codes = error.getCodes();
        Object[] args = error.getArguments();
        for (String code : codes) {
            try {
                String message = applicationContext.getMessage(code, args, locale);
                resultMessage = Strings.isNullOrEmpty(message) ? null : message;
            } catch (org.springframework.context.NoSuchMessageException ex) {
                System.out.println("Message Failed To Process: \"" + code + "\",  " + ex.getMessage());
            }
        }

        if (Objects.isNull(resultMessage)) {
            String defaultMessage = error.getDefaultMessage();

            if (Objects.nonNull(defaultMessage)) {
                MessageFormat form = new MessageFormat(defaultMessage);
                resultMessage = form.format(args);
            } else {
                String defaultMessageCode = applicationContext.getBean("defaultMessageCode", String.class);
                resultMessage = applicationContext.getMessage(defaultMessageCode, error.getArguments(), Locale.getDefault());
            }
        }
        return resultMessage;
    }
}