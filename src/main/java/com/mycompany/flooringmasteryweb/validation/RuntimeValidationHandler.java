package com.mycompany.flooringmasteryweb.validation;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.text.ParseException;

@ControllerAdvice
public class RuntimeValidationHandler {

    @ExceptionHandler(UnparsableDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(UnparsableDateException Exception){
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        ValidationError validationError = new ValidationError();
        validationError.setFieldName("Parse Error");
        validationError.setMessage("Some part of that input could not be understood.");
        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }

    //@ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(RuntimeException runtimeException){
        if (runtimeException.getCause() instanceof UnparsableDateException){
            return processValidationErrors((UnparsableDateException) runtimeException.getCause());
        }

        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        ValidationError validationError = new ValidationError();
        validationError.setFieldName("Unknown");
        validationError.setMessage("Some part of that input could not be understood.");
        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }

    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(ParseException parseException){
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        ValidationError validationError = new ValidationError();
        validationError.setFieldName("Parse Error");
        validationError.setMessage("Some part of that input could not be understood.");
        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(HttpMessageNotReadableException httpMessageNotReadableException){
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        ValidationError validationError = new ValidationError();
        validationError.setFieldName("Parse Error");
        validationError.setMessage("Some part of that input could not be understood.");
        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }

}
