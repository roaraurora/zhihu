package com.zhihu.demo.vo;

import javax.validation.constraints.NotNull;

public class RespMessageVo {

    private String message;

    @NotNull
    private int fromId;
    //todo 头像

    private String fromName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public RespMessageVo() {
    }

    public RespMessageVo(String message, @NotNull int fromId) {
        this.message = message;
        this.fromId = fromId;
    }

    @Override
    public String toString() {
        return "RespMessageVo{" +
                "message='" + message + '\'' +
                ", fromId=" + fromId +
                '}';
    }
}
