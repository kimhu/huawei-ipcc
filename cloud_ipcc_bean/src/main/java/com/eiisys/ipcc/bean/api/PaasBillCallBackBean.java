package com.eiisys.ipcc.bean.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 生成话单和录音索引回调bean
 * 
 * @author hujm
 */

@Getter
@Setter
public class PaasBillCallBackBean {
    /** 回调内容 **/
    private Object msgBody;
}
