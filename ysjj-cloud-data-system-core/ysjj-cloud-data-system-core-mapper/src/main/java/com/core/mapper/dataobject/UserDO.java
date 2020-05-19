package com.core.mapper.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: 用户基础信息表 <br>
 * @Date: 2020/5/19 15:38 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO {
    /**
     * id 主键.
     */
    private Long id;
    /**
     * tenantId 租户id.
     */
    private Long tenantId;
    /**
     * sysUserArea 区.
     */
    private Long sysUserArea;
    /**
     * sysUserCity 市.
     */
    private Long sysUserCity;
    /**
     * sysUserProvince 省.
     */
    private Long sysUserProvince;
    /**
     * uuid 唯一，32位字符串，查询用.
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
     * sysUserName 姓名.
     */
    private String sysUserName;
    /**
     * sysUserEmail 邮箱.
     */
    private String sysUserEmail;
    /**
     * sysUserPhone 手机号.
     */
    private String sysUserPhone;
    /**
     * sysUserAreaName 区名字.
     */
    private String sysUserAreaName;
    /**
     * sysUserCityName 市名字.
     */
    private String sysUserCityName;
    /**
     * sysUserProvinceName 省 名字.
     */
    private String sysUserProvinceName;
    /**
     * delFlag 删除状态(0-正常，1-删除).
     */
    private Integer delFlag;
    /**
     * disabledFlag 是否被禁用  0否 1禁用.
     */
    private Integer disabledFlag;
    /**
     * createTime 创建时间.
     */
    private Date createTime;
    /**
     * updateTime 修改时间.
     */
    private Date updateTime;
}
