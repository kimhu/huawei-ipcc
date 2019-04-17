package com.eiisys.ipcc.service;

import com.eiisys.ipcc.exception.IpccException;

/**
 * 语音通话接口
 * 
 * @author hujm
 */

public interface VoiceCallService {
    public void callOut(Integer id6d, String callee) throws IpccException;
}
