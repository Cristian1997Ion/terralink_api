package com.terralink.terralink_api.domain.shared.validation.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.terralink.terralink_api.domain.shared.repository.BaseEntityRepository;
import com.terralink.terralink_api.domain.shared.validation.validator.UniqueFieldValidator;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueFieldValidator.class)
@Documented
public @interface Unique {
    String message() default "must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends BaseEntityRepository<?>> entityRepository();
    String entityField();
}