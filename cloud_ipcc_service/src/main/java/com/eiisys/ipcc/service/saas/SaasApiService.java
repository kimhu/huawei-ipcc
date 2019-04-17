package com.eiisys.ipcc.service.saas;

/**
 * saas api接口
 * 
 * @author hujm
 */
public interface SaasApiService {
    /** 3.1.4 无页面的支付接口 **/
    public String payOrder(String account, Integer id6d, Integer payId6d, Integer companyId, String productKey,
            String mealKey, String type, String amount, String unit) throws Exception;

    /** 3.2.1 用户名密码登录 **/
    public String login(String userName, String password) throws Exception;

    /** 3.2.3 获取临时令牌 **/
    public String getOnceToken(String token) throws Exception;

    /** 3.2.5 用户名密码登录 **/
    public String tokenLogin(String token) throws Exception;

    /** 3.3.12 获取工号信息 **/
    public String getUserInfo(Integer id6d, Integer companyId) throws Exception;

    /** 3.3.13 获取工号列表 **/
    public String getUserList(String companyId) throws Exception;
}
