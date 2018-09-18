package com.zhihu.demo.controller;

import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.RelationService;
import com.zhihu.demo.vo.LikeVo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.spi.DirStateFactory;
import javax.validation.Valid;

/**
 * 点赞 关注 邀请 收藏
 */
@RestController
@RequestMapping("/relation")
public class RelationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RelationService relationService;

    @Autowired
    public void setRelationService(RelationService relationService) {
        this.relationService = relationService;
    }

    /**
     * 点赞 需要是否点赞 点赞/取消 点赞排序 主体:回答
     * bit map
     */
    @PostMapping("/like")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "post"})
    public Result<Boolean> likeAnswer(@RequestBody @Valid LikeVo likeVo) {
        relationService.setLikeAnswer(likeVo);
        return Result.success(true);
    }

    /**
     * 关注 关注列表 关注/取消 是否关注
     * sort set
     */

    /**
     * 邀请 邀请-通知 和未读消息一套系统
     * list
     */
    /**
     * 收藏 同关注
     * sort set
     */

}
