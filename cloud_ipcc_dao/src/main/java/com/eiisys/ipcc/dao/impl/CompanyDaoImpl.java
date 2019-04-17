package com.eiisys.ipcc.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eiisys.core.mapper.BaseMapper;
import com.eiisys.ipcc.dao.CompanyDao;
import com.eiisys.ipcc.entity.Company;
import com.eiisys.ipcc.mapper.CompanyMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Repository
public class CompanyDaoImpl extends BaseDaoImpl<Company> implements CompanyDao {
    @Autowired
    private CompanyMapper companyMapper;
    
    @Override
    protected BaseMapper<Company> getMapper() {
        return companyMapper;
    }

    @Override
    public Company getCompanyByCompanyId(Integer companyId) {
        Company company = new Company();
        company.setCompanyId(companyId);
        return getMapper().selectOne(company);
    }

    @Override
    public Company getCompanyByCorpId(String corpId) {
        Company company = new Company();
        company.setCorpId(corpId);
        return getMapper().selectOne(company);
    }

    @Override
    public int updateCompany(Company company) {
        Example example = new Example(Company.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("companyId", company.getCompanyId());
        return companyMapper.updateByExampleSelective(company, example);
    }
}
