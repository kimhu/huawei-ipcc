package com.eiisys.ipcc.dao;

import com.eiisys.ipcc.entity.User;

public interface UserDao extends BaseDao<User> {
    public User getUserById6d(Integer id6d);
    
    public int updateUser(User user);
}
