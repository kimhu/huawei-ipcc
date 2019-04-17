package com.eiisys.ipcc.core.util;

import com.eiisys.ipcc.bean.lock.QueryLockObj;

/**
 * 查询锁，目的是保证同时只能由一个线程访问数据库，其他线程等待从缓存中取值返回结果
 */
public class RedisQueryLock extends RedisLock {

    // 查询结果缓存Key
    private String cacheKey;
    
    public RedisQueryLock(RedisClient redisClient, String lockKey, String cacheKey) {
       this(redisClient, lockKey, cacheKey, null);
    }

    public RedisQueryLock(RedisClient redisClient, String lockKey, String cacheKey, Long expireTime) {
        this.redisClient = redisClient;
        this.lockKey = lockKey;
        this.cacheKey = cacheKey;
        this.randNum = genRandNum();
        
        if (expireTime != null) {
            this.expireTime = expireTime;            
        }
    }
    
    /**
     * 使用排他锁控制取值，主要用来保证只有一个实例可以从数据库查询数据，保存到缓存中。
     * 在使用前，必须先尝试从缓存中获取数据，获取失败则使用此功能获得排他锁，然后查询数据库。
     * @param cacheKey 缓存key值
     * @return 
     */
    public QueryLockObj queryLock() {
        boolean isSeted = redisClient.setnx(lockKey, randNum, expireTime);

        QueryLockObj lockHoldResult = new QueryLockObj();
        
        // 如果获取到锁，则结束；如果未获得锁，尝试取值
        if (isSeted) {
            lockHoldResult.setLockStatus(QueryLockObj.STS_IS_LOCK);
        } else {
            needReleaseLock = false; // 获得排他锁失败时，不需要释放锁
            
            int waitCnt = 0;
            Object queryResult = null;
            
            while(queryResult == null && waitCnt < 3) {
                waitCnt++;
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                queryResult = redisClient.get(cacheKey);
            }
            
            lockHoldResult.setQueryResult(queryResult);
        }
        
        return lockHoldResult;
        
    }
}
