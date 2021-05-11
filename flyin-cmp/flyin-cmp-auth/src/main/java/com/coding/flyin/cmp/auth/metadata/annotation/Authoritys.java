package com.coding.flyin.cmp.auth.metadata.annotation;

import java.lang.annotation.*;

/**
 * 一组权限校验注解.
 *
 * <p>创建时间: <font style="color:#00FFFF">20210510 22:18</font><br>
 * 可以配置一组{@linkplain Authority 单个权限校验注解}，按照顺序执行，发现spel表达式为<code>""</code>或者求值为true则停止。
 *
 * @author emon
 * @version 0.1.30
 * @since 0.1.30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authoritys {
    Authority[] value();
}
