package fr.fullstack.shopapp.validation;

import fr.fullstack.shopapp.model.OpeningHoursShop;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OpeningHoursValidator implements ConstraintValidator<ValidOpeningHours, OpeningHoursShop> {

    @Override
    public boolean isValid(OpeningHoursShop openingHoursShop, ConstraintValidatorContext constraintValidatorContext) {
        return openingHoursShop.getOpenAt().isBefore(openingHoursShop.getCloseAt());
    }
}
