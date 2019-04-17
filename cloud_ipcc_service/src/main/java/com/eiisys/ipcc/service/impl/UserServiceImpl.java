package com.eiisys.ipcc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.eiisys.ipcc.bean.IpccDemoBean;
import com.eiisys.ipcc.bean.lock.QueryLockObj;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.util.RedisLock;
import com.eiisys.ipcc.core.util.RedisQueryLock;
import com.eiisys.ipcc.core.util.annotation.ReadCacheFirst;
import com.eiisys.ipcc.dao.UserDao;
import com.eiisys.ipcc.entity.IpccDemo;
import com.eiisys.ipcc.entity.User;
import com.eiisys.ipcc.exception.IpccException;
import com.eiisys.ipcc.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 数据库service
 * 
 * @author hujm
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisClient<User> redisClient;
    
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById6d(Integer id6d) {
        String redisKey = RedisConstants.RKEY_USER_REDIS + id6d;
        String cacheKey = RedisConstants.RKEY_USER_GET_LOCK + id6d;
        if (redisClient.hasKey(redisKey)) {
            return redisClient.get(redisKey);
        }
        
        User user;
        RedisQueryLock redisQueryLock = new RedisQueryLock(redisClient, redisKey, cacheKey);

        try {
            QueryLockObj lockHoldResult = redisQueryLock.queryLock();

            if (QueryLockObj.STS_IS_LOCK == lockHoldResult.getLockStatus()) {
                // 1.查询数据库，生成User
                user = userDao.getUserById6d(id6d);
                // 2.将list保存到缓存中
                redisClient.set(redisKey, user, 9 * 60 * 60L);
            } else {
                user = (User) lockHoldResult.getQueryResult();
            }
        } finally {
            redisQueryLock.release();
        }
        
        return user;
    }

    @Override
    public int updateUser(User user) {
        String lockKey = RedisConstants.REEY_USER_UPDATE_LOCK + user.getId6d();
        
        RedisLock redisLock = new RedisLock(redisClient, lockKey);

        try {
            redisLock.lock();
            
            if (user.getId6d() == null) {
                throw new IpccException("");// 带个message?
            }
            
            return userDao.updateUser(user);
        } finally {
            redisLock.release();
        }
    }
    
   /* 
    @Service
    class InnerService {
        
        @ReadCacheFirst
        User getUser(String lockKey, String cacheKey) {
            
            return null;
        }
    }*/
}
