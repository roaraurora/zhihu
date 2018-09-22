package com.zhihu.demo.vo;

import javax.validation.constraints.NotNull;

public class ReqMessageVo {

    private String message;

    @NotNull
    private int subjectId;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return "ReqMessageVo{" +
                "message='" + message + '\'' +
                ", subjectId=" + subjectId +
                '}';
    }
}
