package com.eiisys.ipcc.bean;

import lombok.Setter;

import java.io.Serializable;

import lombok.Getter;

@Getter
@Setter
public class IpccDemoBean implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;
    
    private String demoName;
}
