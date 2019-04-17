package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountBaseRequestHeader {
    private String version;

    public AccountBaseRequestHeader(String version) {
        super();
        this.version = version;
    }
}
