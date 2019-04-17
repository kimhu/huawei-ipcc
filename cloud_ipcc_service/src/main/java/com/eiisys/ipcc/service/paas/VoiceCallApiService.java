package com.eiisys.ipcc.service.paas;

import com.eiisys.ipcc.bean.huawei.VoiceCallAnswerBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallConfrenceBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallConnectHoldBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallDisconnectBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallDropBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallHoldBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallHoldCancelBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallInnerBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallInnerHelpBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallMuteBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallMuteCancelBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallOutBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallReleaseBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallSecondDailBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallSnatchBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallTransferBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallTransferCancelBean;

/**
 * 座席应用接口> 语音通话
 * 
 * @author hujm
 */
public interface VoiceCallApiService {
    /** 2.2.1 外呼 **/
    public String callOut(VoiceCallOutBean bean) throws Exception;
    
    /** 2.2.2 呼叫应答 **/
    public String callAnswer(VoiceCallAnswerBean bean) throws Exception;

    /** 2.2.3 内部呼叫 **/
    public String callInner(VoiceCallInnerBean bean) throws Exception;

    /** 2.2.4 内部求助 **/
    public String callInnerHelp(VoiceCallInnerHelpBean bean) throws Exception;

    /** 2.2.5 静音 **/
    public String callMute(VoiceCallMuteBean bean) throws Exception;

    /** 2.2.6 取消静音 **/
    public String callMuteCancel(VoiceCallMuteCancelBean bean) throws Exception;
    
    /** 2.2.7 呼叫保持 **/
    public String callHold(VoiceCallHoldBean bean) throws Exception;

    /** 2.2.8 取消保持 **/
    public String callHoldCancel(VoiceCallHoldCancelBean bean) throws Exception;

    /** 2.2.9 呼叫转移 **/
    public String callTransfer(VoiceCallTransferBean bean) throws Exception;

    /** 2.2.10 取消转移 **/
    public String callTransferCancel(VoiceCallTransferCancelBean bean) throws Exception;

    /** 2.2.11 二次拨号(扩展) **/
    public String callSecondDial(VoiceCallSecondDailBean bean) throws Exception;

    /** 2.2.12 三方通话 **/
    public String callConfrence(VoiceCallConfrenceBean bean) throws Exception;

    /** 2.2.13 连接保持 **/
    public String callConnectHold(VoiceCallConnectHoldBean bean) throws Exception;

    /** 2.2.14 释放指定号码连接 **/
    public String callDisconnect(VoiceCallDisconnectBean bean) throws Exception;

    /** 2.2.15 拆除指定callid呼叫 **/
    public String callDrop(VoiceCallDropBean bean) throws Exception;

    /** 2.2.16 挂断呼叫 **/
    public String callRelease(VoiceCallReleaseBean bean) throws Exception;

    /** 2.2.17 呼叫代答 **/
    public String callSnatch(VoiceCallSnatchBean bean) throws Exception;
}
