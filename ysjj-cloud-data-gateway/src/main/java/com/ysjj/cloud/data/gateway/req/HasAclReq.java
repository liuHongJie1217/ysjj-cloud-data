package com.ysjj.cloud.data.gateway.req;

import com.ysjj.cloud.data.common.entity.RedisUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: HasAclReq <br>
 * @Date: 2020/5/14 17:56 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HasAclReq extends RedisUser {
    private String url;
}
