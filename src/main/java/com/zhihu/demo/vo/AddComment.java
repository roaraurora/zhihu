package com.zhihu.demo.vo;

import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;
import com.zhihu.demo.service.UserService;

public class AddComment {
    private String content;
    private Integer qId;
    private Integer cnum;

    public Comment getComment(){
        UserService userService = new UserService();
        Comment comment = new Comment();
        comment.setqId(qId);
        comment.setUserId(Integer.parseInt(userService.getUserIdFromSecurity()));
        comment.setContent(content);
        return comment;
    }
    public Question getQuestion(){
        Question question = new Question();
        question.setqId(qId);
        question.setCnum(cnum);
        return question;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
