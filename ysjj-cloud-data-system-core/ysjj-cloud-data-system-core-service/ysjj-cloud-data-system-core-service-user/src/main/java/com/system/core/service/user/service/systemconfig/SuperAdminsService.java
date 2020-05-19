package com.system.core.service.user.service.systemconfig;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description: 定义一个超级管理员，可以从配置文件中获取
 * 可以指定某个用户，也可以指定某个角色<br>
 * @Date: 2020/5/19 15:02 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Configuration
@ConfigurationProperties("ysjj")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuperAdminsService {
    private Long tenantId;
    private List<String> superAdmins = Lists.newArrayList();

    /**
     * true -> 超级管理员
     */
    public boolean checkIsSuperAdmin(String sysUserPhone) {
        if (StringUtils.isEmpty(sysUserPhone)) {
            return false;
        }
        return superAdmins.contains(sysUserPhone);
    }
}
