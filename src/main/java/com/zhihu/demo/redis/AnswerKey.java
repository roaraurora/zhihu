package com.zhihu.demo.redis;

public class AnswerKey extends BasePrefix {
    private AnswerKey(String prefix) {
        super(prefix);
    }

    public static AnswerKey like = new AnswerKey("like"); //根据用户的ID存储sessionId
}
