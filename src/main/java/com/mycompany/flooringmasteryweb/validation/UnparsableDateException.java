package com.mycompany.flooringmasteryweb.validation;

import org.springframework.http.converter.HttpMessageNotReadableException;

public class UnparsableDateException extends HttpMessageNotReadableException {
    private final String unparsableInput;
    private final String name;
    private final String format;

    public UnparsableDateException(Exception ex, String input, String name, String format) {
        super(input);
        unparsableInput = input;
        this.name = name;
        this.format = format;
    }

    public String getUnparsableInput() {
        return unparsableInput;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }
}
