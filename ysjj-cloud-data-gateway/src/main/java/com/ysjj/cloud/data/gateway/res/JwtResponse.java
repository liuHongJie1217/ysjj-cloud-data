package com.ysjj.cloud.data.gateway.res;


import com.alibaba.fastjson.JSONObject;
import com.ysjj.cloud.data.common.common.JSONResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Description: 网关统一响应数据 <br>
 * @Date: 2020/5/15 11:55 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public class JwtResponse {
    public static Mono<Void> JwtResponse(ServerWebExchange exchange, Integer httpStatus, String responseMsg) {
        byte[] bytes = JSONObject.toJSONString(JSONResult.errorException(httpStatus, responseMsg, responseMsg)).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
