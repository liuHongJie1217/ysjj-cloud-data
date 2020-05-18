package com.core.mapper.req.sysmain;

import com.ysjj.cloud.data.common.error.BaseReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Description: SysUserLoginReq <br>
 * @Date: 2020/5/18 15:39 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserLoginReq extends BaseReq {
    /**
     * sysUserPwd 密码
     */
    @NotBlank(message = "密码必填", groups = {Query.class})
    private String sysUserPwd;

    /**
     * sysUserLoginName 登录名.
     */
    @NotBlank(message = "登录名必填", groups = {Query.class})
    private String sysUserLoginName;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空", groups = {Query.class})
    private String verificationCode;

    /**
     * 该验证码对应唯一一个用户
     */
    @NotBlank(message = "验证码唯一标识必填", groups = {Query.class})
    private String verUUidCode;
}
