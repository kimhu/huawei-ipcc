package com.eiisys.core.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;



public interface BaseMapper<Pojo> extends Mapper<Pojo>, MySqlMapper<Pojo> {
}
