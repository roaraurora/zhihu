package com.zhihu.demo.redis;

public class SessionKey extends BasePrefix {
    private SessionKey(String prefix) {
        super(prefix);
    }

    public static SessionKey getById = new SessionKey("id"); //根据用户的ID存储sessionId
}
