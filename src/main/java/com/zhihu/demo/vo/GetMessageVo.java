package com.zhihu.demo.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class GetMessageVo {
    @NotNull
    private String receiverId;

    @Max(5)
    @NotNull
    private int offset;

    @Override
    public String toString() {
        return "GetMessageVo{" +
                ", receiverId='" + receiverId + '\'' +
                ", offset='" + offset + '\'' +
                '}';
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
