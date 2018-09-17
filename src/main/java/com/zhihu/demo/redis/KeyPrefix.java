package com.zhihu.demo.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
