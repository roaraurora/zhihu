package com.zhihu.demo.vo;

import com.zhihu.demo.model.Question;

public class QuestionVo extends Question {

    private boolean isFollowed;

    private boolean isCollect;

    public  QuestionVo(boolean isFollowed, boolean isCollect, Question question) {
        this.setCnum(question.getCnum());
        this.setqId(question.getqId());
        this.setReleaseTime(question.getReleaseTime());
        this.setUserId(question.getUserId());
        this.setContent(question.getContent());
        this.setLnum(question.getLnum());
        this.setPnum(question.getPnum());
        this.setTitle(question.getTitle());
        this.setUsername(question.getUsername());
        this.isFollowed = isFollowed;
        this.isCollect = isCollect;
    }



    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }
}
