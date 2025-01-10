package fr.fullstack.shopapp.util;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

public class ErrorValidation {
    public static String getErrorValidationMessage(Errors errors) {
        StringBuilder message = new StringBuilder();
        List<ObjectError> objectErrors = errors.getAllErrors();
        for (int i = 0; i < errors.getErrorCount(); i++) {
            ObjectError error = objectErrors.get(i);
            message.append(error.getDefaultMessage()).append("; ");
        }
        return message.substring(0, message.length() - 2);
    }
}
