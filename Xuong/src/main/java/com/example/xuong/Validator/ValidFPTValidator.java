package com.example.xuong.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFPTValidator implements ConstraintValidator<ValidFPT, String> {
    @Override
    public void initialize(ValidFPT constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.endsWith("@fpt.edu.vn");
    }
}

