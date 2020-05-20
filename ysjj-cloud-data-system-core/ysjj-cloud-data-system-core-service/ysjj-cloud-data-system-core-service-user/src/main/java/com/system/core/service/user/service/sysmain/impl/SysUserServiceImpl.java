package com.system.core.service.user.service.sysmain.impl;

import com.core.mapper.dataobject.UserAccountPwdDO;
import com.core.mapper.dataobject.UserDO;
import com.core.mapper.mapper.manual.UserAccountPwdMapper;
import com.core.mapper.mapper.manual.UserMapper;
import com.core.mapper.req.sysmain.SysUserAddReq;
import com.system.core.service.user.service.sysmain.SysUserService;
import com.system.core.service.user.service.systemconfig.BaseService;
import com.ysjj.cloud.data.common.entity.RedisUser;
import com.ysjj.cloud.data.common.error.BizException;
import com.ysjj.cloud.data.common.util.DateUtils;
import com.ysjj.cloud.data.common.util.Encrypt;
import com.ysjj.cloud.data.common.util.StrUtil;
import com.ysjj.cloud.data.common.util.snowFlake.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 用是操作实现类 <br>
 * @Date: 2020/5/20 17:16 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Service
@Slf4j
public class SysUserServiceImpl extends BaseService implements SysUserService {
    @Autowired
    private UserAccountPwdMapper userAccountPwdMapper;
    @Autowired
    private UserMapper userMapper;

    private static final SnowFlake SNOW_FLAKE = new SnowFlake(1, 1);

    @Override
    public String SysUserAdd(SysUserAddReq req) {
        UserAccountPwdDO accountPwdDO = userAccountPwdMapper.sysUserAccountLogin(req.getSysUserLoginName());
        if (accountPwdDO != null) {
            throw BizException.fail("给账号已存在");
        }
        RedisUser redisUser = this.redisUser();
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(req, userDO);
        Long id = SNOW_FLAKE.nextId();
        userDO.setId(id);
        userDO.setSysUserEmail(req.getSysUserEmail());
        userDO.setUuid(StrUtil.genUUID());
        userDO.setCreateBy(redisUser.getSysUserName());
        userDO.setUpdateBy(redisUser.getSysUserName());
        userDO.setCreateTime(DateUtils.getDateTime());
        userDO.setUpdateTime(DateUtils.getDateTime());
        userMapper.insertSelective(userDO);
        UserAccountPwdDO pwdDO = new UserAccountPwdDO();
        BeanUtils.copyProperties(req, pwdDO);
        pwdDO.setId(SNOW_FLAKE.nextId());
        String salt = StrUtil.genUUID();
        pwdDO.setSysUserAuthSalt(salt);
        pwdDO.setSysUserPwd(Encrypt.SHA512AndSHA256(req.getSysUserPwd(), salt));
        pwdDO.setTenantId(redisUser.getTenantId());
        pwdDO.setCreateBy(redisUser.getSysUserName());
        pwdDO.setUpdateBy(redisUser.getSysUserName());
        pwdDO.setCreateTime(DateUtils.getDateTime());
        pwdDO.setUpdateTime(DateUtils.getDateTime());
        pwdDO.setUuid(StrUtil.genUUID());
        pwdDO.setSysUserId(id);
        userAccountPwdMapper.insertSelective(pwdDO);
        return "新增用户成功";
    }
}
