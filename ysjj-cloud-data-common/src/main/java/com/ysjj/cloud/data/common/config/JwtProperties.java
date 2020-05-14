package com.ysjj.cloud.data.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description: JWTp配置获取信息<br>
 * @Date: 2020/5/13 18:07 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Configuration
@ConfigurationProperties(prefix = JwtProperties.JWT_PREFIX) //前缀绑定
@PropertySource(value = "classpath:config/jwt.properties", encoding = "UTF-8") //绑定配置文件
@Data
public class JwtProperties {

    public static final String JWT_PREFIX = "jwt";

    @Value("${jwt.header}")
    private String header = "Authorization";
    @Value("${jwt.secret}")
    private String secret = "mySecret";
    @Value("${jwt.expiration}")
    private Long expiration = 604800L;
    @Value("${jwt.auth-path}")
    private String authPath = "auth";
    @Value("${jwt.md5-key}")
    private String md5Key = "randomKey";
    @Value("${jwt.ignore-url}")
    private String ignoreUrl = "";

}
