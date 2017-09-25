package com.mycompany.flooringmasteryweb.validation;

import com.mycompany.flooringmasteryweb.utilities.StateUtilities;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidStateValidator implements ConstraintValidator<ValidStateConstraint, String> {

    @Override
    public void initialize(ValidStateConstraint validStateConstraint) { }

    @Override
    public boolean isValid(String stateInput,
                           ConstraintValidatorContext constraintValidatorContext) {
        return StateUtilities.validStateInput(stateInput);
    }
}
