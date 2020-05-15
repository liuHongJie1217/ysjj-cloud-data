package com.ysjj.cloud.data.gateway.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description: redis 限流 <br>
 * @Date: 2020/5/15 18:04 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Component
@Primary
@Slf4j
public class RateCheckRedisRateLimiter extends AbstractRateLimiter<RateCheckRedisRateLimiter.Config> implements ApplicationContextAware {

    public static final String CONFIGURATION_PROPERTY_NAME = "redis-rate-limiter";
    public static final String REDIS_SCRIPT_NAME = "redisRequestRateLimiterScript";

    private ReactiveRedisTemplate<String, String> redisTemplate;
    private RedisScript<List<Long>> script;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private Config defaultConfig;


    public RateCheckRedisRateLimiter() {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, (ConfigurationService) null);
    }

    public RateCheckRedisRateLimiter(ReactiveRedisTemplate<String, String> redisTemplate, RedisScript<List<Long>> script, Validator validator) {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, (ConfigurationService) null);
        this.redisTemplate = redisTemplate;
        this.script = script;
        initialized.compareAndSet(false, false);
    }

    public RateCheckRedisRateLimiter(int defaultReplenishRate, int defaultBurstCapacity) {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, (ConfigurationService) null);
        this.defaultConfig = new Config()
                .setBurstCapacity(defaultBurstCapacity)
                .setReplenishRate(defaultReplenishRate);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Validated
    public static class Config {
        @Min(1)
        private int replenishRate;

        @Min(0)
        private int burstCapacity = 0;

        public int getReplenishRate() {
            return replenishRate;
        }

        public Config setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
            return this;
        }

        public int getBurstCapacity() {
            return burstCapacity;
        }

        public Config setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "replenishRate=" + replenishRate +
                    ", burstCapacity=" + burstCapacity +
                    '}';
        }
    }
}
