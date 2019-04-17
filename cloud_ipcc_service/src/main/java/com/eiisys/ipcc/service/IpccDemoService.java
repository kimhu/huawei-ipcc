package com.eiisys.ipcc.service;

import java.util.List;

import com.eiisys.ipcc.bean.IpccDemoBean;
import com.eiisys.ipcc.exception.IpccException;

/**
 * Demo service
 */
public interface IpccDemoService {
    /**
     * 查询所有Demo数据表数据
     * @return
     */
    public List<IpccDemoBean> getAllDemoData();
    
    /**
     * 数据插入
     * @param bean
     * @return
     * @throws IpccException
     */
    public Integer insert(IpccDemoBean bean) throws IpccException;
    
    /**
     * 数据插入，加分布式锁，只允许同时一个线程插入相同记录
     * @param bean
     * @return
     * @throws IpccException
     */
    public Integer insertWithLock(IpccDemoBean bean) throws IpccException;
    
    /**
     * 测试缓存
     * @param id
     * @return
     * @throws IpccException
     */
    public List<IpccDemoBean> getIpccDemoWithLock() throws IpccException;
    
    
    /**
     * 数据插入，加分布式锁，只允许同时一个线程插入相同记录
     * @param bean
     * @return
     * @throws IpccException
     */
    public Integer insertWithLockAnnotation(IpccDemoBean bean, String lockKey) throws IpccException;
    
    /**
     * 测试缓存
     * @param lockKey 排他锁key
     * @param cacheKey 缓存key
     * @param keepTime 缓存保存时间
     * @return
     * @throws IpccException
     */
    public List<IpccDemoBean> getIpccDemoWithLockAnnotation(String lockKey, String cacheKey, Long keepTime) throws IpccException;
}
