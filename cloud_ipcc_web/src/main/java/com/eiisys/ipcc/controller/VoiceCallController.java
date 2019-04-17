package com.eiisys.ipcc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eiisys.ipcc.bean.GenericResponse;
import com.eiisys.ipcc.bean.LoginRequestTokenBean;
import com.eiisys.ipcc.bean.VoiceRequestCalloutBean;
import com.eiisys.ipcc.constants.IpccConstants;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.exception.IpccException;
import com.eiisys.ipcc.service.VoiceCallService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 语音电话Controller
 * 
 * @author hujm
 */

@Api(tags = { "前台接口：语音电话API" }, description = "语音电话呼入呼出转接插入")

@RestController
@Slf4j
public class VoiceCallController {
    @Autowired
    private VoiceCallService voiceService;
    
    @ApiOperation(value = "语音电话呼出", notes = "语音电话呼出", response = GenericResponse.class)
    @PostMapping(path = "voiceCall/callOut")
    public GenericResponse callOut(HttpServletRequest request, @RequestBody VoiceRequestCalloutBean bean) {
        GenericResponse response = null;
        
        Integer id6d = (Integer) request.getSession().getAttribute(IpccConstants.IPCC_SESSION_ID6D);
        
        /*if (null == id6d) {
            log.error("[Voice Callout Controller] session timeout to callout");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_NOT_LOGIN);
            return response;
        }*/
        
        if (null == bean) {
            log.error("[Voice Callout Controller] param is invalid");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "");
            return response;
        }
        
        if (null == bean.getCallee()) {
            log.error("[Voice Callout Controller] param callee is invalid");
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PARAM_IS_NULL, "被叫号码");
            return response;
        }
        
        id6d = 10103861;
        try {
            voiceService.callOut(id6d, bean.getCallee());
            response = ResponseUtils.genSuccessResponse(null);
        } catch (IpccException e) {
            response = ResponseUtils.genErrorResponse(e);
        }catch (Exception e) {
            log.error("[Callout Controller] callout exception: {}", e);
            response = ResponseUtils.genErrorResponse(MsgConstants.MSG_PAAS_CALLOUT_FAILED);
        }
        return response;
    }
}
