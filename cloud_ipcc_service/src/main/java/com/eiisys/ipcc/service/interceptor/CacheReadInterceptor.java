package com.eiisys.ipcc.service.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eiisys.ipcc.bean.lock.QueryLockObj;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.util.RedisQueryLock;
import com.eiisys.ipcc.core.util.annotation.CacheKeepTime;
import com.eiisys.ipcc.core.util.annotation.CacheKey;
import com.eiisys.ipcc.core.util.annotation.LockKey;
import com.eiisys.ipcc.core.util.annotation.ReadCacheFirst;
import com.eiisys.ipcc.exception.IpccException;

/**
 * AopAllianceApplicationContext 配置
 * 避免多线程同时查询数据表
 * 配合 @ReadCacheFirst 方法注解（只能作用在public方法上），参数注解 @LockKey 排他锁， @CacheKey 缓存key， @CacheKeepTime 缓存过期时间
 */
@Component
public class CacheReadInterceptor implements MethodInterceptor {
    @Autowired
    private RedisClient redisClient;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object invocationResult = null;

        Method method = invocation.getMethod();
        Object obj = invocation.getThis();
        Object[] args = invocation.getArguments();

        String lockKey = null;
        String cacheKey = null;
        Long cacheKeepTime = null;

        ReadCacheFirst lockAnnotation = method.getAnnotation(ReadCacheFirst.class);

        if (lockAnnotation == null) {
            throw new IpccException(MsgConstants.MSG_METHOD_ANNO_NOT_ADD, "@ReadCacheFirst", method.getName());

        }

        int paramCnt = method.getParameterCount();
        Parameter[] paramArray = method.getParameters();

        /** @LockKey begin */
        for (int i = 0; i < paramCnt; i++) {
            // 找到 @LockKey 注解标注的参数
            if (paramArray[i].getDeclaredAnnotation(LockKey.class) != null && paramArray[i].getType() == String.class) {
                lockKey = (String) args[i];
                break;
            }
        }

        if (StringUtils.isBlank(lockKey)) {
            throw new IpccException(MsgConstants.MSG_PARAM_ANNO_NOT_ADD, "@LockKey", method.getName());
        }
        /** @LockKey end */
        
        /** @CacheKey begin */
        for (int i = 0; i < paramCnt; i++) {
            // 找到 @LockKey 注解标注的参数
            if (paramArray[i].getDeclaredAnnotation(CacheKey.class) != null
                    && paramArray[i].getType() == String.class) {
                cacheKey = (String) args[i];
                break;
            }
        }

        if (StringUtils.isBlank(cacheKey)) {
            throw new IpccException(MsgConstants.MSG_PARAM_ANNO_NOT_ADD, "@CacheKey", method.getName());
        }
        /** @CacheKey end */
        
        /** @CacheKeepTime begin */
        for (int i = 0; i < paramCnt; i++) {
            // 找到@CacheKeepTime 注解标注的参数
            if (paramArray[i].getDeclaredAnnotation(CacheKeepTime.class) != null
                    && paramArray[i].getType() == Long.class) {
                cacheKeepTime = (Long) args[i];
                break;
            }
        }
        /** @CacheKeepTime end */
        
        // 方法调用前
        if (redisClient.hasKey(cacheKey)) {
            invocationResult = redisClient.get(cacheKey);
            return invocationResult;
        }

        RedisQueryLock redisQueryLock = new RedisQueryLock(redisClient, lockKey, cacheKey);

        try {
            QueryLockObj lockHoldResult = redisQueryLock.queryLock();

            if (QueryLockObj.STS_IS_LOCK == lockHoldResult.getLockStatus()) {

                invocationResult = invocation.proceed();

                // 方法调用后处理
                // 2.将list保存到缓存中
                if (cacheKeepTime == null) {
                    redisClient.set(cacheKey, invocationResult);                    
                } else {
                    redisClient.set(cacheKey, invocationResult, cacheKeepTime);  
                }
            } else {
                invocationResult = lockHoldResult.getQueryResult();
            }
        } finally {
            redisQueryLock.release();
        }
        return invocationResult;
    }

}
