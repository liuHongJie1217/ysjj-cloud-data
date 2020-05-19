package com.core.netty.service;

import com.core.netty.utils.ChannelManager;
import com.core.netty.utils.IMMessage;
import com.ysjj.cloud.data.common.error.RedisKeyEnum;
import com.ysjj.cloud.data.common.util.JwtTokenUtil;
import com.ysjj.cloud.data.common.util.RedisUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Description: netty相关实现<br>
 * @Date: 2020/5/19 18:23 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Service
@Slf4j
public class NettyHandlerService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Async("threadPoolTaskExecutor")
    public Future<String> onlineCount(String symbol) {
        AsyncResult<String> result = new AsyncResult<>("更新首页用户在线数量");
        Map<String, Channel> allChannel = ChannelManager.getAllChannel();
        if (allChannel != null || !allChannel.isEmpty()) {
            int onlineCount = 0;
            if (StringUtils.isEmpty(symbol)) {
                onlineCount = ChannelManager.sizeChannel();
            } else if ("-".equals(symbol)) {
                onlineCount = ChannelManager.sizeChannel() - 1;
            } else if ("+".equals(symbol)) {
                onlineCount = ChannelManager.sizeChannel() + 1;
            } else {
                onlineCount = ChannelManager.sizeChannel();
            }
            IMMessage imMessage = new IMMessage(RedisKeyEnum.NETTY_ONLINE_COUNT.getExpireTime(), onlineCount, null);
            allChannel.forEach((s, channel) -> ChannelManager.ctxWrite(channel, imMessage));
        }
        return result;
    }
}
