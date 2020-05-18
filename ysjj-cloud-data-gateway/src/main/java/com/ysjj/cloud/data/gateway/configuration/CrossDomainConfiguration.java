package com.ysjj.cloud.data.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @Description: 设置自定义 response headers <br>
 * @Date: 2020/5/18 11:10 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Configuration
public class CrossDomainConfiguration {
    public static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,userInfo,x-token,token,client";
    public static final String ALLOWED_METHODS = "POST, GET, OPTIONS,DELETE,PUT";
    public static final String ALLOWED_ORIGIN = "*";
    public static final String ALLOWED_Expose = "*";
    public static final String MAX_AGE = "96400L";
//    private static final String MAX_AGE = "60*60*24L";

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // cookie跨域
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedMethod(CorsConfiguration.ALL);
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.addAllowedHeader(CorsConfiguration.ALL);
        // 配置前端js允许访问的自定义响应头
        config.addExposedHeader("x-token");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

}
