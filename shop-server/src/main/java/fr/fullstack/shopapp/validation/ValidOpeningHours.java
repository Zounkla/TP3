package fr.fullstack.shopapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OpeningHoursValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOpeningHours {
    String message() default "openAt must be earlier than closeAt";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
