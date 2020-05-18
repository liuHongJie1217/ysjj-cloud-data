package com.system.core.service.user.service.sysmain.impl;

import com.core.mapper.dataobject.UserAccountPwdDO;
import com.core.mapper.mapper.manual.UserAccountPwdMapper;
import com.core.mapper.req.sysmain.SysUserLoginReq;
import com.system.core.service.user.service.sysmain.SysMainService;
import com.ysjj.cloud.data.common.error.BizException;
import com.ysjj.cloud.data.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户登录相关操作实现类 <br>
 * @Date: 2020/5/18 15:09 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Service
public class SysMainServiceImpl implements SysMainService {
    @Value("${ysjj.checkVerificationCode}")
    private boolean checkVerificationCode;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserAccountPwdMapper userAccountPwdMapper;

    @Override
    public String userLogin(SysUserLoginReq req, HttpServletRequest HttpRequest) {
        //检验验证码
        if (checkVerificationCode) {
            checkVerificationCode(req);
        }


        return null;
    }

    /**
     * 检验验证码信息
     *
     * @param req 用户请求信息
     */
    private void checkVerificationCode(SysUserLoginReq req) {
        String key = "verUUidCode" + req.getVerUUidCode();
        if (!redisUtil.hasKey(key)) {
            throw BizException.failHandler("验证码已失效");
        }
        String verUUidCode = (String) redisUtil.get(key);
        String ss = req.getVerUUidCode().toLowerCase().trim();
        if (!ss.equals(verUUidCode.toLowerCase().trim())) {
            throw BizException.failHandler("验证码不正确");
        }
        //查询用户信息
        UserAccountPwdDO userAccountPwdDO = userAccountPwdMapper.sysUserAccountLogin(req.getSysUserLoginName());
        if (null == userAccountPwdDO) {
            BizException.fail("用户名或密码错误");
        }


    }
}
