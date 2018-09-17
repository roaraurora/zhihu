package com.zhihu.demo.service;

import com.zhihu.demo.dao.CommentDao;
import com.zhihu.demo.dao.QuestionDao;
import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;

@Service
public class CommentService {
    @Resource(name = "CommentMapper")
    private CommentDao commentDao;
    @Resource(name = "QuestionMapper")
    private QuestionDao questionDao;
    @Autowired
    private QuestionService questionService;

    /**
     * 根据问题找到对应的评论
     * @param q_id  问题的id
     * @return 对应的评论
     */
    public List<Comment> queryCommentByQid(int q_id) {
        return commentDao.queryCommentByQid(q_id);
    }

    /**
     * 找到自己的评论及所属的问题
     * @param u_id 用户id
     * @return  返回一个评论与问题对应的map
     */
    public List<List> queryCommentByUid(int u_id) {
        List<List> lists = new ArrayList<List>();
        List<Comment> commentList = commentDao.queryCommentByUid(u_id);
        ListIterator<Comment> listIterator = commentList.listIterator();
        List<Question> questionList = new ArrayList<Question>();
        while (listIterator.hasNext()){
            Comment comment = listIterator.next();
            Question question = questionDao.queryQuestionByq_id(comment.getQ_id());
            questionList.add(question);
        }
        lists.add(commentList);
        lists.add(questionList);
        return  lists;
    }

    /**
     * 添加评论
     * @param comment 要添加的评论
     * @param question 所要添加评论的问题
     * @return 是否成功
     */
    @Transactional
    public Result<Question> addComment(Comment comment, Question question) {
        if (comment.getContent() != null && !"".equals(comment.getContent())) {
            comment.setRelease_time(new Date());
            comment.setPnum(0);
            try {
                int effectedNum = commentDao.insertComment(comment);
                if (effectedNum > 0) {
                    // 添加评论成功后，还要修改该问题的评论数
                    return questionService.modifyQuestion(question);
                } else {
                    throw new GlobalException(CodeMsg.INSERT_COMMENT_ERROR);
                }
            } catch (Exception e) {
                throw new GlobalException(new CodeMsg(-1, e.getMessage()));
            }
        } else {
            throw new GlobalException(CodeMsg.COMMENT_IS_NULL);
        }
    }

    /**
     * 删除评论和修改对应评论上的评论数
     * @param comment 要删除的评论
     * @param question 对应的问题
     * @return
     */
    @Transactional
    public Result<Question> deleteComment(Comment comment, Question question) {
        if (comment.getC_id() > 0) {
            try {
                int effectedNum = commentDao.deleteComment(comment.getC_id());
                if (effectedNum > 0) {
                    return questionService.modifyQuestion(question);
                } else {
                    throw new GlobalException(CodeMsg.DELETE_COMMENT_ERROR);
                }
            } catch (Exception e) {
                throw new GlobalException(new CodeMsg(-1, e.getMessage()));
            }
        } else {
            throw new GlobalException(CodeMsg.DELETE_COMMENT_ID_ERROR_);
        }
    }

    /**
     * 修改点赞数
     * @param comment
     * @return 是否成功
     */
    @Transactional
    public Result<Comment> modifyComment(Comment comment) {
        if (comment.getC_id() > 0) {
            try {
                int effectedNum = commentDao.updataComment(comment);
                if (effectedNum > 0) {
                    return Result.success(comment);
                } else {
                    throw new GlobalException(CodeMsg.MODIFY_COMMENT_PNUM_ERROR);
                }
            } catch (Exception e) {
                throw new GlobalException(new CodeMsg(-1,e.getMessage()));
            }

        } else {
            throw new GlobalException(CodeMsg.MODIFY_ID_ERROR);
        }
    }
}
