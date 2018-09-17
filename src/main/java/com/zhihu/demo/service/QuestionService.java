package com.zhihu.demo.service;

import com.zhihu.demo.dao.QuestionDao;
import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.Question;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    private QuestionDao questionDao;

    /**
     * 检索所有的问题
     * @return  所有问题
     */
    public List<Question> getQuestionList() {
        return questionDao.queryQuestion();
    }

    /**
     * 检索个人的评论表
     * @param userId  用户的id
     * @return 对应的评论表
     */
    public List<Question> getQuestionListByUID(int userId) {
        return questionDao.queryQuestionByu_id(userId);
    }

    /**
     * 发布一个问题
     * @param question  要发布的问题
     * @return  是否成功
     */
    @Transactional
    public Result<Question> addQuestion(Question question) {
        if(question.getContent()!=null&&!"".equals(question.getContent())){
            question.setRelease_time(new Date());
            try {
                int effectedNum = questionDao.insertQuestion(question);
                if (effectedNum > 0) {
                    return Result.success(question);
                } else {
                    throw new GlobalException(CodeMsg.INSERT_QUESTION_ERROR);
                }
            }catch (Exception e){
                throw new GlobalException(new CodeMsg(-1,e.getMessage()));
            }

        }else {
            throw new GlobalException(CodeMsg.QUESTION_IS_NULL);
        }
    }

    /**
     * 删除个人的问题
     * @param q_id   要删除的问题id
     * @return  返回是否成功
     */
    @Transactional
    public Result<Object> deleteQuestion(int q_id) {
        if(q_id>0){

            try {
                int effectedNum = questionDao.deleteQuestion(q_id);
                if (effectedNum > 0) {
                    return Result.success(null);
                } else {
                    throw new GlobalException(CodeMsg.DELETE_QUESTION_ERROR);
                }
            }catch (Exception e){
                throw new GlobalException(new CodeMsg(-1,e.getMessage()));
            }

        }else {
            throw new GlobalException(CodeMsg.DELETE_QUESTION_ID_ERROR_);
        }
    }

    /**
     * 更新问题的评论数或者点赞数，
     * @param question  要更新的问题
     * @return 是否成功
     */
    public Result<Question> modifyQuestion(Question question){
        if(question.getQ_id()>0){

            try {
                int effectedNum = questionDao.updataQuestion(question);
                if (effectedNum > 0) {
                    return Result.success(question);
                } else {
                    throw new GlobalException(CodeMsg.MODIFY_QUESTION_ERROR);
                }
            }catch (Exception e){
                throw new GlobalException(new CodeMsg(-1,e.getMessage()));
            }

        }else {
            throw new GlobalException(CodeMsg.MODIFY_QUESTOPM_ID_ERROR);
        }
    }

}
