package com.mycompany.flooringmasteryweb.validation;

public class UnparsableDateException extends RuntimeException {
    private final String unparsableInput;
    private final String name;
    private final String format;

    public UnparsableDateException(Exception ex, String input, String name, String format){
        super(ex);
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
