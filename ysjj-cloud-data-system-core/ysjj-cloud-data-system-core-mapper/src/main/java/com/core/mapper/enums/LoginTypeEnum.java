package com.core.mapper.enums;

/**
 * @Description: LoginTypeEnum <br>
 * @Date: 2020/5/19 16:12 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public enum LoginTypeEnum {
    pwd(0, "账号密码登陆", "账号密码登陆");

    public final Integer type;
    public final String value;
    public final String description;

    LoginTypeEnum(Integer type, String value, String description) {
        this.type = type;
        this.value = value;
        this.description = description;
    }
}
