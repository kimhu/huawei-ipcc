package com.eiisys.ipcc.core.util.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来标注方法参数作为返回结果缓存key值，参数必须是String类型
 */
@Documented
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface CacheKeepTime {

}
