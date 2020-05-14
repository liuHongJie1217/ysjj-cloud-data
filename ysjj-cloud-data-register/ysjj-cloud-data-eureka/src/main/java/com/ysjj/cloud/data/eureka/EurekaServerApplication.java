package com.ysjj.cloud.data.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Description: EurekaServerApplication <br>
 * @Date: 2020/5/13 9:44 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
