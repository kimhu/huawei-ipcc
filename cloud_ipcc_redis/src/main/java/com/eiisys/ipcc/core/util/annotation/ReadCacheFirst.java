/**
 * 
 */
package com.eiisys.ipcc.core.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将结果添加到缓存
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadCacheFirst {

//    // 锁的参数名
//    String lockKeyParam();
//    // 缓存值的参数名
//    String cacheKeyParam();
}
