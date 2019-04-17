package com.eiisys.ipcc.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.eiisys.ipcc.bean.GenericResponse;
import com.eiisys.ipcc.constants.IpccConstants;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.service.saas.SaasApiService;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private SaasApiService saasService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        System.out.println(session.getId());
        Integer id6d = (Integer) session.getAttribute(IpccConstants.IPCC_SESSION_ID6D);
        String token = (String) session.getAttribute(IpccConstants.IPCC_SESSION_TOKEN);
        String sToken = request.getHeader(IpccConstants.IPCC_ACCESS_TOKEN);
        
        /**测试 begin**/
        return true;
        /**测试 end**/
        
        /*if (id6d != null && !StringUtils.isEmpty(token) && token.equals(sToken)) {
            return true;
        }
        
        if (StringUtils.isEmpty(sToken)) {
            GenericResponse genericResponse = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(genericResponse));
            return false;
        } 
        
        *//** 去saas平台验证token **//*
        String onceToken = saasService.getOnceToken(sToken);
        if (StringUtils.isEmpty(onceToken)) {
            GenericResponse genericResponse = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(genericResponse));
            return false;
        } 
        
        *//** 获取登录用户id6d **//*
        String tokenLogin = saasService.tokenLogin(onceToken);
        if (StringUtils.isEmpty(tokenLogin)) {
            GenericResponse genericResponse = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(genericResponse));
            return false;
        }
        
        JSONObject object = JSONObject.parseObject(tokenLogin);
        if (object.getJSONObject("server_response") == null ||
                object.getJSONObject("server_response").getInteger("status_code") == null || 
                object.getJSONObject("server_response").getIntValue("status_code") != 201 ||
                object.getJSONObject("server_response").getInteger("id6d") == null) {
            GenericResponse genericResponse = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(genericResponse));
            return false;
        }
        
        id6d = object.getJSONObject("server_response").getInteger("id6d");
        session.setAttribute("id6d", id6d);
        session.setAttribute("token", sToken);
        return false;*/
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
