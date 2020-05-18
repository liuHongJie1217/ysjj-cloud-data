package com.core.mapper.mapper.manual;

import com.core.mapper.dataobject.UserAccountPwdDO;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户相关操作 <br>
 * @Date: 2020/5/18 16:59 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public interface UserAccountPwdMapper {

    /**
     * 根据登录账号 查询用户信息
     *
     * @param sysUserLoginName
     * @return
     */
    UserAccountPwdDO sysUserAccountLogin(@Param("sysUserLoginName") String sysUserLoginName);
}
