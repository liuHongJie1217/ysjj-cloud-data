package com.ysjj.cloud.data.gateway.limit;


import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.ysjj.cloud.data.common.common.JSONResult;
import com.ysjj.cloud.data.gateway.configuration.CrossDomainConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

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
    private KeyResolver keyResolver;
    @Value("${ysjj.openRedisLimiter}")
    private boolean openRedisLimiter;

    public RateCheckGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        this.rateLimiter = applicationContext.getBean(RateCheckRedisRateLimiter.class);
        this.keyResolver = applicationContext.getBean(config.keyResolver, KeyResolver.class);

        return (exchange, chain) -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            if (openRedisLimiter) {
                return keyResolver.resolve(exchange).flatMap(key ->
                        // TODO: if key is empty?
                        rateLimiter.isAllowed(route.getId(), key).flatMap(response -> {
                            log.info("response: " + response);
                            // TODO: set some headers for rate, tokens left
                            if (response.isAllowed()) {
                                return chain.filter(exchange);
                            }
                            //超过了限流的response返回值
                            return setRateCheckResponse(exchange);
                        }));
            } else {
                //Guava rateLimiter 单机版限流
                return keyResolver.resolve(exchange).flatMap(key -> {
                    RateLimiter rateLimiter = GuavaRateLimiter.resourceRateLimiter.get(key);
                    if (rateLimiter != null) {
                        if (GuavaRateLimiter.resourceRateLimiter.get(key).tryAcquire(500, TimeUnit.SECONDS)) {
                            return chain.filter(exchange);
                        } else {
                            return setRateCheckResponse(exchange);
                        }
                    } else {
                        GuavaRateLimiter.updateResourceRateLimiter(key, null);
                        return chain.filter(exchange);
                    }
                });
            }
        };
    }

    private Mono<? extends Void> setRateCheckResponse(ServerWebExchange exchange) {
        //超过限流的返回
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Access-Control-Allow-Origin", CrossDomainConfiguration.ALLOWED_ORIGIN);
        httpHeaders.add("Access-Control-Allow-Methods", CrossDomainConfiguration.ALLOWED_METHODS);
        httpHeaders.add("Access-Control-Max-Age", CrossDomainConfiguration.MAX_AGE);
        httpHeaders.add("Access-Control-Allow-Headers", CrossDomainConfiguration.ALLOWED_HEADERS);
        httpHeaders.add("Access-Control-Expose-Headers", CrossDomainConfiguration.ALLOWED_Expose);
        httpHeaders.add("Access-Control-Allow-Credentials", "true");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        JSONResult jsonResult = JSONResult.build(HttpStatus.TOO_MANY_REQUESTS.value(), "当前访问人数过多，请稍后再试", "当前访问人数过多，请稍后再试");
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(jsonResult).getBytes());
        log.error("--------限流中---------");
        return response.writeWith(Mono.just(dataBuffer));

    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        log.info("RateCheckGatewayFilterFactory.setApplicationContext，applicationContext=" + context);
        applicationContext = context;


    }

    public static class Config {
        private String keyResolver; //限流Id

        public String getKeyResolver() {
            return keyResolver;
        }

        public void setKeyResolver(String keyResolver) {
            this.keyResolver = keyResolver;
        }

        public String getKetResolver() {
            return keyResolver;
        }

        public void setKetResolver(String ketResolver) {
            this.keyResolver = ketResolver;
        }
    }
}
