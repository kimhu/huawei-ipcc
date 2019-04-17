package com.eiisys.ipcc.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.eiisys.ipcc.bean.GenericResponse;
import com.eiisys.ipcc.bean.LoginRequestBean;
import com.eiisys.ipcc.bean.AgentStatusChangeBean;
import com.eiisys.ipcc.bean.LoginRequestPaasBean;
import com.eiisys.ipcc.bean.LoginResponseBean;
import com.eiisys.ipcc.bean.LoginRequestTokenBean;
import com.eiisys.ipcc.constants.IpccConstants;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.exception.IpccException;
import com.eiisys.ipcc.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录Controller
 * 
 * @author hujm
 */

@Api(tags = {
              "前台接口：登录API" }, description = "账号登录和坐席登录")

@RestController
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "token登录", notes = "token登录", response = GenericResponse.class)
    @PostMapping(path = "tokenLogin")
    public GenericResponse tokenLogin(@RequestBody LoginRequestTokenBean bean) {
        log.info("[Token Login Controller] param: {}", JSON.toJSONString(bean));

        GenericResponse response = null;

        if (null == bean || StringUtils.isEmpty(bean.getToken())) {
            log.error("[Token Login Controller] param token is ivalid");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "token");
            return response;
        }

        try {
            LoginResponseBean responseBean = loginService.tokenLogin(bean);
            response = ResponseUtils.genSuccessResponse(null);
            response.setData(responseBean);
        } catch (IpccException e) {
            response = ResponseUtils.genErrorResponse(e);
        } catch (Exception e) {
            log.error("[Login Controller] exception: {}", e);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_LOGIN_FAILED);
        }
        return response;
    }

    @ApiOperation(value = "用户名密码登录", notes = "用户名密码登录", response = GenericResponse.class)
    @PostMapping(path = "login")
    public GenericResponse login(HttpServletRequest request, @RequestBody LoginRequestBean bean) {
        log.info("[Login Controller] param: {}", JSON.toJSONString(bean));

        GenericResponse response = null;

        if (null == bean) {
            log.error("[Login Controller] no param to login");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "用户名密码");
            return response;
        }

        if (StringUtils.isEmpty(bean.getUserName())) {
            log.error("[Login Controller] param username is ivalid");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "用户名");
            return response;
        }

        if (StringUtils.isEmpty(bean.getPassword())) {
            log.error("[Login Controller] param password is invalid");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "密码");
            return response;
        }

        request.getSession().setAttribute(IpccConstants.IPCC_SESSION_ID6D, 10103861);
        try {
            LoginResponseBean responseBean = loginService.login(bean.getUserName(), bean.getPassword());
            response = ResponseUtils.genSuccessResponse(null);
            response.setData(responseBean);
            request.getSession().setAttribute(IpccConstants.IPCC_SESSION_ID6D, responseBean.getId6d());
        } catch (IpccException e) {
            response = ResponseUtils.genErrorResponse(e);
        } catch (Exception e) {
            log.error("[Logout Controller] exception: {}", e);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_LOGIN_FAILED);
        }
        response.setCode("0");
        return response;
    }
    
    @ApiOperation(value = "账号签出", notes = "账号签出", response = GenericResponse.class)
    @PostMapping(path = "logout")
    public GenericResponse paasLogin(HttpServletRequest request) {
        GenericResponse response = null;
        
        Integer id6d = (Integer) request.getSession().getAttribute(IpccConstants.IPCC_SESSION_ID6D);
        if (null == id6d) {
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
        } else {
            request.getSession().invalidate();
            response = ResponseUtils.genSuccessResponse(null);
        }
        return response;
    }

    @ApiOperation(value = "华为坐席登录", notes = "华为坐席登录", response = GenericResponse.class)
    @PostMapping(path = "agent/login")
    public GenericResponse paasLogin(@RequestBody LoginRequestPaasBean bean) {
        log.info("[Paas Login Controller] param: {}", JSON.toJSONString(bean));

        GenericResponse response = null;

        if (null == bean) {
            log.error("[Paas Login Controller] no param to login paas");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "");
            return response;
        }

        if (null == bean.getId6d()) {
            log.error("[Paas Login Controller] param id6d is ivalid");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "工号");
            return response;
        }

        try {
            loginService.paasLogin(bean.getId6d());
            response = ResponseUtils.genSuccessResponse(null);
        } catch (IpccException e) {
            response = ResponseUtils.genErrorResponse(e);
        } catch (Exception e) {
            log.error("[Paas login Controller] exception: {}", e);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PAAS_LOGIN_FAILED);
        }
        return response;
    }

    @ApiOperation(value = "华为坐席登录", notes = "华为坐席登录", response = GenericResponse.class)
    @PostMapping(path = "agent/logout")
    public GenericResponse paasLogout(HttpServletRequest request) {
        Integer id6d = (Integer) request.getSession().getAttribute(IpccConstants.IPCC_SESSION_ID6D);

        log.info("[Paas Logout Controller] param: {}", id6d);

        GenericResponse response = null;

        if (null == id6d) {
            log.error("[Paas Logout Controller] session timeout for id6d {}", id6d);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
            return response;
        }

        try {
            loginService.paasLogout(id6d);
            response = ResponseUtils.genSuccessResponse(null);
        } catch (IpccException e) {
            response = ResponseUtils.genErrorResponse(e);
        } catch (Exception e) {
            log.error("[Paas Logout Controller] exception: {}", e);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PAAS_LOGOUT_FAILED);
        }
        return response;
    }

    @ApiOperation(value = "华为坐席心跳检测", notes = "华为坐席心跳检测", response = GenericResponse.class)
    @PostMapping(path = "agent/heartbeat")
    public GenericResponse paasHeartbeat(HttpServletRequest request) {
        Integer id6d = (Integer) request.getSession().getAttribute(IpccConstants.IPCC_SESSION_ID6D);

        log.info("[Paas Heartbeat Controller] param: {}", id6d);

        GenericResponse response = null;

        if (null == id6d) {
            log.error("[Paas Logout Controller] session timeout for id6d {}", id6d);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
            return response;
        }

        try {
            loginService.paasHeartbeat(id6d);
            response = ResponseUtils.genSuccessResponse(null);
        } catch (IpccException e) {
            response = ResponseUtils.genErrorResponse(e);
        } catch (Exception e) {
            log.error("[Paas Heartbeat Controller] exception: {}", e);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PAAS_HEARTBEAT_ERROR);
        }
        return response;
    }

    @ApiOperation(value = "华为坐席状态切换", notes = "华为坐席状态切换", response = GenericResponse.class)
    @PostMapping(path = "agent/changeStatus")
    public GenericResponse paasChangeStatus(HttpServletRequest request, @RequestBody AgentStatusChangeBean bean) {
        Integer id6d = (Integer) request.getSession().getAttribute(IpccConstants.IPCC_SESSION_ID6D);

        log.info("[Paas Status Change Controller] param: {}", JSON.toJSONString(bean));

        GenericResponse response = null;

        if (null == id6d) {
            log.error("[Paas Logout Controller] session timeout for id6d {}", id6d);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
        }

        if (null == bean) {
            log.error("[Paas Status Change Controller] no param to logout paas");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "状态、工号、坐席");
            return response;
        }

        if (null == bean.getStatus()) {
            log.error("[Paas Status Change Controller] param status is ivalid");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "坐席状态");
            return response;
        }

        try {
            loginService.paasStatusChange(id6d, bean.getStatus());
            response = ResponseUtils.genSuccessResponse(null);
        } catch (IpccException e) {
            response = ResponseUtils.genErrorResponse(e);
        } catch (Exception e) {
            log.error("[Paas Status Change Controller] exception: {}", e);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PAAS_STATUS_CHANGE_FAILED);
        }
        return response;
    }
}
