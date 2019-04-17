package com.eiisys.ipcc.dao;


import org.apache.ibatis.session.RowBounds;

import java.util.List;

/*******************************
 * * 版权所有：快服科技
 * * 创建日期: 2017/12/22 13:50
 * * 创建作者: Kevin_Ge
 * * 版本:  1.0
 * * 功能:
 * * 最后修改时间:
 * * 修改记录:
 ********************************/
public interface BaseDao<Pojo> {

    public int deleteByExample(Object o);


    public int deleteByPrimaryKey(Object o);


    public int delete(Pojo pojo);


    public boolean existsWithPrimaryKey(Object o);


    public int insertList(List<Pojo> list);


    public int insert(Pojo pojo);


    public int insertSelective(Pojo pojo);


    public int insertUseGeneratedKeys(Pojo pojo);


    public List<Pojo> selectAll();


    public List<Pojo> selectByExample(Object o);


    public List<Pojo> selectByExampleAndRowBounds(Object o, RowBounds rowBounds);


    public Pojo selectByPrimaryKey(Object o);


    public int selectCountByExample(Object o);


    public int selectCount(Pojo pojo);


    public List<Pojo> select(Pojo pojo);


    public Pojo selectOne(Pojo pojo);


    public List<Pojo> selectByRowBounds(Pojo pojo, RowBounds rowBounds);


    public int updateByExample(Pojo pojo, Object o);


    public int updateByExampleSelective(Pojo pojo, Object o);


    public int updateByPrimaryKey(Pojo pojo);


    public int updateByPrimaryKeySelective(Pojo pojo);
}
        
    

