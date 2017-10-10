package com.mycompany.flooringmasteryweb.validation;

public class UnparsableDateException extends RuntimeException {
    private final String unparsableInput;
    private final String name;

    public UnparsableDateException(Exception ex, String input){
        super(ex);
        unparsableInput = input;
        this.name = null;
    }

    public UnparsableDateException(Exception ex, String input, String name){
        super(ex);
        unparsableInput = input;
        this.name = name;
    }

    public String getUnparsableInput() {
        return unparsableInput;
    }

    public String getName() {
        return name;
    }
}
