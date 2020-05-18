package com.core.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: web端启动类 <br>
 * @Date: 2020/5/18 12:00 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.ysjj.feign"})
public class SystemCoreWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemCoreWebApplication.class, args);
    }
}
