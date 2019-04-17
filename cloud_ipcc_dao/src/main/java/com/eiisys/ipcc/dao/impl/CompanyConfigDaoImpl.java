package com.eiisys.ipcc.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eiisys.core.mapper.BaseMapper;
import com.eiisys.ipcc.dao.CompanyConfigDao;
import com.eiisys.ipcc.entity.CompanyConfig;
import com.eiisys.ipcc.mapper.CompanyConfigMapper;

@Repository
public class CompanyConfigDaoImpl extends BaseDaoImpl<CompanyConfig> implements CompanyConfigDao {
    @Autowired
    private CompanyConfigMapper configMapper;
    
    @Override
    protected BaseMapper<CompanyConfig> getMapper() {
        return configMapper;
    }

    @Override
    public CompanyConfig getConfigByConfigId(Integer companyId, String configId) {
        CompanyConfig config = new CompanyConfig();
        config.setCompanyId(companyId);
        config.setConfigId(configId);
        return getMapper().selectOne(config);
    }
}
