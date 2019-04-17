package com.eiisys.ipcc.service;

import com.eiisys.ipcc.entity.User;

public interface UserService {
    public User getUserById6d(Integer id6d);
    
    public int updateUser(User user);
}
