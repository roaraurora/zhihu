package com.zhihu.demo.redis;

public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        //访问权限设置为private 防止被其他类实例化
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey followKey = new UserKey("follow");
    public static UserKey fansKey = new UserKey("fans");
    public static UserKey messageKey = new UserKey("message");
}
