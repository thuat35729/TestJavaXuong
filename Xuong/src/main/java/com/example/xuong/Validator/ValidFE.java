package com.example.xuong.Validator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Constraint(validatedBy = ValidFEValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFE {
    String message() default "Email FE phải kết thúc với @fe.edu.vn";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
