package com.ysjj.cloud.data.gateway.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description: 自定义gateway filter 实现：
 * 1.超过限流返回体定义
 * 2.针对不同的接口限流不同
 * <br>
 * @Date: 2020/5/15 17:53 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Component
@Slf4j
public class RateCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<RateCheckGatewayFilterFactory.Config> implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private RateCheckRedisRateLimiter rateLimiter;

    @Override
    public GatewayFilter apply(Config config) {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    public static class Config {
        private String ketResolver; //限流Id

        public String getKetResolver() {
            return ketResolver;
        }

        public void setKetResolver(String ketResolver) {
            this.ketResolver = ketResolver;
        }
    }
}
