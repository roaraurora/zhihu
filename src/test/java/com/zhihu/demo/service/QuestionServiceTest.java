package com.zhihu.demo.service;

import io.swagger.models.auth.In;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceTest {
    @Autowired
    QuestionService questionService;
    @Test
    public void getQuestionListByUID() {

    }

    @Test
    public void getQuestionByQids() {
        Set<String> s = new HashSet<String>();
        s.add("1060");
        s.add("1061");
        s.add("1062");
        List<String> list1 = new ArrayList<String>(s);
        List<Integer> list2 = null;
        list2 = parseIntegersList(list1);
        int i = questionService.getQuestionByQids(list2).getData().size();
        Assert.assertEquals(2,i);
    }
    private List<Integer> parseIntegersList(List<String> StringList) {
        List<Integer> IntegerList = new ArrayList<Integer>();
        for (String x : StringList) {
            Integer z = Integer.parseInt(x);
            IntegerList.add(z);
        }      	return IntegerList;
    }
}