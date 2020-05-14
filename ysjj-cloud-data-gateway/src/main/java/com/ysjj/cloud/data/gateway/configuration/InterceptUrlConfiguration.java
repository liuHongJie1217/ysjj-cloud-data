package com.ysjj.cloud.data.gateway.configuration;

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
 * @Description: 拦截URL配置 <br>
 * @Date: 2020/5/14 17:04 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Configuration
@ConfigurationProperties("ysjj")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterceptUrlConfiguration {
    //忽略拦截URL
    private List<String> ignorerUrl = Lists.newArrayList();
    //忽略拦截权限URL
    private List<String> ignorerAclUrl = Lists.newArrayList();

    //#企业相关操作URL，只有ysjj企业下的员工才可以操作
    private List<String> enterpriseUrl = Lists.newArrayList();
    private List<String> superAdmins = Lists.newArrayList();

    public boolean checkIsSuperAdmin(String sysUserPhone) {
        if (StringUtils.isBlank(sysUserPhone)) {
            return false;
        }
        return superAdmins.contains(sysUserPhone);
    }

    public boolean checkIgnoreUrl(String gatewayUrl) {
        if (StringUtils.isBlank(gatewayUrl)) {
            return true;
        }
        return ignorerUrl.contains(gatewayUrl);
    }

    public boolean checkIgnorerAclUrl(String gatewayUrl) {
        if (StringUtils.isBlank(gatewayUrl)) {
            return true;
        }
        return ignorerAclUrl.contains(gatewayUrl);
    }

    public boolean checkEnterpriseUrl(String gatewayUrl) {
        if (StringUtils.isBlank(gatewayUrl)) {
            return true;
        }
        return enterpriseUrl.contains(gatewayUrl);
    }
}
