package com.coding.flyin.cmp.api.validation;

import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;

public interface BaseEnum<T> {

    T getCode();

    default String getDesc() {
        throw new AppException(AppStatus.S0001, "当前枚举未实现getDesc方法");
    }
}
