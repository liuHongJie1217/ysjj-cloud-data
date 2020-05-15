package com.ysjj.cloud.data.gateway.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ysjj.cloud.data.common.common.JSONResult;
import com.ysjj.cloud.data.common.entity.RedisUser;
import com.ysjj.cloud.data.common.error.RedisKeyEnum;
import com.ysjj.cloud.data.common.util.DateUtils;
import com.ysjj.cloud.data.common.util.JwtTokenUtil;
import com.ysjj.cloud.data.common.util.RedisUtil;
import com.ysjj.cloud.data.gateway.configuration.InterceptUrlConfiguration;
import com.ysjj.cloud.data.gateway.feign.HasAclServiceFeign;
import com.ysjj.cloud.data.gateway.req.HasAclReq;
import com.ysjj.cloud.data.gateway.res.JwtResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.function.Consumer;

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
        String uuid = exchange.getRequest().getHeaders().getFirst(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey());
        String authToken;
        ServerHttpRequest request = exchange.getRequest();

        try {
            URI uri = request.getURI();
            String path = uri.getPath();
            //检验是否是忽略拦截的URL
            if (interceptUrlConfiguration.checkIgnoreUrl(path)) {
                return chain.filter(exchange);
            }
            //未授权的用户 -> 401
            if (StringUtils.isBlank(uuid)) {
                return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "token出错");
            } else {

                //获取用户Token
                Object sessionJwt = redisUtil.get(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + uuid);
                if (sessionJwt == null) {
                    return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "会话已失效，请重新登录");
                }
                authToken = String.valueOf(sessionJwt);
                //解析token
                String userInfo = jwtTokenUtil.getUsernameFromToken(authToken);
                RedisUser redisUser = JSON.parseObject(userInfo, new TypeReference<RedisUser>() {
                });
                //判断租户Id是否一致 && 需要访问的资源是否需要权限
                if (!redisUser.getTenantId().equals(interceptUrlConfiguration.getTeantId()) && interceptUrlConfiguration.checkEnterpriseUrl(path)) {
                    return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "您无权访问该资源");
                }
                //不允许修改管理员权限
                if (interceptUrlConfiguration.checkIsSuperAdmin(redisUser.getSysUserPhone()) && path.equalsIgnoreCase(modifyAdministratorAccountUrl)) {
                    return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "您无法修改管理员账号密码");
                }

                if (!interceptUrlConfiguration.checkIgnorerAclUrl(path)) {
                    //权限拦截
                    HasAclReq hasAclReq = new HasAclReq();
                    hasAclReq.setUrl(path);
                    hasAclReq.setTenantId(redisUser.getTenantId());
                    hasAclReq.setSysUserPhone(redisUser.getSysUserPhone());
                    JSONResult result = hasAclServiceFeign.hasAcl(hasAclReq);
                    if (result.getStatus() == 200) {
                        if (!(boolean) result.getData()) {
                            return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "您无权访问");
                        }
                    } else {
                        return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "您无权访问");
                    }
                }

                //重置JWT失效时间
                //获取失效时间
                Date expirationDataFromToken = jwtTokenUtil.getExpirationDataFromToken(String.valueOf(sessionJwt));
                long minuteDifference = DateUtils.getMinuteDifference(expirationDataFromToken, DateUtils.getDateTime());
                if (minuteDifference <= EXPIREDJWT) {
                    //randomKey 和 token 已经生成完毕
                    final String randomKey = jwtTokenUtil.getRandomKey();
                    final String newToken = jwtTokenUtil.generateToken(userInfo, randomKey);
                    redisUtil.set(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + redisUser.getRedisUserKey(), newToken, RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());
                    authToken = newToken;
                }
                //刷新redis - token时间
                long expire = redisUtil.getExpire(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + redisUser.getBaseId());
                if (expire <= EXPIREDJWT) {
                    //刷新Redis时间
                    redisUtil.expire(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + redisUser.getBaseId(), RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());
                }
                String finalAuthToken = authToken;
                Consumer<HttpHeaders> headersConsumer = httpHeaders -> {
                    httpHeaders.add("exit", uuid);
                    httpHeaders.add(RedisKeyEnum.REDIS_KEY_HEADER_INFO.getKey(), finalAuthToken);
                };
                ServerHttpRequest build = exchange
                        .getRequest().mutate()
                        .headers(headersConsumer)
                        .build();
                exchange.getResponse().getHeaders().add("testResponse1", "testResponse1");
                exchange.getResponse().getHeaders().add("testResponse1", "testResponse1");
                ResponseCookie cookie1 = ResponseCookie.from("cookie1", "cookie1").build();
                exchange.getResponse().addCookie(cookie1);
                ResponseCookie cookie2 = ResponseCookie.from("cookie1", "cookie1").build();
                exchange.getResponse().addCookie(cookie2);
                //构建新的Request 变成 exchange 对象
                ServerWebExchange webExchange = exchange.mutate().request(request).build();
                return chain.filter(webExchange);
            }

        } catch (MalformedJwtException e) {
            log.error("JWT格式错误异常:{}======>>>:{}====={}", uuid, e.getMessage(), e);
            return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "JWT格式错误");

        } catch (SignatureException e) {
            log.error("JWT签名错误异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "JWT签名错误");
        } catch (ExpiredJwtException e) {
            log.error("JWT过期异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "会话结束，请重新登录");
        } catch (UnsupportedJwtException e) {
            log.error("不支持的JWT异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "JWT格式不正确");
        } catch (Exception e) {
            log.error("TokenFilter，不支持的异常:{}======>>>:{}", uuid, e.getMessage(), e);
            return JwtResponse.JwtResponse(exchange, HttpStatus.UNAUTHORIZED.value(), "TokenFilter:token 解析异常：");
        }

    }

    @Override
    public int getOrder() {
        return -9999999;
    }

    private String urlEncode(Object o) {
        try {
            return URLEncoder.encode(JSONObject.toJSONString(o), "UTF-8");
        } catch (Exception e) {
            log.error("urlEncode:{}", e.getMessage());
        }
        return "";
    }
}
