package com.terralink.terralink_api.domain.shared.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.terralink.terralink_api.domain.shared.repository.BaseEntityRepository;
import com.terralink.terralink_api.domain.shared.validation.constraint.Unique;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class UniqueFieldValidator implements ConstraintValidator<Unique, Object> {
    @Autowired
    private ApplicationContext applicationContext;

    private BaseEntityRepository<?> entityRepository;
    private String entityField;

    @Override
    public void initialize(Unique unique) {
        Class<? extends BaseEntityRepository<?>> clazz = unique.entityRepository();
        this.entityRepository = this.applicationContext.getBean(clazz);
        this.entityField = unique.entityField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try{
            return ! this.entityRepository.existsWithAttribute(this.entityField, object).toFuture().get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
