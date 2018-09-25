package com.zhihu.demo.service;

import com.zhihu.demo.model.Question;
import com.zhihu.demo.redis.*;
import com.zhihu.demo.util.ConstantBean;
import com.zhihu.demo.vo.NeterVo;
import com.zhihu.demo.vo.PageVo;
import com.zhihu.demo.vo.RelVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.nio.ch.Net;

import java.util.Set;

@Service
public class RelationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisService redisService;

    private RelRedisService relRedisService;

    private UserService userService;

    private ConstantBean constantBean;

    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Autowired
    public void setConstantBean(ConstantBean constantBean) {
        this.constantBean = constantBean;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRelRedisService(RelRedisService relRedisService) {
        this.relRedisService = relRedisService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }


    /**
     * 设置user对answer的点赞动作
     *
     * @param likeVo like data object
     */
    public void setLikeAnswer(RelVo likeVo) {
        String answerId = likeVo.getSubjectId();
        boolean like = likeVo.isRel();
        long id = Long.parseLong(userService.getUserIdFromSecurity()) - constantBean.getUserseqstart();
        relRedisService.setBit(AnswerKey.like, answerId, id, like);
    }

    /**
     * 取得user对answer的点赞动作
     *
     * @param userId   user
     * @param answerId answer
     */
    public boolean isLikeAnswer(String userId, String answerId) {
        return relRedisService.getBit(AnswerKey.like, answerId, Long.parseLong(userId) - constantBean.getUserseqstart());
    }

    /**
     * 获得一个回答的总赞数
     *
     * @param answerId asnwer
     */
    public long countLikeAnswer(String answerId) {
        return relRedisService.getCount(AnswerKey.like, answerId);
    }

    /**
     * 设置关注用户
     */
    public void setFollowUser(RelVo followVo) {
        String fansId = userService.getUserIdFromSecurity();
        String followerId = followVo.getSubjectId();
        String followerName = userService.getUserById(followerId).getUsername();
        String fansName = userService.getUserById(fansId).getUsername();
        NeterVo follower = new NeterVo(followerId, followerName);
        NeterVo fans = new NeterVo(fansId, fansName);
        if (followVo.isRel()) {
            //设为关注关系
            relRedisService.zaddNet(followerId, fansId, follower, fans);
        } else {
            //取消关注关系
            relRedisService.zremNet(followerId, fansId, follower, fans);
        }
    }

    /**
     * 获得对应页码的关注列表
     */
    public Set<NeterVo> getFollowers(PageVo pageVo) {
        String fansId = userService.getUserIdFromSecurity();
        return relRedisService.zrange(UserKey.followKey, fansId, pageVo.getPage(), pageVo.getOffset(), NeterVo.class);
    }
    /**
     * 获得对应页码的粉丝列表
     */
    public Set<NeterVo> getFans(PageVo pageVo) {
        String followerId = userService.getUserIdFromSecurity();
        return relRedisService.zrange(UserKey.fansKey, followerId, pageVo.getPage(), pageVo.getOffset(), NeterVo.class);
    }
    /**
     * 获得id对应用户对neterVo的关注时间 为null时id不关注neterVo
     */
    public double isFollowsTime(String id, NeterVo neterVo) {
        return relRedisService.zscore(UserKey.followKey, id, neterVo);
    }

    /**
     * 获得id对应用户对neterVo的 被 关注时间 为null时id不被neterVo关注
     */
    public double isFansTime(String id, NeterVo neterVo) {
        return relRedisService.zscore(UserKey.fansKey, id, neterVo);
    }

    /**
     * 获得id对应用户的关注总数
     */
    public long countFollowers(String id) {
        return relRedisService.zcard(UserKey.followKey, id);
    }

    /**
     * 获得id对应用户的粉丝总数
     */
    public long countFans(String id) {
        return relRedisService.zcard(UserKey.fansKey, id);
    }


    public void setCollect(RelVo collectVo) {
        //TODO 等加上 question title字段
        String userId = userService.getUserIdFromSecurity();
        String questionId = collectVo.getSubjectId();
//        String title = questionService.getQuestionById(questionId).get();
//        NeterVo neterVo = new NeterVo(questionId, title);
        if (collectVo.isRel()) {
            //设为关注关系
            relRedisService.zadd(ItemKey.collect, userId, questionId);
        } else {
            //取消关注关系
            relRedisService.zrem(ItemKey.collect, userId, questionId);
        }
    }

    public Set<Question> getCollections(PageVo pageVo) {
        String userId = userService.getUserIdFromSecurity();
        Set<String> idSet =  relRedisService.zrange(ItemKey.collect, userId, pageVo.getPage(), pageVo.getOffset(), String.class);
        return null;
    }

}
