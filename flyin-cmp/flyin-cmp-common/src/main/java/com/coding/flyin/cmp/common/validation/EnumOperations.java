package com.coding.flyin.cmp.common.validation;

import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;

public interface EnumOperations {

    /** 根据T类型的code值获取枚举实例，如果找不到则返回null. */
    static <T, E extends BaseEnum<T>> E getByCode(Class<E> enumClass, T code) {
        for (E each : enumClass.getEnumConstants()) {
            if (each.getCode().equals(code)) {
                return each;
            }
        }
        return null;
    }

    /** 根据T类型的code值获取枚举实例，如果找不到则抛异常. */
    static <T, E extends BaseEnum<T>> E getByCodeNoisy(Class<E> enumClass, T code) {
        E e = getByCode(enumClass, code);
        if (e == null) {
            throw new AppException(AppStatus.S0001, "根据字典值找不到对应字典");
        }
        return e;
    }

    /** 根据String类型的desc值获取枚举实例，如果枚举未实现getDesc方法会抛异常，如果找不到则返回null. */
    static <T, E extends BaseEnum<T>> E getByDesc(Class<E> enumClass, String desc) {
        for (E each : enumClass.getEnumConstants()) {
            if (each.getDesc().equals(desc)) {
                return each;
            }
        }
        return null;
    }

    /** 根据String类型的desc值获取枚举实例，如果枚举未实现getDesc方法会抛异常，如果找不到则抛异常. */
    static <T, E extends BaseEnum<T>> E getByDescNoisy(Class<E> enumClass, String desc) {
        E e = getByDesc(enumClass, desc);
        if (e == null) {
            throw new AppException(AppStatus.S0001, "根据字典描述找不到对应字典");
        }
        return e;
    }
}
