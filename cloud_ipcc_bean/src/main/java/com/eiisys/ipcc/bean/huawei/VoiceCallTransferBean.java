package com.eiisys.ipcc.bean.huawei;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author hujm
 *
 */

@Getter
@Setter
public class VoiceCallTransferBean {
    /** 座席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席鉴权信息 **/
    @JSONField(serialize = false)
    private String guid;
    /** 转移设备类型，1：技能队列，2：业务代表，3：IVR设备，4：系统接入码，5：外部号码 **/
    private Integer devicetype;
    /** 转移地址，即转移设备类型对应的设备ID。最大长度24字节 **/
    private String address;
    /**
     * 转移模式，在内部转移的情况下：释放转为0，挂起转为1，成功转为2，指定转为3，合并转为4；
     * 在转外部号码的情况下，释放转为1，成功转为2，通话转为3，三方转为4
     **/
    private Integer mode;
    /** 需设置的随路数据。内容可为空，最大长度为1024字节 **/
    private String callappdata;
    /** 主叫号码 **/
    private String caller;
    /** 媒体能力。（默认为0。0：音频，1：视频） **/
    private Integer mediaability;
}
