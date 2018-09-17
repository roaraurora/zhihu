package com.zhihu.demo.model;

import java.util.Date;

public class Question {
    private Integer q_id;
    private Date release_time;
    private String content;
    private Integer pnum;
    private Integer cnum;
    private Integer u_id;

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

    public Integer getU_id() {
        return u_id;
    }

    public void setU_id(Integer u_id) {
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "Question{" +
                "q_id=" + q_id +
                ", release_time=" + release_time +
                ", content='" + content + '\'' +
                ", pnum=" + pnum +
                ", cnum=" + cnum +
                ", u_id=" + u_id +
                '}';
    }
}
