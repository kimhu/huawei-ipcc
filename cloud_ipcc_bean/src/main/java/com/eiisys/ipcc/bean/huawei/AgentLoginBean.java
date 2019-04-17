package com.eiisys.ipcc.bean.huawei;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hujm 
 * 
 * 2.1.1 登录
 */

@Getter
@Setter
public class AgentLoginBean {
    /** 坐席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席密码（最大长度为32位字符） **/
    private String password;
    /** 座席电话（最大长度为24位字符） **/
    private String phonenum;
    /** 是否自动应答，默认为true（只对语音呼叫生效） **/
    private Boolean autoanswer;
    /** 通话结束后是否自动进入空闲态，默认为true **/
    private Boolean autoenteridle;
    /** 座席挂机后是否进入非长通态，默认为true **/
    private Boolean releasephone;
    /** 登录后的状态，默认为空闲态。4：空闲 5：工作态 **/
    private Integer status;
    /** 是否登录Webm媒体服务器（处理点击通话、文字 交谈、回呼请求三种呼叫类型的业务时需要登 录）。默认为true。 **/
    private Boolean checkInWebm;
    /** 接收事件的URL（最大长度为512位字符） **/
    private String pushUrl;
}