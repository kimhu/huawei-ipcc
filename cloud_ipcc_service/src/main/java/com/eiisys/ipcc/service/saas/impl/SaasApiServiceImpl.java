package com.eiisys.ipcc.service.saas.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eiisys.ipcc.core.utils.OkhttpClientUtils;
import com.eiisys.ipcc.service.saas.SaasApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * saas api接口实现类
 * 
 * @author hujm
 */

@Service
@Slf4j
public class SaasApiServiceImpl implements SaasApiService {
    @Value("${saas.53kf.token}")
    private String saasToken;
    
    @Value("${saas.53kf.login.url}")
    private String loginURL;
    
    @Value("${saas.53kf.userinfo.url}")
    private String userInfoURL;
    
    @Value("${saas.53kf.order.url}")
    private String orderURL;

    /**
     * 3.1.4 无页面的支付接口
     * 
     * @param account 账号
     * @param id6d 工号
     * @param payId6d 支付工号
     * @param companyId 公司id
     * @param productKey 产品键值
     * @param mealKey 套餐键值
     * @param type 订单类型（1是销售，2是销售退货）
     * @param amount 订单数量（单位月）
     * @param unit 单位（1到期时间，2条数，3时间，4一次性金额，5按产品计算）
     * @throws Exception
     */
    @Override
    public String payOrder(String account, Integer id6d, Integer payId6d, Integer companyId, String productKey,
            String mealKey, String type, String amount, String unit) throws Exception {
        /** 请求头部信息 **/
        Map<String, String> headers = getHeaders();

        /** 请求body参数 **/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmd", "renew_order");
        params.put("account_id", account);
        params.put("id6d", id6d);
        params.put("payid6d", payId6d);
        params.put("company_id", companyId);
        params.put("paycompany_id", companyId);
        params.put("product_id", productKey);
        params.put("meal_key", mealKey);
        params.put("order_type", type);
        params.put("order_amount", amount);
        params.put("order_remarks", "callcenter");
        params.put("product_unit", unit);
        params.put("53kf_token", saasToken);
        
        log.info("[Pay Order] param: {}", JSON.toJSONString(params));
        
        try {
            String result = OkhttpClientUtils.syncPost(orderURL, null, headers, params, OkhttpClientUtils.TYPE_FORM);
            log.info("[Pay Order] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Pay Order] exception: {}", e);
            throw e;
        }
    }

    /**
     * 3.2.1 用户名密码登录
     * 
     * @param userName
     * @param password
     * @throws Exception
     */
    @Override
    public String login(String userName, String password) throws Exception {
        /** 请求头部信息 **/
        Map<String, String> headers = getHeaders();

        /** 请求body参数 **/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmd", "login");
        params.put("account", userName);
        params.put("password", password);
        params.put("53kf_token", saasToken);

        log.info("[Saas Login] param: {}", JSON.toJSONString(params));

        try {
            String result = OkhttpClientUtils.syncPost(loginURL, null, headers, params, OkhttpClientUtils.TYPE_FORM);
            log.info("[Saas Login] resposne: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Saas Login] exception " + ": {}", e);
            throw e;
        }
    }

    /**
     * 3.2.3获取临时令牌
     * 
     * @param token
     * @throws Exception
     */
    @Override
    public String getOnceToken(String token) throws Exception {
        /** 请求头部信息 **/
        Map<String, String> headers = getHeaders();

        /** 请求body参数 **/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmd", "get_token");
        params.put("token", token);
        params.put("53kf_token", saasToken);

        log.info("[Get Once Token] param: {}", JSON.toJSONString(params));

        try {
            String result = OkhttpClientUtils.syncPost(loginURL, null, headers, params, OkhttpClientUtils.TYPE_FORM);
            log.info("[Get Once Token] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Once Token] exception: {}", e);
            throw e;
        }
    }

    /**
     * 3.2.5 用户名密码登录
     * 
     * @param token
     * @throws Exception
     */
    @Override
    public String tokenLogin(String token) throws Exception {
        /** 请求头部信息 **/
        Map<String, String> headers = getHeaders();

        /** 请求body参数 **/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmd", "token_login");
        params.put("once_token", token);
        params.put("53kf_token", saasToken);

        log.info("[Token Login] param: {}", JSON.toJSONString(params));

        try {
            String result = OkhttpClientUtils.syncPost(loginURL, null, headers, params, OkhttpClientUtils.TYPE_FORM);
            log.info("[Token Login] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Token Login] exception: {}", e);
            throw e;
        }
    }

    /**
     * 3.3.12获取工号信息
     * 
     * @param id6a
     * @param companyId
     * @throws Exception
     */
    @Override
    public String getUserInfo(Integer id6d, Integer companyId) throws Exception {
        /** 请求头部信息 **/
        Map<String, String> headers = getHeaders();

        /** 请求body参数 **/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmd", "worker_info");
        params.put("id6d", id6d);
        params.put("company_id", companyId);
        params.put("53kf_token", saasToken);

        log.info("[Saas User Info] param: {}", JSON.toJSONString(params));

        try {
            String result = OkhttpClientUtils.syncPost(userInfoURL, null, headers, params, OkhttpClientUtils.TYPE_FORM);
            log.info("[Saas User Info] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Saas User Info] exception: {}", e);
            throw e;
        }
    }

    /**
     * 3.3.13 获取工号列表
     * 
     * @param companyId
     * @throws Exception
     */
    @Override
    public String getUserList(String companyId) throws Exception {
        /** 请求头部信息 **/
        Map<String, String> headers = getHeaders();

        /** 请求body参数 **/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmd", "worker_list");
        params.put("company_id", companyId);
        params.put("53kf_token", saasToken);
        
        log.info("[Get User List] param: {}", JSON.toJSONString(params));
        
        try {
            String result = OkhttpClientUtils.syncPost(userInfoURL, null, headers, params, OkhttpClientUtils.TYPE_FORM);
            log.info("[Get User List] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get User List] exception: {}", e);
            throw e;
        }
    }

    /**
     * 封装头部信息
     */
    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("strictSSL", "false");
        headers.put("rejectUnauthorized", "false");
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept", "*/*");

        return headers;
    }
}
