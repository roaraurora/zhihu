package com.zhihu.demo.redis;

/**
 * @author 邓超
 * @description AnswerKey redis前缀
 * @create 2018/9/15
 */
public class AnswerKey extends BasePrefix {
    private AnswerKey(String prefix) {
        super(prefix);
    }

    public static AnswerKey like = new AnswerKey("like"); //根据用户的ID存储sessionId
}
