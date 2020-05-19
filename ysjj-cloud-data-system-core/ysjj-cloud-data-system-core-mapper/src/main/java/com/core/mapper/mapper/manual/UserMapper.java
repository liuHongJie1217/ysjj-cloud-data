package com.core.mapper.mapper.manual;

import com.core.mapper.dataobject.UserDO;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户基础信息查询 <br>
 * @Date: 2020/5/19 15:37 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public interface UserMapper {

    UserDO getById(@Param("id") Long id);
}
