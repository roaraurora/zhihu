package com.zhihu.demo.service;

import com.zhihu.demo.model.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;
    @Test
    public void getQuestionList() {
    }

    @Test
    public void getQuestionListByUID() {
    }

    @Test
    public void addQuestion() {
        Question question = new Question();
        question.setContent("sunday");
        question.setUserId(3);
        questionService.addQuestion(question);

    }

    @Test
    public void deleteQuestion() {
    }

    @Test
    public void modifyQuestion() {
    }
}