package com.core.netty.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: netty channel 管理 <br>
 * @Date: 2020/5/19 18:27 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public class ChannelManager {
    //k-->channelId v-->channel
    private final static Map<String, Channel> channelCache = new ConcurrentHashMap<>(64);
    //k-->channelId v-->userId
    private final static Map<String, Long> userIdCache = new ConcurrentHashMap<>(64);
    //用户id和channelId的关系
    private final static Multimap<Long, String> channelIdCache = ArrayListMultimap.create();

    //###########   用户channelId和channel的关系   ###########
    public static Map<String, Channel> getAllChannel() {
        return channelCache;
    }

    public static void putChannel(String key, Channel value) {
        channelCache.put(key, value);
    }

    public static Channel getChannel(String key) {
        return channelCache.get(key);
    }

    public static void removeChannel(String key) {
        channelCache.remove(key);
    }

    public static int sizeChannel() {
        return channelCache.size();
    }
    //###########   用户channelId和用户id的关系   ###########

    public static Map<String, Long> getAllUserId() {
        return userIdCache;
    }

    public static void putUserId(String key, Long userId) {
        userIdCache.put(key, userId);
    }

    public static Long getUserId(String key) {
        return userIdCache.get(key);
    }

    public static void removeUserId(String key) {
        userIdCache.remove(key);
    }

    public static int sizeUserId() {
        return channelCache.size();
    }

    //###########   用户id和channelId的关系   ###########
    public static Map<Long, Collection<String>> getChannelId() {
        Map<Long, Collection<String>> listMap = channelIdCache.asMap();
        return listMap;
    }

    public static void putChannelId(Long key, String channelId) {
        channelIdCache.put(key, channelId);
    }

    public static List<String> getChannelId(Long key) {
        return (List<String>) channelIdCache.get(key);
    }

    public static void removeChannelId(Long key, String channelId) {
        channelIdCache.remove(key, channelId);
    }

    public static void ctxWrite(String key, Object obj) {
        Channel channel = getChannel(key);
        ctxWrite(channel, obj);
    }

    public static void ctxWrite(ChannelHandlerContext ctx, Object obj) {
        Channel channel = ctx.channel();
        ctxWrite(channel, obj);
    }

    public static void ctxWrite(Channel channel, Object obj) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(obj)));
    }

    public static String channelLongText(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        return channel.id().asLongText();
    }

    public static void channelClose(ChannelHandlerContext ctx) {
        String channelId = ChannelManager.channelLongText(ctx);
        Long UserId = ChannelManager.getUserId(channelId);
        channelClose(channelId, UserId);
    }

    public static void channelClose(String channelId, Long UserId) {
        ChannelManager.removeChannel(channelId);
        ChannelManager.removeUserId(channelId);
        ChannelManager.removeChannelId(UserId, channelId);
    }
}
