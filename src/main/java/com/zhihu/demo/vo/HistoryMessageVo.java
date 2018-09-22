package com.zhihu.demo.vo;

public class HistoryMessageVo {
    private String message;

    private Long sendTime;

    private String receiverId;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HistoryMessageVo(String message, Long sendTime, String receiverId) {
        this.message = message;
        this.sendTime = sendTime;
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "RespMessageVo{" +
                "message='" + message + '\'' +
                ", sendTime=" + sendTime +
                ", receiverId='" + receiverId + '\'' +
                '}';
    }
}
