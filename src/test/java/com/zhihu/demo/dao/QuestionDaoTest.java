package com.zhihu.demo.dao;

import com.zhihu.demo.model.Question;
import org.apache.ibatis.session.SqlSession;
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
public class QuestionDaoTest {


    private QuestionDao questionDao;
    @Autowired
    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Test
    public void queryQuestion() {
        List<Question> list= questionDao.queryQuestion();
//        list.get(0).toString();
        assertEquals(1,list.size());
    }

    @Test
    public void queryQuestionByu_id() {
        List<Question> list = questionDao.queryQuestionByu_id(1);
        assertEquals(1,list.size());
    }

    @Test
    public void insertQuestion() {
        Question question = new Question();
        question.setReleaseTime(new Date());
        question.setContent("this is a question555555");
        question.setUserId(3);
        int i= questionDao.insertQuestion(question);
        assertEquals(1,i);

    }

    @Test
    public void deleteQuestion() {
        int i = questionDao.deleteQuestion(1011);
        assertEquals(1,i);
    }

    @Test
    public void updataQuestion(){
        Question question = new Question();

        question.setqId(1);
        question.setCnum(3);
        question.setPnum(3);
        int i;
        i =questionDao.updataQuestion(question);
        assertEquals(1,i);
    }

    @Test
    public void queryQuestionByQid(){
        Question question = questionDao.queryQuestionByq_id(1);
        assertEquals("12312",question.getContent());
    }
}