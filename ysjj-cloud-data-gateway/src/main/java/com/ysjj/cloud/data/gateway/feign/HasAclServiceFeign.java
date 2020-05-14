package com.ysjj.cloud.data.gateway.feign;

import com.ysjj.cloud.data.common.common.JSONResult;
import com.ysjj.cloud.data.gateway.req.HasAclReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: HasAcServiceFeign <br>
 * @Date: 2020/5/14 17:52 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@FeignClient(name = "ysjj-cloud-data-system-core", fallback = HasAclServiceFeign.HasAclServiceImplFeign.class)
public interface HasAclServiceFeign {
    @PostMapping(value = "/platform/intercept/hasAcl/v1", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JSONResult hasAcl(@RequestBody HasAclReq hasAclReq);

    @Component
    @Slf4j
    class HasAclServiceImplFeign implements HasAclServiceFeign {
        @Override
        public JSONResult hasAcl(HasAclReq hasAclReq) {
            log.error("hasAcl error");
            return JSONResult.errorMsg("服务调用权限校验异常");
        }
    }

}
