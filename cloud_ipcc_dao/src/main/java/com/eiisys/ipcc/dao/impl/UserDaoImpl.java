package com.eiisys.ipcc.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eiisys.core.mapper.BaseMapper;
import com.eiisys.ipcc.dao.UserDao;
import com.eiisys.ipcc.entity.User;
import com.eiisys.ipcc.mapper.UserMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{
    @Autowired
    private UserMapper userMapper;
    
    @Override
    protected BaseMapper<User> getMapper() {
        return userMapper;
    }

    @Override
    public User getUserById6d(Integer id6d) {
        User user = new User();
        user.setId6d(id6d);
        return getMapper().selectOne(user);
    }

    @Override
    public int updateUser(User user) {
        Example example = new Example(User.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id6d", user.getId6d());
        return getMapper().updateByExampleSelective(user, example);
    }
}
