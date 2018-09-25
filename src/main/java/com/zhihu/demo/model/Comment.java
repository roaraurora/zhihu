package com.zhihu.demo.model;

import java.util.Date;

public class Comment {
    private Integer cId;
    private Integer qId;
    private  Integer userId;
    private Date releaseTime;
    private String content;
    private Integer pnum;
    private String username;

    private long totalNum;

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public Integer getqId() {
        return qId;
    }

    public void setqId(Integer qId) {
        this.qId = qId;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "cId=" + cId +
                ", qId=" + qId +
                ", userId=" + userId +
                ", releaseTime=" + releaseTime +
                ", content='" + content + '\'' +
                ", pnum=" + pnum +
                ", username='" + username + '\'' +
                '}';
    }
}
