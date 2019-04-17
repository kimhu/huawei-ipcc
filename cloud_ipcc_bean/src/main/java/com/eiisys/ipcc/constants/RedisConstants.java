package com.eiisys.ipcc.constants;

/**
 * Redis缓存相关常量类
 */
public class RedisConstants {

    /** 分布式锁Key过期时间 单位：秒 */
    public static long LOCK_EXPIRE_TIME = 15L;
    
    /** 项目redis key前缀 */
    public static final String RKEY_PRJ_PRE = "ContactCenter:";
    
    /** Demo信息key 后接 id */
    public static final String RKEY_DEMO_LIST = RKEY_PRJ_PRE+"demo_list:";
    
    /** Demo信息排他锁key 后接 名称 */
    public static final String RKEY_DEMO_INSERT_LOCK = RKEY_PRJ_PRE+"demo_insert_lock:";
    /** Demo信息排他锁key 后接 id */
    public static final String RKEY_DEMO_LIST_LOCK = RKEY_PRJ_PRE+"demo_list_lock:";
    
    /** 解锁脚本 */
    public static final String LOCK_RELEASE_SCRIPT = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    /** 微信接入码 **/
    public static final String WECHAT_ACCESS_TOKEN = RKEY_PRJ_PRE + "wechat_token:";
    
    /** 坐席登录生成的guid 后接id6d */
    public static final String RKEY_AGENT_GUID = RKEY_PRJ_PRE + "guid:";
    /** 坐席工号 后接id6d */
    public static final String RKEY_AGNET_ID = RKEY_PRJ_PRE + "agent_id:";
    
    /** 用户信息缓存key 后接id6d */
    public static final String RKEY_USER_REDIS = RKEY_PRJ_PRE + "user:";
    /** 用户信息获取排他锁key 后接id6d */
    public static final String RKEY_USER_GET_LOCK = RKEY_PRJ_PRE + "user_get_lock:";
    /** 用户更新排他锁key 后接id6d */
    public static final String REEY_USER_UPDATE_LOCK = RKEY_PRJ_PRE + "user_update_lock";
    
    /** 公司信息缓存key 后接companyId */
    public static final String RKEY_COMPANY_REDIS = RKEY_PRJ_PRE + "company:";
    /** 公司信息获取排他锁key 后接companyId */
    public static final String RKEY_COMPANY_GET_LOCK = RKEY_PRJ_PRE + "company_get_lock:";
    /** 公司更新排他锁key 后接companyId */
    public static final String REEY_COMPANY_UPDATE_LOCK = RKEY_PRJ_PRE + "company_update_lock";
}
