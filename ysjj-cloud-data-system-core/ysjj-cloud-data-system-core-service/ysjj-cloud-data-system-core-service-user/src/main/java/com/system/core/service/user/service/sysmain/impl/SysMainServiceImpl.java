package com.system.core.service.user.service.sysmain.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.core.mapper.dataobject.LoginLogDO;
import com.core.mapper.dataobject.UserAccountPwdDO;
import com.core.mapper.dataobject.UserDO;
import com.core.mapper.enums.DelFlagEnum;
import com.core.mapper.enums.LoginTypeEnum;
import com.core.mapper.mapper.manual.UserAccountPwdMapper;
import com.core.mapper.mapper.manual.UserMapper;
import com.core.mapper.req.sysmain.SysUserLoginReq;
import com.core.netty.service.NettyHandlerService;
import com.google.common.collect.Lists;
import com.system.core.service.user.async.LoginLogAsync;
import com.system.core.service.user.service.sysmain.SysMainService;
import com.system.core.service.user.service.systemconfig.SuperAdminsService;
import com.ysjj.cloud.data.common.entity.RedisUser;
import com.ysjj.cloud.data.common.error.BizException;
import com.ysjj.cloud.data.common.error.RedisKeyEnum;
import com.ysjj.cloud.data.common.util.Encrypt;
import com.ysjj.cloud.data.common.util.JwtTokenUtil;
import com.ysjj.cloud.data.common.util.RedisUtil;
import com.ysjj.cloud.data.common.util.StrUtil;
import com.ysjj.cloud.data.common.util.snowFlake.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    private SuperAdminsService superAdminsService;
    @Autowired
    private NettyHandlerService nettyHandlerService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAccountPwdMapper userAccountPwdMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LoginLogAsync loginLogAsync;
    private static final SnowFlake snowFlake = new SnowFlake(1, 1);

    @Override
    public String userLogin(SysUserLoginReq req, HttpServletRequest HttpRequest) {
        //检验验证码
        if (checkVerificationCode) {
            checkVerificationCode(req);
        }
        //查询用户信息
        UserAccountPwdDO userAccountPwdDO = userAccountPwdMapper.sysUserAccountLogin(req.getSysUserLoginName());
        if (null == userAccountPwdDO) {
            throw BizException.fail("用户名或密码错误");
        }
        boolean isSuperAdmin = superAdminsService.checkIsSuperAdmin(userAccountPwdDO.getSysUserLoginName());
        if (!isSuperAdmin) {
            //删除状态(0-正常 1-删除)
            if (userAccountPwdDO.getDelFlag().equals(DelFlagEnum.del)) {
                throw BizException.fail("您的账号已被删除，请联系管理员");
            }
            //状态 0-启用 1-禁用
            if (userAccountPwdDO.getDisabledFlag().equals(1)) {
                throw BizException.fail("您的账号已被禁用，请联系管理员");
            }
        }
        String pwd = Encrypt.SHA512AndSHA256(req.getSysUserPwd(), userAccountPwdDO.getSysUserAuthSalt());
        if (!userAccountPwdDO.getSysUserPwd().equals(pwd)) {
            throw new BizException("用户名或密码错误");
        }
        UserDO userInfo = userMapper.getById(userAccountPwdDO.getSysUserId());
        String uuid = StrUtil.genUUID();
        RedisUser redisUser = RedisUser.builder().redisUserKey(uuid)
                .loginType(LoginTypeEnum.pwd.type)
                .sysUserLoginName(userAccountPwdDO.getSysUserLoginName())
                .sysUserPhone(userInfo.getSysUserPhone())
                .tenantId(userAccountPwdDO.getTenantId())
                .sysUserName(userInfo.getSysUserName())
                .baseId(userInfo.getId())
                .pwdId(userAccountPwdDO.getId())
                .sysLoginNumber(userAccountPwdDO.getSysLoginNumber())
                .sysUserEmail(userInfo.getSysUserEmail())
                .build();
        String jsonUser = JSONObject.toJSONString(redisUser);
        //生成Token
        //randomKey和token已经生成完毕
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(jsonUser, randomKey);
        //限制登录次数 -1 则不进行限制
        Integer sysLoginNumber = userAccountPwdDO.getSysLoginNumber();
        if (sysLoginNumber.equals(0)) {
            throw BizException.fail("您已经被限制登录，请联系管理员");
        } else if (sysLoginNumber.equals(-1)) {
            //redis key -> 用户id    value  -> List<String> string = uuid
            //redis key -> uuid      value  -> token
            Object listUuid = redisUtil.get(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + userInfo.getId());
            //首次登录
            if (listUuid == null) {
                ArrayList<String> list = Lists.newArrayList();
                list.add(uuid);
                String jsonString = JSONObject.toJSONString(list);
                //设置 token
                loginNumber(userInfo.getId(), jsonString, uuid, token);
            } else {//已经登录
                List<String> list = JSON.parseObject((String) listUuid, new TypeReference<List<String>>() {
                });
                //登录次数已满，剔除最先登录的用户
                if (list.size() >= sysLoginNumber) {
                    int count = list.size() - sysLoginNumber + 1;
                    String[] strings = new String[count];
                    for (int i = 0; i < count; i++) {
                        strings[i] = RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + list.get(i);
                    }
                    redisUtil.del(strings);
                    list.remove(0);
                    list.add(uuid);
                    String listStr = JSONObject.toJSONString(list);
                    loginNumber(userInfo.getId(), listStr, uuid, token);
                } else {//登录次数未满
                    list.add(uuid);
                    String listStr = JSONObject.toJSONString(list);
                    loginNumber(userInfo.getId(), listStr, uuid, token);
                }
            }
        } else { //不限制登录次数
            //token 放入 redis
            redisUtil.set(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + uuid, token, RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());
        }
        LoginLogDO logDO = LoginLogDO.builder().tenantId(redisUser.getTenantId())
                .userLoginName(redisUser.getSysUserLoginName())
                .userUserName(redisUser.getSysUserName())
                .userLoginType(redisUser.getLoginType())
                .userId(userInfo.getId())
                .id(snowFlake.nextId())
                .uuid(uuid)
                .build();
        loginLogAsync.loginLog(logDO, HttpRequest);
        //TODO 更新首页用户在线数量
        nettyHandlerService.onlineCount("+");
        //登录成功删除验证码
        redisUtil.del("verUUidCode" + req.getVerUUidCode());
        return uuid;

    }

    /**
     * 将token 保存在redis
     *
     * @param id         用户id
     * @param jsonString List<String> string = uuid
     * @param uuid
     * @param token
     */
    private void loginNumber(Long id, String jsonString, String uuid, String token) {
        redisUtil.set(RedisKeyEnum.REDIS_KEY_USER_ID.getKey() + id, jsonString, RedisKeyEnum.REDIS_KEY_USER_ID.getExpireTime());
        redisUtil.set(RedisKeyEnum.REDIS_KEY_USER_INFO.getKey() + uuid, token, RedisKeyEnum.REDIS_KEY_USER_INFO.getExpireTime());
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
    }
}
