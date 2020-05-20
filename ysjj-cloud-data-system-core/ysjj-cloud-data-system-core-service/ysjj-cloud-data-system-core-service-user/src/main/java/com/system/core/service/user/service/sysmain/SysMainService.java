package com.system.core.service.user.service.sysmain;

import com.core.mapper.req.sysmain.SysUserLoginReq;
import com.core.mapper.resp.sysmain.UserInfoRes;
import com.ysjj.cloud.data.common.common.JSONResult;

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

    /**
     * @return 用户信息
     * @description: 用户信息 <br>
     * @date: 2020/5/20 15:12 <br>
     * @author: hongjie.liu <br>
     */
    UserInfoRes getUserInfo();

    /**
     * @return 状态信息
     * @description: 用户正常退出 <br>
     * @date: 2020/5/20 16:22 <br>
     * @author: hongjie.liu <br>
     */
    String logout();

    /**
     * @return 返回验证码基础信息
     * @description: 验证码 <br>
     * @date: 2020/5/20 16:45 <br>
     * @author: hongjie.liu <br>
     */
    JSONResult createVerificationCode();

}
