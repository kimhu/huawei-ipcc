package com.eiisys.ipcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@Component("com.eiisys.**")
@Slf4j
public class IpccWebApp {
    public static void main(String[] args) {
        SpringApplication.run(IpccWebApp.class, args);
        log.debug("this is the main method in IPCCWebAPP");
    }
}
