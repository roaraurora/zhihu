package com.zhihu.demo.vo;

import javax.validation.constraints.NotNull;

/**
 * 点赞: 回答
 * 关注: 用户
 * 收藏: 问题
 */
public class RelVo {

    @NotNull
    private String subjectId;


    @NotNull
    private boolean rel;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }

    @Override
    public String toString() {
        return "RelVo{" +
                "subjectId='" + subjectId + '\'' +
                ", rel=" + rel +
                '}';
    }
}
