package com.core.netty.utils;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: IMMessage <br>
 * @Date: 2020/5/19 18:35 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
@Data
@NoArgsConstructor
@Builder
public class IMMessage implements Serializable {
    /**
     * 消息类型
     */
    private Integer msgtype;
    /**
     * 业务数据
     */
    private Object data;
    /**
     * token
     */
    private String tonken;

    public IMMessage(Integer msgtype, Object data, String tonken) {
        this.msgtype = msgtype;
        this.data = data;
        this.tonken = tonken;
    }
}
