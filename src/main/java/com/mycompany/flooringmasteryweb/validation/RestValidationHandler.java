/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.validation;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        MethodParameter methodParameter = ex.getParameter();

        List<FieldError> fieldErrors = result.getFieldErrors();

        ValidationErrorContainer container = new ValidationErrorContainer();

        for (FieldError error : fieldErrors) {

            ValidationError valError = new ValidationError();
            valError.setFieldName(error.getField());

            String code = error.getCode();
            String[] codes = error.getCodes();

            String[] mess1 = result.resolveMessageCodes(code);

            for (String asf : mess1) {
                String[] mess2 = result.resolveMessageCodes(asf);
                for (String mess : mess2) {
                    ValidationError validationError = new ValidationError();
                    validationError.setFieldName(error.getField());
                    validationError.setMessage(mess);
                    container.addError(validationError);
                }
            }

            for (String acode : codes) {
                String[] mess2 = result.resolveMessageCodes(acode);

                for (String mess : mess2) {
                    ValidationError validationError = new ValidationError();
                    validationError.setFieldName(error.getField());
                    validationError.setMessage(mess);
                    container.addError(validationError);
                }

            }


            valError.setMessage(error.getDefaultMessage());

            container.addError(valError);
        }

        return container;
    }

}
