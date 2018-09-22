package com.zhihu.demo.vo;

import java.util.Date;

/**
 * 如何在网络请求大小 和网络请求数量之间取得一个平衡
 * 这里选择了 减少请求数量
 * 倘若引入头像 则势必需要减少请求大小？或采用云？
 */
public class RespMessageVo {

    private String message;

    private String senderId;
    //todo 头像
    private Long sendTime;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public RespMessageVo(String message, String fromId, Long sendTime, String username) {
        this.message = message;
        this.senderId = fromId;
        this.sendTime = sendTime;
        this.username = username;
    }

    @Override
    public String toString() {
        return "RespMessageVo{" +
                "message='" + message + '\'' +
                ", senderId=" + senderId +
                ", sendTime=" + sendTime +
                ", username='" + username + '\'' +
                '}';
    }
}
