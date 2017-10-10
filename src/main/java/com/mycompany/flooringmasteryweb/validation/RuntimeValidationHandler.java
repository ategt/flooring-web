package com.mycompany.flooringmasteryweb.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RuntimeValidationHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(HttpMessageNotReadableException httpMessageNotReadableException) {
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        ValidationError validationError = new ValidationError();

        Throwable rootCause = httpMessageNotReadableException.getRootCause();

        if (rootCause instanceof UnparsableDateException) {
            UnparsableDateException unparsableDateException = (UnparsableDateException) rootCause;
            String format = unparsableDateException.getFormat();
            String name = unparsableDateException.getName();
            validationError.setFieldName(name);
            validationError.setMessage("Date should be in  \"" + format + "\" format.");
        } else {
            validationError.setFieldName("Unknown");
            validationError.setMessage("Some part of that input could not be understood.");
        }

        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }

}