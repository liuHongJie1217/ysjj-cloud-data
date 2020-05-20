package com.system.core.service.user.service.systemconfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ysjj.cloud.data.common.entity.RedisUser;
import com.ysjj.cloud.data.common.error.RedisKeyEnum;
import com.ysjj.cloud.data.common.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Description: 基础类 <br>
 * @Date: 2020/5/20 16:02 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Slf4j
public class BaseService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 由网关传入用户信息
     *
     * @return
     */
    public RedisUser redisUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authToken = request.getHeader(RedisKeyEnum.REDIS_KEY_USER_HEADER_CODE.getKey());
        //解析token
        if (!StringUtils.isEmpty(authToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            return JSON.parseObject(username, new TypeReference<RedisUser>() {
            });
        }
        RedisUser redisUser = new RedisUser();
        redisUser.setSysUserName("liuhongjie");
        return redisUser;
    }
}
