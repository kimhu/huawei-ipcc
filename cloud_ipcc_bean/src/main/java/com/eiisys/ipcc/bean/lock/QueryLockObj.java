package com.eiisys.ipcc.bean.lock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryLockObj {

    public static final int STS_NOT_LOCK = 0;
    public static final int STS_IS_LOCK = 1;
    
    /**
     * 获得分布式锁状态 <br>
     * 0: 未获得锁  1：获得锁
     */
    private Integer lockStatus = STS_NOT_LOCK;
    
    /**
     * 未获得锁情况下，尝试重新获得redis数据对象
     * <p>STS_NOT_LOCK的情况下才设值返回
     */
    private Object queryResult;
}
