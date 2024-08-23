package com.example.xuong.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFEValidator implements ConstraintValidator<ValidFE, String> {
    @Override
    public void initialize(ValidFE constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.endsWith("@fpt.edu.vn");
    }
}
