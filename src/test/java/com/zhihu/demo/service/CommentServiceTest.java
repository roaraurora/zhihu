package com.zhihu.demo.service;

import com.github.pagehelper.PageHelper;
import com.zhihu.demo.dao.CommentDao;
import com.zhihu.demo.model.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {
    @Autowired
    CommentDao commentDao;

    @Test
    public void setQuestionService() {
    }

    @Test
    public void queryCommentByQid() {
        PageHelper.startPage(1,3);
        List<Comment> commentList = commentDao.queryCommentByQid(1);
        assertEquals(4,commentList.size());

    }

    @Test
    public void queryCommentByUid() {
    }

    @Test
    public void addComment() {
    }

    @Test
    public void deleteComment() {
    }

    @Test
    public void modifyComment() {
    }
}