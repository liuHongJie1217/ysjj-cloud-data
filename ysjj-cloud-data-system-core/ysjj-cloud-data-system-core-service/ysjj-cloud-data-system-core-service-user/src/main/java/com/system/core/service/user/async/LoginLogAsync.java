package com.system.core.service.user.async;

import com.alibaba.fastjson.JSONObject;
import com.core.mapper.dataobject.LoginLogDO;
import com.core.mapper.mapper.manual.LoginLogMapper;
import com.ysjj.cloud.data.common.common.UserAgentGetter;
import com.ysjj.cloud.data.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 登录日志同步数据库 <br>
 * @Date: 2020/5/19 17:23 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Slf4j
public class LoginLogAsync {
    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     * 登录日志存入数据库
     */
    @Async("threadPoolTaskExecutor")
    public void loginLog(LoginLogDO entity, HttpServletRequest request) {
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        entity.setCreateTime(DateUtils.getDateTime());
        entity.setUserIp(userAgentGetter.getIpAddr());
        entity.setUserLoginDevice(userAgentGetter.getDevice());
        entity.setUserLoginSys(userAgentGetter.getOS());
        entity.setUserLoginBrowser(userAgentGetter.getBrowser());
        log.info("登录日志信息：{}", JSONObject.toJSONString(entity));
        loginLogMapper.insertSelective(entity);
    }


}
