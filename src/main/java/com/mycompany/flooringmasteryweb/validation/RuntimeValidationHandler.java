package com.mycompany.flooringmasteryweb.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.text.ParseException;
import java.util.Objects;

@ControllerAdvice
public class RuntimeValidationHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(RuntimeException runtimeException){
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        ValidationError validationError = new ValidationError();
        validationError.setFieldName("Unknown");
        validationError.setMessage("Some part of that input could not be understood.");
        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(ParseException parseException){
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        ValidationError validationError = new ValidationError();
        validationError.setFieldName("Global");
        validationError.setMessage("Some part of that input could not be understood.");
        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }

    @ExceptionHandler(UnparsableDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorContainer processValidationErrors(UnparsableDateException unparsableDateException){
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();

        String unparseableInput = unparsableDateException.getUnparsableInput();
        String name = unparsableDateException.getName();

        ValidationError validationError = new ValidationError();
        validationError.setFieldName(Objects.nonNull(name) ? name : "Global");
        validationError.setMessage("That input could not be understood: " + unparseableInput);
        validationErrorContainer.addError(validationError);
        return validationErrorContainer;
    }
}
