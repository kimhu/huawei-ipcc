package com.eiisys.ipcc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.eiisys.ipcc.bean.lock.QueryLockObj;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.util.RedisLock;
import com.eiisys.ipcc.core.util.RedisQueryLock;
import com.eiisys.ipcc.dao.CompanyDao;
import com.eiisys.ipcc.entity.Company;
import com.eiisys.ipcc.exception.IpccException;
import com.eiisys.ipcc.service.CompanyService;

public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;
    
    @Autowired
    private RedisClient<Company> redisClient;
    
    @Override
    public Company getCompanyByCompanyId(Integer companyId) {
        String redisKey = RedisConstants.RKEY_COMPANY_REDIS + companyId;
        String cacheKey = RedisConstants.RKEY_COMPANY_GET_LOCK + companyId;
        if (redisClient.hasKey(redisKey)) {
            return redisClient.get(redisKey);
        }
        
        Company company;
        RedisQueryLock redisQueryLock = new RedisQueryLock(redisClient, redisKey, cacheKey);

        try {
            QueryLockObj lockHoldResult = redisQueryLock.queryLock();

            if (QueryLockObj.STS_IS_LOCK == lockHoldResult.getLockStatus()) {
                // 1.查询数据库，生成User
                company = companyDao.getCompanyByCompanyId(companyId);
                // 2.将list保存到缓存中
                redisClient.set(redisKey, company, 9 * 60 * 60L);
            } else {
                company = (Company) lockHoldResult.getQueryResult();
            }
        } finally {
            redisQueryLock.release();
        }
        
        return company;
    }

    @Override
    public int updateCompany(Company company) {
        String lockKey = RedisConstants.REEY_COMPANY_UPDATE_LOCK + company.getCompanyId();
        
        RedisLock redisLock = new RedisLock(redisClient, lockKey);

        try {
            redisLock.lock();
            
            if (company.getCompanyId() == null) {
                throw new IpccException("");// 带个message?
            }
            
            return companyDao.updateCompany(company);
        } finally {
            redisLock.release();
        }
    }
}
