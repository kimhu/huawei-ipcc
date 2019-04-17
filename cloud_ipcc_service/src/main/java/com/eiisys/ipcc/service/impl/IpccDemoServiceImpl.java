package com.eiisys.ipcc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.eiisys.ipcc.bean.IpccDemoBean;
import com.eiisys.ipcc.bean.lock.QueryLockObj;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.util.RedisLock;
import com.eiisys.ipcc.core.util.RedisQueryLock;
import com.eiisys.ipcc.core.util.annotation.CacheKeepTime;
import com.eiisys.ipcc.core.util.annotation.CacheKey;
import com.eiisys.ipcc.core.util.annotation.ExclusiveLock;
import com.eiisys.ipcc.core.util.annotation.LockKey;
import com.eiisys.ipcc.core.util.annotation.ReadCacheFirst;
import com.eiisys.ipcc.dao.DemoDao;
import com.eiisys.ipcc.entity.IpccDemo;
import com.eiisys.ipcc.exception.IpccException;
import com.eiisys.ipcc.service.IpccDemoService;

@Service
public class IpccDemoServiceImpl implements IpccDemoService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private DemoDao demoDao;

    @Override
    public List<IpccDemoBean> getAllDemoData() {
        List<IpccDemo> list = demoDao.selectAllData();
        List<IpccDemoBean> retList = Collections.emptyList();

        if (!CollectionUtils.isEmpty(list)) {
            retList = new ArrayList<>();

            for (IpccDemo ipccDemo : list) {
                IpccDemoBean bean = new IpccDemoBean();
                BeanUtils.copyProperties(ipccDemo, bean);
                retList.add(bean);
            }
        }

        return retList;
    }

    @Transactional
    @Override
    public Integer insert(IpccDemoBean bean) throws IpccException {

        IpccDemo ipccDemo = new IpccDemo();
        BeanUtils.copyProperties(bean, ipccDemo);
        int insertCnt = demoDao.insert(ipccDemo);

        if (bean.getDemoName() == null || "error".equals(bean.getDemoName())) {
            throw new IpccException("");
        }

        return insertCnt;
    }

    @Transactional
    @Override
    public Integer insertWithLock(IpccDemoBean bean) throws IpccException {
        
        String lockKey = RedisConstants.RKEY_DEMO_INSERT_LOCK + bean.getDemoName();
        
        RedisLock redisLock = new RedisLock(redisClient, lockKey);

        try {
            redisLock.lock();
            
            IpccDemo ipccDemo = new IpccDemo();
            BeanUtils.copyProperties(bean, ipccDemo);
            
            if (bean.getDemoName() == null || "error".equals(bean.getDemoName())) {
                throw new IpccException("");// 带个message?
            }
            
            return  demoDao.insert(ipccDemo);
        } finally {
            redisLock.release();
        }
        

    }
    
    /**
     * 
     */
    @Override
    public List<IpccDemoBean> getIpccDemoWithLock() throws IpccException {
        List<IpccDemoBean> retList = Collections.emptyList();

        String lockKey = RedisConstants.RKEY_DEMO_LIST_LOCK + "all";
        String cacheKey = RedisConstants.RKEY_DEMO_LIST + "all";
        long cacheKeepTime = 600L;// 数据缓存时长10分钟

        if (redisClient.hasKey(cacheKey)) {
            retList = (List<IpccDemoBean>) redisClient.get(cacheKey);
            return retList;
        }
        
        RedisQueryLock redisQueryLock = new RedisQueryLock(redisClient, lockKey, cacheKey);

        try {
            QueryLockObj lockHoldResult = redisQueryLock.queryLock();

            if (QueryLockObj.STS_IS_LOCK == lockHoldResult.getLockStatus()) {
                // 1.查询数据库，生成list
                List<IpccDemo> list = demoDao.selectAllData();

                if (!CollectionUtils.isEmpty(list)) {
                    retList = new ArrayList<>();

                    for (IpccDemo ipccDemo : list) {
                        IpccDemoBean bean = new IpccDemoBean();
                        BeanUtils.copyProperties(ipccDemo, bean);
                        retList.add(bean);
                    }
                }

                // 2.将list保存到缓存中
                redisClient.set(cacheKey, retList, cacheKeepTime);
            } else {
                retList = (List<IpccDemoBean>) lockHoldResult.getQueryResult();
            }
        } finally {
            redisQueryLock.release();
        }

        return retList;
    }
    
    @ExclusiveLock
    @Override
    public Integer insertWithLockAnnotation(IpccDemoBean bean, @LockKey String lockKey) throws IpccException {

//        String lockKey = RedisConstants.RKEY_DEMO_INSERT_LOCK + bean.getDemoName();
        
        return insertDemoDataWithLock(bean);
    }

    @ReadCacheFirst
    @Override
    public List<IpccDemoBean> getIpccDemoWithLockAnnotation(@LockKey String lockKey, @CacheKey String cacheKey, @CacheKeepTime Long keepTime) throws IpccException {
//        String lockKey = RedisConstants.RKEY_DEMO_LIST_LOCK + "all";
//        String cacheKey = RedisConstants.RKEY_DEMO_LIST + "all";
        return queryAllDemoDataWithReadCache();
    }
    
    /**
     * 使用 '@ExclusiveLock'注解标识此方法使用排他锁控制，使用'@LockKey'标识排他锁的key值
     * @param bean
     * @param lockKey
     * @return
     */
    private Integer insertDemoDataWithLock(IpccDemoBean bean) {
        IpccDemo ipccDemo = new IpccDemo();
        BeanUtils.copyProperties(bean, ipccDemo);
        
        if (bean.getDemoName() == null || "error".equals(bean.getDemoName())) {
            throw new IpccException("");// 带个message?
        }
        
        return  demoDao.insert(ipccDemo);
    }

    
    private List<IpccDemoBean> queryAllDemoDataWithReadCache() {
        List<IpccDemoBean> retList = null;
        List<IpccDemo> list = demoDao.selectAllData();

        if (!CollectionUtils.isEmpty(list)) {
            retList = new ArrayList<>();

            for (IpccDemo ipccDemo : list) {
                IpccDemoBean bean = new IpccDemoBean();
                BeanUtils.copyProperties(ipccDemo, bean);
                retList.add(bean);
            }
        }
        
        return retList;
    }

}
