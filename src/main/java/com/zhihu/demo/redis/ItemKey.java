package com.zhihu.demo.redis;

public class ItemKey extends BasePrefix {
    private ItemKey(String prefix) {
        super(prefix);
    }

    public static ItemKey getById = new ItemKey("id"); //根据用户的ID存储sessionId
    public static ItemKey collect = new ItemKey("collect"); //存储用户收藏
}
