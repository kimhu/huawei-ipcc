package com.eiisys.ipcc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 坐席状态切换
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentStatusChangeBean {
    /** 需要切换的状态 **/
    private Integer status;
}
