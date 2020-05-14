package com.ysjj.cloud.data.common.util;

import java.util.Random;

/**
 * @Description: 常用方法工具类 <br>
 * @Date: 2020/5/14 10:31 <br>
 * @author: hongjie.liu <br>
 * @version: 1.0 <br>
 */
public class ToolUtil {

    /**
     * 获取随机数的字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int i1 = random.nextInt(base.length());
            sb.append(base.charAt(i1));
        }
        return sb.toString();

    }

}
