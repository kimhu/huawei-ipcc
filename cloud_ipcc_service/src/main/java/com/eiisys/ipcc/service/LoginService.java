package com.eiisys.ipcc.service;

import com.eiisys.ipcc.bean.LoginResponseBean;
import com.eiisys.ipcc.bean.LoginRequestTokenBean;
import com.eiisys.ipcc.exception.IpccException;

/**
 * 登录接口
 * 
 * @author hujm
 */
public interface LoginService {
    /** token登录 **/
    public LoginResponseBean tokenLogin(LoginRequestTokenBean bean) throws IpccException;

    /** 用户名密码登录 **/
    public LoginResponseBean login(String userName, String password) throws IpccException;

    /** 坐席登录 **/
    public void paasLogin(Integer id6d) throws IpccException;

    /** 坐席签出 **/
    public void paasLogout(Integer id6d) throws IpccException;

    /** 坐席心跳检测 **/
    public void paasHeartbeat(Integer id6d) throws IpccException;
    
    /** 坐席状态切换 **/
    public void paasStatusChange(Integer id6d, Integer status) throws IpccException;
}
