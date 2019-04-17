package com.eiisys.ipcc.service.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eiisys.ipcc.service.interceptor.CacheReadInterceptor;
import com.eiisys.ipcc.service.interceptor.ExclusiveLockInterceptor;

/**
 * 负责把方法拦截器指定切入点
 */
@Configuration
public class AopAllianceApplicationContext {

    /**
     * 添加排他锁方法拦截器，针对附加@ExclusiveLock注解的 service方法
     * @param exclusiveLockInterceptor
     * @return
     */
    @Bean("exclusiveLockAdvisor")
    public Advisor exclusiveLockAdvisor(ExclusiveLockInterceptor exclusiveLockInterceptor) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        
        pointcut.setExpression("execution(* com.eiisys.ipcc.service.*.*(..)) && @annotation(com.eiisys.ipcc.core.util.annotation.ExclusiveLock)");
        
        return new DefaultPointcutAdvisor(pointcut, exclusiveLockInterceptor);
    }
    
    /**
     * 添加@ReadCacheFirst的方法拦截器，针对附加@ReadCacheFirst注解的 service方法
     * @param cacheReadInterceptor
     * @return
     */
    @Bean("readCacheFirstAdvisor")
    public Advisor readCacheFirstAdvisor(CacheReadInterceptor cacheReadInterceptor) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        
        pointcut.setExpression("execution(* com.eiisys.ipcc.service.*.*(..)) && @annotation(com.eiisys.ipcc.core.util.annotation.ReadCacheFirst)");
        
        return new DefaultPointcutAdvisor(pointcut, cacheReadInterceptor);
    }
}
