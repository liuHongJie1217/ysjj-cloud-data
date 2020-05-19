package com.core.mapper.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: 登录日志 <br>
 * @Date: 2020/5/19 17:09 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginLogDO {
    /**
     * id ID.
     */
    private Long id;
    /**
     * userId 用户id.
     */
    private Long userId;
    /**
     * tenantId 租户id.
     */
    private Long tenantId;
    /**
     * uuid UUID.
     */
    private String uuid;
    /**
     * userIp 用户ip.
     */
    private String userIp;
    /**
     * createBy 创建人.
     */
    private String createBy;
    /**
     * updateBy 修改人.
     */
    private String updateBy;
    /**
     * userLoginSys 操作系统.
     */
    private String userLoginSys;
    /**
     * userUserName 用户名.
     */
    private String userUserName;
    /**
     * userLoginName 登录名.
     */
    private String userLoginName;
    /**
     * userLoginDevice 登录设备.
     */
    private String userLoginDevice;
    /**
     * userLoginBrowser 获取浏览器类型.
     */
    private String userLoginBrowser;
    /**
     * disabledFlag 状态 0启用  1禁用.
     */
    private Integer disabledFlag;
    /**
     * userLoginType 登录类型.
     */
    private Integer userLoginType;
    /**
     * createTime 登录时间.
     */
    private Date createTime;
    /**
     * updateTime 修改时间.
     */
    private Date updateTime;
}
