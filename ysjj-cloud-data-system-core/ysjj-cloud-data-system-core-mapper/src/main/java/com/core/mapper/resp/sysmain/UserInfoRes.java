package com.core.mapper.resp.sysmain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: UserInfoRes <br>
 * @Date: 2020/5/20 15:10 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoRes implements Serializable {
    /**
     * 用户名称
     */
    private String sysUserName;
}
