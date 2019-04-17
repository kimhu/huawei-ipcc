package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountBaseRequest {
    private AccountBaseRequestHeader request;
    private Object msgBody;
}