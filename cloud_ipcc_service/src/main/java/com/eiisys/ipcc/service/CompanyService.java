package com.eiisys.ipcc.service;

import com.eiisys.ipcc.entity.Company;

public interface CompanyService {
    public Company getCompanyByCompanyId(Integer companyId);
    
    public int updateCompany(Company company);
}
