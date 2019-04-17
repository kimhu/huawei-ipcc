package com.eiisys.ipcc.dao.impl;



import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.eiisys.core.mapper.BaseMapper;
import com.eiisys.ipcc.dao.BaseDao;

/*******************************
 * * 版权所有：快服科技
 * * 创建日期: 2017/12/22 13:07
 * * 创建作者: Kevin_Ge
 * * 版本:  1.0
 * * 功能:
 * * 最后修改时间:
 * * 修改记录:
 ********************************/
public abstract class BaseDaoImpl<Pojo> implements BaseDao<Pojo> {

   protected abstract BaseMapper<Pojo> getMapper();

    public int deleteByExample(Object o) {
        return getMapper().deleteByExample(o);
    }

    public int deleteByPrimaryKey(Object o) {
        return getMapper().deleteByPrimaryKey(o);
    }

    public int delete(Pojo pojo) {
        return getMapper().delete(pojo);
    }

    public boolean existsWithPrimaryKey(Object o) {
        return getMapper().existsWithPrimaryKey(o);
    }

    public int insertList(List<Pojo> list) {
        return getMapper().insertList(list);
    }

    public int insert(Pojo pojo) {
        return getMapper().insert(pojo);
    }

    public int insertSelective(Pojo pojo) {
        return getMapper().insertSelective(pojo);
    }

    public int insertUseGeneratedKeys(Pojo pojo) {
        return getMapper().insertUseGeneratedKeys(pojo);
    }

    public List<Pojo> selectAll() {
        return getMapper().selectAll();
    }

    public List<Pojo> selectByExample(Object o) {
        return getMapper().selectByExample(o);
    }

    public List<Pojo> selectByExampleAndRowBounds(Object o, RowBounds rowBounds) {
        return getMapper().selectByExampleAndRowBounds(o, rowBounds);
    }

    public Pojo selectByPrimaryKey(Object o) {
        return getMapper().selectByPrimaryKey(o);
    }

    public int selectCountByExample(Object o) {
        return getMapper().selectCountByExample(o);
    }

    public int selectCount(Pojo pojo) {
        return getMapper().selectCount(pojo);
    }

    public List<Pojo> select(Pojo pojo) {
        return getMapper().select(pojo);
    }

    public Pojo selectOne(Pojo pojo) {
        return getMapper().selectOne(pojo);
    }

    public List<Pojo> selectByRowBounds(Pojo pojo, RowBounds rowBounds) {
        return getMapper().selectByRowBounds(pojo, rowBounds);
    }

    public int updateByExample(Pojo pojo, Object o) {
        return getMapper().updateByExample(pojo, o);
    }

    public int updateByExampleSelective(Pojo pojo, Object o) {
        return getMapper().updateByExampleSelective(pojo, o);
    }

    public int updateByPrimaryKey(Pojo pojo) {
        return getMapper().updateByPrimaryKey(pojo);
    }

    public int updateByPrimaryKeySelective(Pojo pojo) {
        return getMapper().updateByPrimaryKeySelective(pojo);
    }

//
}
