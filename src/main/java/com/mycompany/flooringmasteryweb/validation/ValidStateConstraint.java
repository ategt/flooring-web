package com.mycompany.flooringmasteryweb.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidStateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStateConstraint {
    String message() default "Invalid State";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
