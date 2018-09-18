package com.zhihu.demo.event.eventListener;

import com.zhihu.demo.event.CheckMessageEvent;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.UserKey;
import com.zhihu.demo.service.MessageService;
import com.zhihu.demo.vo.RespMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckMessageEventListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MessageService messageService;

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @EventListener(CheckMessageEvent.class)
    public void notifyUser(CheckMessageEvent event) {
        logger.info("监听到CheckMessageEvent事件 检查并发送用户{}的未读消息",event.getUserId());
        List<RespMessageVo> respList = redisService.getList(UserKey.messageKey, event.getUserId(), RespMessageVo.class);
        if (respList != null && respList.size() != 0) {
            messageService.sendListToUser(event.getSessionId(), respList);
            redisService.delete(UserKey.messageKey, event.getUserId()); //将未读消息缓存删除
        }
    }
}
