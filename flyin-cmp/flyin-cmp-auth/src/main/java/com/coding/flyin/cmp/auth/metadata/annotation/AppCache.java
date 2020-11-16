package com.coding.flyin.cmp.auth.metadata.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppCache {

    /**
     * Declares whether the annotated dependency is required.
     *
     * <p>Defaults to {@code true}.
     */
    boolean required() default true;
}
