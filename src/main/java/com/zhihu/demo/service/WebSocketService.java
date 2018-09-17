package com.zhihu.demo.service;

import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.vo.ReqMessageVo;
import com.zhihu.demo.vo.RespMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class WebSocketService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SimpMessagingTemplate simpMessagingTemplate;

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendPrivateMessage(ReqMessageVo messageVo, Principal principal) {
        String userId = Integer.toString(messageVo.getSubjectId());
        String message = messageVo.getMessage();
        RespMessageVo respMessageVo = new RespMessageVo(message, Integer.valueOf(principal.getName()));
        simpMessagingTemplate.convertAndSendToUser(userId, "/queue/getResponse", redisService.beanToString(respMessageVo));
        // 3 如何判断一个用户是否在线 订阅了WAZ vc zerstdughy cxszAzxCDXZZebSocket

    }

}
