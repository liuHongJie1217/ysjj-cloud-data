package com.system.core.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 用户中心启动类 <br>
 * @Date: 2020/5/18 15:06 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
