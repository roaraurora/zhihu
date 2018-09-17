package com.zhihu.demo.dao;

import com.zhihu.demo.model.Comment;

import java.util.List;

public interface CommentDao {
    /**
     * 根据发布的问题id查找评论
     * @param q_id  问题的id
     * @return 问题对应的所有评论
     */
    List<Comment> queryCommentByQid(int q_id);

    /**
     * 根据用户的id查找对应的评论
     * @param u_id  用户id
     * @return 用户对应的所有评论
     */
    List<Comment> queryCommentByUid(int u_id);

    /**
     * 存入一个评论
     * @param comment  评论对象
     * @return 被影响的列的行数
     */
    int insertComment(Comment comment);

//    int updataQuestion(Question Question);

    /**
     * 删除一条评论
     * @param c_id  评论的id
     * @return 被影响的列的行数
     */
    int deleteComment(int c_id);

    /**
     * 修改一个评论，目前貌似只能改pnum评论数
     * @param comment  要修改的评论
     * @return 返回被影响的行数
     */
    int updataComment(Comment comment);
}
