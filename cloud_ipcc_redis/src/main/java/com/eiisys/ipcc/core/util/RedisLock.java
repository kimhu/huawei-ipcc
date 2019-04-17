package com.eiisys.ipcc.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.exception.IpccException;

/**
 * 基于Redis的分布式锁工具
 */
public class RedisLock {

    protected RedisClient redisClient;
    
    /** 排他锁Key名 */
    protected String lockKey;
    
    /** 排他锁过期时间 默认15秒*/
    protected Long expireTime = RedisConstants.LOCK_EXPIRE_TIME;
    
    /** 排他锁随机值 */
    protected Long randNum;
    
    protected boolean needReleaseLock = true;
    
    protected RedisLock() {
        // 子类构造方法需要
        
    }
    
    public RedisLock(RedisClient redisClient, String lockKey) {
        this(redisClient, lockKey,  null);
    }

    public RedisLock(RedisClient redisClient, String lockKey, Long expireTime) {
        this.redisClient = redisClient;
        this.lockKey = lockKey;
        this.randNum = genRandNum();
        
        if (expireTime != null) {
            this.expireTime = expireTime;            
        }
    }
    
    /**
     * 生成随机数
     * @return
     */
    public long genRandNum() {
        return RandomUtils.nextLong(0L, 99999999L) + System.currentTimeMillis();
    }
    
    
    /**
     * 尝试加排他锁，如果加锁失败，则抛出异常
     * 
     * @param expireTime 排他锁过期时间
     * @throws IpccException
     */
    public void lock() throws IpccException {
        
        boolean isSeted = redisClient.setnx(lockKey, randNum, expireTime);
        
        if (!isSeted) {
            needReleaseLock = false; // 获得排他锁失败时，不需要释放锁
            throw new IpccException(MsgConstants.MSG_LOCK_CONFLICT);
        }
    }
    
    /**
     * 释放排他锁
     */
    public void release() {
        if (!needReleaseLock) { // 获得排他锁失败时，不需要释放锁
            return;
        }
        
        List<String> keys = new ArrayList<>(1);
        keys.add(lockKey);
        
        redisClient.evalUnlock(RedisConstants.LOCK_RELEASE_SCRIPT, keys, randNum);

    }
}
