package com.eiisys.ipcc.dao;

import com.eiisys.ipcc.entity.CompanyConfig;

public interface CompanyConfigDao extends BaseDao<CompanyConfig> {
    public CompanyConfig getConfigByConfigId(Integer companyId, String configId);
}
