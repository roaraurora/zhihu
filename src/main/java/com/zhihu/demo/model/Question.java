package com.zhihu.demo.model;

import java.util.Date;

public class Question {
    private Integer q_id;
    private Date release_time;
    private String content;
    private Integer pnum;
    private Integer cnum;
    private Integer userId;
    private String title;
    private Integer lnum;

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

    public Integer getQ_id() {
        return q_id;
    }

    public void setQ_id(Integer q_id) {
        this.q_id = q_id;
    }

    public Date getRelease_time() {
        return release_time;
    }

    public void setRelease_time(Date release_time) {
        this.release_time = release_time;
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
                "q_id=" + q_id +
                ", release_time=" + release_time +
                ", content='" + content + '\'' +
                ", pnum=" + pnum +
                ", cnum=" + cnum +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", lnum=" + lnum +
                '}';
    }
}
