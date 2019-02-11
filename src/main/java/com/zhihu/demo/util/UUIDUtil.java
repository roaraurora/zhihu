package com.zhihu.demo.util;

import java.util.UUID;


/**
 * @author 邓超
 * @description UUID工具类
 * @create 2018/9/19
 */
public class UUIDUtil {
    public static String uuid() {
        //生成一个UUID作为cookies里的token java.util.UUID生成的UUID带 "-" 去掉
        return UUID.randomUUID().toString().replace("-", "");
    }
}
