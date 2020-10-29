package com.coding.flyin.cmp.common.validation.validator;

import com.coding.flyin.cmp.common.validation.BaseEnum;
import com.coding.flyin.cmp.common.validation.EnumOperations;
import com.coding.flyin.cmp.common.validation.constraints.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private String[] strValues;
    private int[] intValues;
    private long[] longValues;
    private Class<? extends BaseEnum> enumClazz;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
        longValues = constraintAnnotation.longValues();
        enumClazz = constraintAnnotation.enumClazz();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 即是枚举又是 BaseEnum 的子类
        if (BaseEnum.class.isAssignableFrom(enumClazz) && Enum.class.isAssignableFrom(enumClazz)) {
            return EnumOperations.getByCode(enumClazz, value) != null;
        } else {
            if (value instanceof String) {
                for (String strValue : strValues) {
                    if (strValue.equals(value)) {
                        return true;
                    }
                }
            } else if (value instanceof Integer) {
                for (Integer intValue : intValues) {
                    if (Objects.equals(intValue, value)) {
                        return true;
                    }
                }
            } else if (value instanceof Long) {
                for (Long longValue : longValues) {
                    if (Objects.equals(longValue, value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
