package com.eiisys.ipcc.saas.service;

import com.eiisys.ipcc.bean.saas.SaasOpenProductBean;
import com.eiisys.ipcc.entity.Company;
import com.eiisys.ipcc.entity.User;

public interface SaasProductOpenService {
    public String generateToken(SaasOpenProductBean bean);
    
    public int initCompany(Company company, User user, boolean isHuawei) throws Exception;
    
    public int initNormalUser(User user) throws Exception;
    
    public int openProduct(SaasOpenProductBean bean) throws Exception;
}
