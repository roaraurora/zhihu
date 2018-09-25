package com.zhihu.demo.dao;

import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentDaoTest {
    private CommentDao commentDao;
    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }
    @Test
    public void queryCommentByQid() {
        List<Comment> list = commentDao.queryCommentByQid(5);
        assertEquals(0,list.size());

    }

    @Test
    public void queryCommentByUid() {
        List<Comment> list = commentDao.queryCommentByUid(1);
        assertEquals(3,list.size());
    }

    @Test
    public void insertComment() {
        Comment comment1 = new Comment();

        comment1.setUserId(1);
        comment1.setqId(1);
        comment1.setContent("this is a comment");
        comment1.setPnum(1);
        comment1.setReleaseTime(new Date());
        int i;
        commentDao.insertComment(comment1);
//        assertEquals(1, i);
    }

    @Test
    public void deleteComment() {
        int i = commentDao.deleteComment(23);
        assertEquals(0,i);
    }
    @Test
    public void updataComment(){
        Comment comment1 = new Comment();

        comment1.setcId(1);
//        comment1.setU_id(1);
//        comment1.setQ_id(1);
//        comment1.setContent("this is a comment");
        comment1.setPnum(3);
//        comment1.setRelease_time(new Date());
        int i;
        i =commentDao.updataComment(comment1);
        assertEquals(1,i);
    }
}