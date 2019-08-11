package com.ikkong.core.jfinal.ext.shiro;

import org.apache.shiro.authz.annotation.Logical;

import java.lang.annotation.*;

/**
 * 根据URL检查权限
 * Created by ikkong on 2016/11/22.
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequiresUrlPermission {
    String[] value() default "";

    Logical logical() default Logical.AND;
}
