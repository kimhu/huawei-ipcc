package com.eiisys.ipcc.bean.huawei;

public enum CallBillDataTypeEnum {
    call, record, call_record;

    public static String getDataType(int type) {
        String result;
        switch (type) {
        case 0:
            result = String.valueOf(call);
            break;
        case 1:
            result = String.valueOf(record);
            break;
        case 2:
            result = String.valueOf(call_record);
            break;
        default:
            result = String.valueOf(call);
            break;
        }
        return result;
    }
}
