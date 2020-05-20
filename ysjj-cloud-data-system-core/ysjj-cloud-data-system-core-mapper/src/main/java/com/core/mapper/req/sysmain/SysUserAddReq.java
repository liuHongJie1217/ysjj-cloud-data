package com.core.mapper.req.sysmain;

import com.ysjj.cloud.data.common.error.BaseReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * @Description: 添加用户请求 <br>
 * @Date: 2020/5/20 17:17 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserAddReq implements Serializable {


    @NotBlank(message = "uuid必填", groups = {BaseReq.Detail.class, BaseReq.Status.class, BaseReq.Permission.class, BaseReq.Modify.class})
    private String uuid;
    /**
     * sysUserName 姓名.
     */
    @NotBlank(message = "姓名必填", groups = {BaseReq.Add.class, BaseReq.Modify.class})
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
     * disabledFlag 是否被禁用  0否 1禁用.
     */
    @NotNull(message = "状态必填", groups = {BaseReq.Add.class, BaseReq.Modify.class, BaseReq.Status.class})
    private Integer disabledFlag;
    /**
     * sysUserLoginName 登录名.
     */
    @NotBlank(message = "登录名必填", groups = {BaseReq.Add.class})
    @Email(message = "邮箱格式不正确")
    private String sysUserLoginName;
    /**
     * sysUserPwd 密码.
     */
    @NotBlank(message = "密码必填", groups = {BaseReq.Add.class, BaseReq.Permission.class})
    private String sysUserPwd;
    /**
     * remark 备注.
     */
    private String remark;
}
