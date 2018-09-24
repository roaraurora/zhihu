package com.zhihu.demo.vo;

import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;

public class DeleteComment {
    private Integer c_id;
    private Integer q_id;
    private Integer cnum;

    public Comment getComment(){
        Comment comment = new Comment();
        comment.setC_id(c_id);
        return comment;
    }
    public Question getQuestion(){
        Question question = new Question();
        question.setQ_id(q_id);
        question.setCnum(cnum);
        return question;
    }

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public Integer getQ_id() {
        return q_id;
    }

    public void setQ_id(Integer q_id) {
        this.q_id = q_id;
    }

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }
}
