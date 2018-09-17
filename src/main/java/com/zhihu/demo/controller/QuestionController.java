package com.zhihu.demo.controller;

import com.zhihu.demo.model.Question;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    /**
     * 检索所有的问题  测
     * @return 包含问题列表的result    注意列表可能为空
     */
    @GetMapping("/getquestionlist")
    public Result<List<Question>> getQuestionList(){
        return Result.success(questionService.getQuestionList());
    }

    /**
     * 根据用户id检索用户发布的问题    测
     * @param id  包含问题列表的result    注意列表可能为空
     * @return
     */
    @GetMapping("/getquestionlistbyuid/{uid}")
    public Result<List<Question>> getQuestionListByUID(@PathVariable("uid") Integer id){
        return Result.success(questionService.getQuestionListByUID(id));
    }

    /**
     * 增加一个问题   测
     * @param question  要增加的问题对象
     * @return  返回一个result  增加成功的话data为要增加的对象，失败就失败
     * */
    @PostMapping("/addquestion")
    public Result<Question> addQuestion( @Valid Question question){
        return questionService.addQuestion(question);
    }

    /**
     * 根据问题的id删除问起   测
     * @param id  问题的id
     * @return  返回一个result  删除成功的话data为要增加的对象，失败就失败
     */
    @GetMapping("/deletequestion/{qid}")
    public Result<Object> deleteQuestion(@PathVariable("qid") Integer id){
        return questionService.deleteQuestion(id);
    }

    /**
     * 修改评论或者点赞数
     * @param question   要被修改的问题对象
     * @return  返回一个result  修改成功的话data为要增加的对象，失败就失败
     */
    @PostMapping("/modifyquestion")
    public Result<Question> modifyQuestion(@Valid Question question){
        return questionService.modifyQuestion(question);
    }









}
