package com.zhihu.demo.util;

import com.zhihu.demo.model.Question;
import com.zhihu.demo.redis.ItemKey;
import com.zhihu.demo.redis.RelRedisService;
import com.zhihu.demo.service.RelationService;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.vo.NeterVo;
import com.zhihu.demo.vo.QuestionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WrapUtil {
    private RelRedisService relRedisService;

    private UserService userService;

    private RelationService relationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setRelationService(RelationService relationService) {
        this.relationService = relationService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRelRedisService(RelRedisService relRedisService) {
        this.relRedisService = relRedisService;
    }

    public List<QuestionVo> wrap(List<Question> questionList) {
        List<QuestionVo> questionVoList = new ArrayList<>();
        String userId = userService.getUserIdFromSecurity();
        if (questionList != null) {
            for (Question q : questionList) {
                boolean isC = (relRedisService.zscore(ItemKey.collect, userId, q.getqId()) != null);
                boolean isF = (relationService.isFollowsTime(userId, new NeterVo(String.valueOf(q.getUserId()), q.getUsername())) != null);
                questionVoList.add(new QuestionVo(isF, isC, q));
            }
        }
        return questionVoList;
    }
}
