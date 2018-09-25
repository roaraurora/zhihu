package com.zhihu.demo.vo;

import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;

public class DeleteComment {
    private Integer cId;
    private Integer qId;
    private Integer cnum;

    public Comment getComment(){
        Comment comment = new Comment();
        comment.setcId(cId);
        return comment;
    }
    public Question getQuestion(){
        Question question = new Question();
        question.setqId(qId);
        question.setCnum(cnum);
        return question;
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

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }
}
