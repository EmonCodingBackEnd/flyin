package com.coding.flyin.cmp.auth.metadata.annotation;

import java.lang.annotation.*;

/**
 * 单个权限校验注解.
 *
 * <p>创建时间: <font style="color:#00FFFF">20210510 22:18</font><br>
 * 单个权限校验，spel表达式默认为<code>""</code>表示条件生效
 *
 * @author emon
 * @version 0.1.0
 * @since 0.1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authority {

    /** 权限不足时描述信息. */
    String message() default "无权访问该功能";

    /** spel表达式. */
    String spel() default "";

    /** 需要拥有的权限 */
    String[] value();
}
