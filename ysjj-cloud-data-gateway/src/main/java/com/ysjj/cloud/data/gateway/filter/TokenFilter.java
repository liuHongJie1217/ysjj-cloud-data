package com.ysjj.cloud.data.gateway.filter;

import com.ysjj.cloud.data.common.util.JwtTokenUtil;
import com.ysjj.cloud.data.common.util.RedisUtil;
import com.ysjj.cloud.data.gateway.configuration.InterceptUrlConfiguration;
import com.ysjj.cloud.data.gateway.feign.HasAclServiceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: 全局过滤器 JWT 校验 <br>
 * @Date: 2020/5/13 17:51 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Component
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    // JWT 失效时间小于60分钟，更新JWT
    private final static Long EXPIREDJWT = 3600L;
    //redis 30 分钟会话失效时间
    private final static Long EXPIREDREDIS = 1800L;
    @Autowired
    private InterceptUrlConfiguration interceptUrlConfiguration;
    @Autowired
    private HasAclServiceFeign hasAclServiceFeign;
    @Value("${momo.modifyAdministratorAccount}")
    private boolean modifyAdministratorAccount;
    @Value("${momo.modifyAdministratorAccountUrl}")
    private String modifyAdministratorAccountUrl;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
