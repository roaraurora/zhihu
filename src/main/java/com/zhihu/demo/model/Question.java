package com.zhihu.demo.model;

import java.util.Date;

public class Question {
    private Integer qId;
    private Date releaseTime;
    private String content;
    private Integer pnum;
    private Integer cnum;
    private Integer userId;
    private String title;
    private Integer lnum;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLnum() {
        return lnum;
    }

    public void setLnum(Integer lnum) {
        this.lnum = lnum;
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

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Question{" +
                "qId=" + qId +
                ", releaseTime=" + releaseTime +
                ", content='" + content + '\'' +
                ", pnum=" + pnum +
                ", cnum=" + cnum +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", lnum=" + lnum +
                '}';
    }
}
