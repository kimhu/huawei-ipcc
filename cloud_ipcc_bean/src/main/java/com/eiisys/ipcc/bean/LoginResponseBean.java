package com.eiisys.ipcc.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录返回页面实体
 * 
 * @author hujm
 */

@Getter
@Setter
public class LoginResponseBean {
    /** 用户id6d **/
    private Integer id6d;
    /** 是否Admin **/
    private boolean isAdmin;
    /** 用户名字 **/
    private String userName;
    /** 所属分组 **/
    private String groupName;
    /** 坐席等级 **/
    private String roleName;
    /** 权限列表 **/
    private List<String> codeList;
    /** 性别 **/
    private Integer gender;
    /** 头像路径 **/
    private String imagePath;
    /** 呼叫转移号码 **/
    private String phone;
    /** 公司id **/
    private Integer companyId;
    /** 分组id **/
    private Short groupId;
    /** 账号 **/
    private String userId;
    /** 身份标识 **/
    private LoginResponseIndefierBean indefier;
    /** 其他配置 **/
    private LoginResponseConfigBean configs;
    /** 请求requestToken **/
    private String requestToken;
}
