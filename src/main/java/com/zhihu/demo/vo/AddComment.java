package com.zhihu.demo.vo;

import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;

public class AddComment {
    private String content;
    private Integer u_id;
    private Integer q_id;


    private Integer cnum;

    public Comment getComment(){
        Comment comment = new Comment();
        comment.setQ_id(q_id);
        comment.setU_id(u_id);
        comment.setContent(content);
        return comment;
    }
    public Question getQuestion(){
        Question question = new Question();
        question.setQ_id(q_id);
        question.setCnum(cnum);
        return question;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getU_id() {
        return u_id;
    }

    public void setU_id(Integer u_id) {
        this.u_id = u_id;
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
