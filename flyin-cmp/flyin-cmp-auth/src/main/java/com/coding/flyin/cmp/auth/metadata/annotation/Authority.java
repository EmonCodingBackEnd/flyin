package com.coding.flyin.cmp.auth.metadata.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authority {

    /** 需要拥有的权限 */
    String[] value();
}
