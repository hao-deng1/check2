package com.lostAndFind.project.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author yc
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {

	String value() default "";
}
