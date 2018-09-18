package com.zhihu.demo.service;

import com.zhihu.demo.redis.AnswerKey;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.RelRedisService;
import com.zhihu.demo.util.ConstantBean;
import com.zhihu.demo.vo.LikeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisService redisService;

    private RelRedisService relRedisService;

    private UserService userService;

    private ConstantBean constantBean;

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
    public void setLikeAnswer(LikeVo likeVo) {
        String answerId = likeVo.getAnswerId();
        boolean like = likeVo.isLike();
        long id = Long.parseLong(userService.getUserIdFromSecurity())-constantBean.getUserseqstart();
        relRedisService.setBit(AnswerKey.like, answerId, id, like);
    }

    /**
     * 取得user对answer的点赞动作
     *
     * @param userId   user
     * @param answerId answer
     */
    public boolean isLikeAnswer(String userId, String answerId) {
        return relRedisService.getBit(AnswerKey.like, answerId, Long.parseLong(userId)-constantBean.getUserseqstart());
    }

    /**
     * 获得一个回答的总赞数
     *
     * @param answerId asnwer
     */
    public long countLikeAnswer(String answerId) {
        return relRedisService.getCount(AnswerKey.like, answerId);
    }
}
