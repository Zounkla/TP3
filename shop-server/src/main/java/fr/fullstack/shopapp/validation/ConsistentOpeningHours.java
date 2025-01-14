package fr.fullstack.shopapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ConsistentOpeningHoursValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsistentOpeningHours {
    String message() default "opening hours can't be on conflict with others";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
