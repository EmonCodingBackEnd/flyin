package com.coding.flyin.starter.gray.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
/** 具体原因，参见 GrayAutoConfiguration 的使用描述. */
@Deprecated
public @interface EnableGrayWebMvcFilter {}
