package com.eiisys.ipcc.dao;

import java.util.List;

import com.eiisys.ipcc.dao.BaseDao;
import com.eiisys.ipcc.entity.IpccDemo;

public interface DemoDao extends BaseDao<IpccDemo>{

    public List<IpccDemo> selectAllData() ;
}
