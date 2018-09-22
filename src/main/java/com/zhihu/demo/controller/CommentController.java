package com.zhihu.demo.controller;

import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.CommentService;
import com.zhihu.demo.vo.AddComment;
import com.zhihu.demo.vo.DeleteComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 根据问题的id查询对应的评论   测
     * @param qid  问题id
     * @return  对应的评论集
     */
    /**
     * 根据问题的id查询对应的评论   测
     * @param qid   问题id
     * @param pageNum   要访问的页数
     * @param pageSize  页面的大小
     * @return
     */
    @GetMapping("/querycommentbyqid/{qid}")
    public Result<List<Comment>> queryCommentByQid(@PathVariable("qid") Integer qid,
                                                   @PathVariable("qid") Integer pageNum,
                                                   @PathVariable("qid") Integer pageSize) {
        return Result.success(commentService.queryCommentByQid(qid,pageNum,pageSize));
    }

    /**
     * 根据用户的id查询评论   测
     * @param uid  用户id
     * @return  对应的评论集
     */
    @GetMapping("/querycommentbyuid/{uid}")
    public Result<List<List>> queryCommentByUid(@PathVariable("uid") Integer uid) {
        return Result.success(commentService.queryCommentByUid(uid));
    }

    /**
     * 添加一个评论  同时修改对应问题的评论数    测
     * @param addComment  要添加的评论包括 content u_id q_id cnum
     * @return 成功的话result里面的data是 修改了评论数的问题对象， 失败就没有
     */
    @PostMapping("/addcomment")
    public Result<Question> addComment(@Valid AddComment addComment){
        return commentService.addComment(addComment.getComment(),addComment.getQuestion());
    }

    /**
     * 删除一个评论，同时修改对应问题的评论    测
     * @param deleteComment  要添加的评论包括  u_id q_id cnum
     * @return  成功的话result里面的data是 修改了评论数的问题对象， 失败就没有
     */
    @PostMapping("/deletecomment")
    public Result<Question> deleteComment(@Valid DeleteComment deleteComment){
        return commentService.deleteComment(deleteComment.getComment(), deleteComment.getQuestion());
    }

    /**
     * 修改评论的点赞数     测
     * @param comment   要修改的评论    就传入id 和点赞数
     * @return  成功的话result里面的data就是这个评论对象
     */
    @PostMapping("/modifycomment")
    public Result<Comment> modifyComment(@Valid Comment comment){
        return commentService.modifyComment(comment);
    }

}
