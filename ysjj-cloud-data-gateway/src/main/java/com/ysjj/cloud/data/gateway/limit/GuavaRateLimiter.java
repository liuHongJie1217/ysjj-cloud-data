package com.ysjj.cloud.data.gateway.limit;


import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Guava本地限流 <br>
 * @Date: 2020/5/15 17:25 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public class GuavaRateLimiter {
    static ConcurrentHashMap<String, RateLimiter> resourceRateLimiter = new ConcurrentHashMap<>(32);

    //初始化限流工具RateLimiter
    static {
        createResourceRateLimiter("platform/sysMain/login/v1", 50d);
    }

    public static void createResourceRateLimiter(String resource, Double qps) {
        if (qps == null) {
            qps = 50d;
        }
        if (resourceRateLimiter.contains(resource)) {
            resourceRateLimiter.get(resource).setRate(qps);

        } else {
            //创建限流工具，没秒发出50个令牌指令
            RateLimiter rateLimiter = RateLimiter.create(qps);
            resourceRateLimiter.put(resource, rateLimiter);
        }
    }

    public static void updateResourceRateLimiter(String resource, Double qps) {
        if (StringUtils.isBlank(resource)) {
            return;
        }
        if (qps == null) {
            qps = 50d;
        }
        //创建限流工具，没秒发出50个令牌指令
        RateLimiter rateLimiter = RateLimiter.create(qps);
        resourceRateLimiter.put(resource, rateLimiter);
    }

}
