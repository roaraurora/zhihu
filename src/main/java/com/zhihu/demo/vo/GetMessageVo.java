package com.zhihu.demo.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class GetMessageVo {
    @NotNull
    private String senderId;

    @NotNull
    private String receiverId;

    @Max(5)
    @NotNull
    private int offset;

    @Override
    public String toString() {
        return "GetMessageVo{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", offset='" + offset + '\'' +
                '}';
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
