package com.eiisys.ipcc.service.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.util.RedisLock;
import com.eiisys.ipcc.core.util.annotation.ExclusiveLock;
import com.eiisys.ipcc.core.util.annotation.LockKey;
import com.eiisys.ipcc.exception.IpccException;

/**
 * AopAllianceApplicationContext 中配置好只有 @ExclusiveLock 注解的方法才能被拦截<br>
 * 配合参数注解 @LockKey 排他锁key
 */
@Component
public class ExclusiveLockInterceptor implements MethodInterceptor {
    @Autowired
    private RedisClient redisClient;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object invocationResult = null;

        Method method = invocation.getMethod();
        Object obj = invocation.getThis();
        Object[] args = invocation.getArguments();

        // 方法调用前
        ExclusiveLock lockAnnotation = method.getAnnotation(ExclusiveLock.class);

        if (lockAnnotation == null) {
            throw new IpccException(MsgConstants.MSG_METHOD_ANNO_NOT_ADD, "@ExclusiveLock", method.getName());
        }

        String lockKey = null;

        int paramCnt = method.getParameterCount();

        Parameter[] paramArray = method.getParameters();

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

        RedisLock redisLock = new RedisLock(redisClient, lockKey);

        try {
            redisLock.lock();

            invocationResult = invocation.proceed();

        } finally {
            redisLock.release();
        }

        // 方法调用后处理

        return invocationResult;
    }

}
