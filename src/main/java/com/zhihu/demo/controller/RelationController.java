package com.zhihu.demo.controller;

import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.RelationService;
import com.zhihu.demo.vo.NeterVo;
import com.zhihu.demo.vo.PageVo;
import com.zhihu.demo.vo.RelVo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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
    public Result<Boolean> likeAnswer(@RequestBody @Valid RelVo likeVo) {
        relationService.setLikeAnswer(likeVo);
        return Result.success(true);
    }

    /**
     * 关注 关注列表 关注/取消 是否关注
     * sort set
     */
    @PostMapping("/follow")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "post"})
    public Result<Boolean> followUser(@RequestBody @Valid RelVo followVo) {
        relationService.setFollowUser(followVo);
        return Result.success(true);
    }

    @PostMapping("/followers")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "post"})
    public Result<Set> getFollowers(@RequestBody @Valid PageVo pageVo) {
        Set<NeterVo> set = relationService.getFollowers(pageVo);
        return Result.success(set);
    }


    /**
     * 邀请 邀请-通知 和未读消息一套系统
     * list
     */
    

    /**
     * 收藏 同关注 主体是问题
     * sort set
     */
    @PostMapping("/collect")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "post"})
    public Result<Boolean> collect(@RequestBody @Valid RelVo collectVo) {
        relationService.setCollect(collectVo);
        return Result.success(true);
    }

    @PostMapping("/collections")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "post"})
    public Result<Set> collections(@RequestBody @Valid PageVo pageVo) {
        Set<NeterVo> set = relationService.getCollections(pageVo);
        return Result.success(set);
    }

}
