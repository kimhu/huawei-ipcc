package com.eiisys.ipcc.interceptor;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eiisys.ipcc.bean.huawei.AgentForceLogoutBean;
import com.eiisys.ipcc.bean.huawei.AgentLogoutBean;
import com.eiisys.ipcc.constants.IpccConstants;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.entity.User;
import com.eiisys.ipcc.service.LoginService;
import com.eiisys.ipcc.service.UserService;
import com.eiisys.ipcc.service.paas.AgentApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * session过期处理
 * 
 * @author hujm
 */

@Component
@WebListener
@Slf4j
public class SessionListener implements HttpSessionListener {
    @Autowired
    private RedisClient<String> redisClient;
    
    @Autowired
    private AgentApiService agentService;

    @Override
    public void sessionCreated(HttpSessionEvent event) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        Integer id6d = (Integer) session.getAttribute(IpccConstants.IPCC_SESSION_ID6D);
        
        String redisGuidKey = RedisConstants.RKEY_AGENT_GUID + id6d;
        String redisAgentIdKey = RedisConstants.RKEY_AGNET_ID + id6d;
        if (redisClient.hasKey(redisGuidKey) && redisClient.hasKey(redisAgentIdKey)) {
            String guid = redisClient.get(redisGuidKey);
            String agentId = redisClient.get(redisAgentIdKey);
            
            AgentForceLogoutBean bean = new AgentForceLogoutBean();
            bean.setAgentid(Integer.valueOf(agentId));
            bean.setGuid(guid);
            bean.setReason(1);
            
            try {
                agentService.forceLogout(bean);
                redisClient.delete(redisGuidKey);
                redisClient.delete(redisAgentIdKey);
            } catch (Exception e) {
               log.error("[Session Listener] agent logout exception: {}", e);
            }
        }
    }
}
