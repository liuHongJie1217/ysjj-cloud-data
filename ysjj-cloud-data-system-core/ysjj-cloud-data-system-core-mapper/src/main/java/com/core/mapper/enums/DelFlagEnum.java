package com.core.mapper.enums;

/**
 * @Description: 删除状态 <br>
 * @Date: 2020/5/19 15:15 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public enum DelFlagEnum {

    ok(0, "正常", ""),
    del(1, "已删除", "");

    public final Integer type;
    public final String value;
    public final String description;

    DelFlagEnum(Integer type, String value, String description) {
        this.type = type;
        this.value = value;
        this.description = description;
    }
}
