package com.zhihu.demo.vo;

import javax.validation.constraints.NotNull;

/**
 * 点赞数据
 */
public class LikeVo {

    @NotNull
    private String answerId;

    @NotNull
    private boolean like;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
