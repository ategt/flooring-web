package com.mycompany.flooringmasteryweb.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidProductValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductConstraint {
    String message() default "Invalid Product";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
