package com.mycompany.flooringmasteryweb.errorHandling;

import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoSuchMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String noMessageExceptionHandler(NoSuchMessageException ex){
        return ex.getMessage();
    }
}
