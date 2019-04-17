package com.eiisys.ipcc.bean.huawei;

/**
 * 华为状态
 * 
 * @author hujm
 */
public enum AgentGatewayEnum {
    /**2:签出 3:示忙 4:空闲 5:工作态 7:通话态 8:休息状态 **/
    Logout, Busy, Idle, Working, Calling, Rest;
    
    public static String getStatus(int gateWay) {
        String result;
        switch (gateWay) {
        case 2:
            result = String.valueOf(Logout);
            break;
        case 3:
            result = String.valueOf(Busy);
            break;
        case 4:
            result = String.valueOf(Idle);
            break;
        case 5:
            result = String.valueOf(Working);
            break;
        case 7:
            result = String.valueOf(Calling);
            break;
        case 8:
            result = String.valueOf(Rest);
            break;
        default:
            result = String.valueOf(Idle);
            break;
        }
        return result;
    }
}
