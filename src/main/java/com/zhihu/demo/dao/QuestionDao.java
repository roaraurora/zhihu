package com.zhihu.demo.dao;

import com.zhihu.demo.model.Question;

import java.util.List;

public interface QuestionDao {
    /**
     * 查询所有发布的问题
     * @return  返回所有发布的问题
     */
    List<Question> queryQuestion();

    /**
     * 根据用户id查询用户个人发布的问题
     * @param u_id 用户id
     * @return 用户发布的问题
     */
    List<Question> queryQuestionByu_id(int u_id);

    /**
     * 根据问题id查找问题
     * @param q_id 问题id
     * @return 问题对象
     */
    Question queryQuestionByq_id(int q_id);
    /**
     * 存入一个问题
     * @param Question 要存的问题
     * @return 被影响的行数
     */
    int insertQuestion(Question Question);

//    int updataQuestion(Question Question);

    /**
     * 删除一个问题
     * @param q_id 被删除问题的id
     * @return 被影响的行数
     */
    int deleteQuestion(int q_id);

    /**
     * x修改问题的  可能有评论数和点赞数
     * @param question   问题的信息
     * @return  返回被影响的行数
     */
    int updataQuestion(Question question);
}
