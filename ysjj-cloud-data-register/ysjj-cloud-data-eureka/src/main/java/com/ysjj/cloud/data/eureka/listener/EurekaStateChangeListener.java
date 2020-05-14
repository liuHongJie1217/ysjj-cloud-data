package com.ysjj.cloud.data.eureka.listener;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Description: Eureka 监听事件 <br>
 * @Date: 2020/5/13 10:05 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Component
@Slf4j
public class EurekaStateChangeListener {

    /**
     * 服务下线事件
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.error(event.getServerId() + "\t" + event.getAppName() + "服务下线");
    }

    /**
     * 服务注册事件
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info(instanceInfo.getAppName() + "正在注册");
    }

    /**
     * 服务续约时间
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info(event.getServerId() + "\t" + event.getAppName());
    }

    /**
     * 服务注册中心启动
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info("注册中心 启动...");

    }

    /**
     * Eureka Server启动事件
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info("Eureka Server 启动...");

    }


}
