package com.ysjj.cloud.data.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description: 网关启动类 <br>
 * @Date: 2020/5/13 15:35 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
