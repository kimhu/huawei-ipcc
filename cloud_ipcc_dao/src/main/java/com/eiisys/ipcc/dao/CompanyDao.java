package com.eiisys.ipcc.dao;

import com.eiisys.ipcc.entity.Company;

public interface CompanyDao extends BaseDao<Company> {
    public Company getCompanyByCompanyId(Integer companyId);
    
    public Company getCompanyByCorpId(String corpId);
    
    public int updateCompany(Company company);
}
