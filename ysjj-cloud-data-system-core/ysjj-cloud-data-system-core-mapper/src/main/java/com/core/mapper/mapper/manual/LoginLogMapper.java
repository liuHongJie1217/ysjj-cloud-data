package com.core.mapper.mapper.manual;

import com.core.mapper.dataobject.LoginLogDO;

/**
 * @Description: 用户登录日志 <br>
 * @Date: 2020/5/19 17:25 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public interface LoginLogMapper {

    int insertSelective(LoginLogDO loginLogDO);
}
