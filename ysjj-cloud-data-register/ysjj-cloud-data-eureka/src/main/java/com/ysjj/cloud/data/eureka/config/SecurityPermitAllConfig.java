package com.ysjj.cloud.data.eureka.config;

import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Description: Security 配置类 <br>
 * @Date: 2020/5/13 10:43 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Configuration
public class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.csrf().disable();
        super.configure(security);
    }
}
