package com.system.core.service.user.service.sysmain;

import com.core.mapper.req.sysmain.SysUserLoginReq;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户登录相关操作 <br>
 * @Date: 2020/5/18 15:08 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public interface SysMainService {

    /**
     * @param req 用户登录信息
     * @return 用户随机码 UUID
     * @description: 用户登录 <br>
     * @date: 2020/5/18 15:15 <br>
     * @author: hongjie.liu <br>
     */
    String userLogin(SysUserLoginReq req, HttpServletRequest httpRequest);
}
