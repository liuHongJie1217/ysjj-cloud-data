package com.core.mapper.dataobject;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 用户账号密码 <br>
 * @Date: 2020/5/18 17:02 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
public class UserAccountPwdDO {
    /**
     * id ID.
     */
    private Long id;
    /**
     * tenantId 租户id.
     */
    private Long tenantId;
    /**
     * sysUserId 关联用户表id.
     */
    private Long sysUserId;
    /**
     * uuid uuid.
     */
    private String uuid;
    /**
     * remark 备注.
     */
    private String remark;
    /**
     * createBy 创建人.
     */
    private String createBy;
    /**
     * updateBy 修改人.
     */
    private String updateBy;
    /**
     * sysUserPwd 密码.
     */
    private String sysUserPwd;
    /**
     * sysUserAuthSalt 撒盐.
     */
    private String sysUserAuthSalt;
    /**
     * sysUserLoginName 登录名.
     */
    private String sysUserLoginName;
    /**
     * delFlag 删除状态(0-正常，1-删除).
     */
    private Integer delFlag;
    /**
     * disabledFlag 状态 0启用  1禁用.
     */
    private Integer disabledFlag;
    /**
     * sysLoginNumber 账号允许登录的次数 -1 不限次数 ，0禁止登陆.
     */
    private Integer sysLoginNumber;
    /**
     * createTime 创建时间.
     */
    private Date createTime;
    /**
     * updateTime 修改时间.
     */
    private Date updateTime;
}
