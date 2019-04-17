package com.eiisys.ipcc.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eiisys.core.mapper.BaseMapper;
import com.eiisys.ipcc.dao.DemoDao;
import com.eiisys.ipcc.entity.IpccDemo;
import com.eiisys.ipcc.mapper.IpccDemoMapper;

@Repository
public class DemoDaoImpl extends BaseDaoImpl<IpccDemo> implements DemoDao {

    @Autowired
    private IpccDemoMapper ipccDemoMapper;
    
    @Override
    protected BaseMapper<IpccDemo> getMapper() {
        return ipccDemoMapper;
    }

    @Override
    public List<IpccDemo> selectAllData() {
        return ipccDemoMapper.selectAll();
    }

    
}
