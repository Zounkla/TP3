package fr.fullstack.shopapp.validation;

import fr.fullstack.shopapp.model.OpeningHoursShop;
import fr.fullstack.shopapp.model.Shop;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.util.*;

public class ConsistentOpeningHoursValidator implements ConstraintValidator<ConsistentOpeningHours, Shop> {

    @Override
    public boolean isValid(Shop shop, ConstraintValidatorContext constraintValidatorContext) {
        Map<Long, List<OpeningHoursShop>> openingByDay = new HashMap<>();
        for (OpeningHoursShop hours : shop.getOpeningHours()) {
            openingByDay.computeIfAbsent(hours.getDay(), k -> new ArrayList<>()).add(hours);
        }

        for (Map.Entry<Long, List<OpeningHoursShop>> entry : openingByDay.entrySet()) {
            List<OpeningHoursShop> dayHours = entry.getValue();
            if (hasOverlaps(dayHours)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasOverlaps(List<OpeningHoursShop> hoursList) {
        hoursList.sort(Comparator.comparing(OpeningHoursShop::getOpenAt));
        for (int i = 0; i < hoursList.size() - 1; i++) {
            LocalTime closeAt = hoursList.get(i).getCloseAt();
            LocalTime nextOpenAt = hoursList.get(i + 1).getOpenAt();
            if (closeAt.isAfter(nextOpenAt)) {
                return true;
            }
        }
        return false;
    }
}
