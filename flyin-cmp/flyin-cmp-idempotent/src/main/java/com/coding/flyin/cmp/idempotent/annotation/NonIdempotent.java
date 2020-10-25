package com.coding.flyin.cmp.idempotent.annotation;

import com.coding.flyin.cmp.api.AppRequest;

import java.lang.annotation.*;

/**
 * 标注该注解的方法，表示方法提供的API不是幂等操作，需要控制重复执行.
 *
 * <p>创建时间: <font style="color:#00FFFF">20190303 23:49</font><br>
 * 标注在类上，类里面所有API都需要控制以防止重复调用，要求在请求信息中包含 {@linkplain AppRequest#requestId} 参数值。<br>
 * 标注在方法上，方法提供的所有API都需要控制以防止重复调用，要求在请求信息中包含 {@linkplain AppRequest#requestId} 参数值。<br>
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NonIdempotent {}
