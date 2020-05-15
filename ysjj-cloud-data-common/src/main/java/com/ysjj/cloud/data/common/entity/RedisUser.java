package com.ysjj.cloud.data.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 用户基础信息 <br>
 * @Date: 2020/5/15 14:53 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisUser {

    /**
     * redis随机数
     */
    private String redisUserKey;
    /**
     * 登录类型
     */
    private Integer loginType;
    /**
     * 用户基础表id.
     */
    private Long baseId;
    /**
     * 密码Id
     */
    private Long pwdId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户基础表 姓名.
     */
    private String sysUserName;

    /**
     * 用户基础表 邮箱.
     */
    private String sysUserEmail;
    /**
     * 用户基础表 手机号.
     */
    private String sysUserPhone;
    /**
     * sysLoginNumber 账号允许登录的次数 -1 不限次数 ，如需登录次数为0，请禁用该账号，减少代码复杂度.
     */
    private Integer sysLoginNumber;

    /**
     * sysUserLoginName 登录名.
     */
    private String sysUserLoginName;

}
