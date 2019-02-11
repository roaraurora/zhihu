package com.zhihu.demo.redis;

/**
 * @author 邓超
 * @description  redis前缀接口
 * @create 2018/9/15
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
