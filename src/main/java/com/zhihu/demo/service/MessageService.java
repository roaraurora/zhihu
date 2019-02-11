package com.zhihu.demo.service;

import com.alibaba.fastjson.JSON;
import com.zhihu.demo.model.User;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.ItemKey;
import com.zhihu.demo.redis.UserKey;
import com.zhihu.demo.vo.ReqMessageVo;
import com.zhihu.demo.vo.RespMessageVo;
import com.zhihu.demo.vo.GetMessageVo;
import com.zhihu.demo.vo.HistoryMessageVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


/**
 * @author 邓超
 * @description 即时通讯服务类
 * @create 2018/9/19
 */
@Service
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SimpMessagingTemplate simpMessagingTemplate;

    private RedisService redisService;

    private UserService userService;

    private AmqpTemplate amqpTemplate;

    @Autowired
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendPrivateMessage(ReqMessageVo messageVo, Principal principal) {
        String subjectId = String.valueOf(messageVo.getSubjectId());
        User sender = userService.getUserById(principal.getName());
        String userId = String.valueOf(sender.getUserId());
        String message = messageVo.getMessage();
        Long sendTime = System.currentTimeMillis();
        String username = sender.getUsername();
        RespMessageVo respMessageVo = new RespMessageVo(message, userId, sendTime, username); //respMessageVo中只储存sender的信息
        String sessionId = redisService.get(ItemKey.getById, subjectId, String.class); //接收者的sessionId
        if (StringUtils.isEmpty(sessionId)) {
            //未命中redis时 缓存消息信息
            logger.info("未命中redis websocket session");
            redisService.rpush(UserKey.messageKey, subjectId, respMessageVo);
        } else {
            //根据sessionId 发送消息
            logger.info("命中了redis websocket session");
            sendToUser(sessionId, respMessageVo);
        }
        HistoryMessageVo historyMessageVo = new HistoryMessageVo(message, sendTime, subjectId);
        redisService.addHistory(Integer.parseInt(userId), Integer.parseInt(subjectId), historyMessageVo); //存入历史记录
    }

    public void sendToUser(String sessionId, RespMessageVo respMessageVo) {
        //发送一个消息给用户 TODO 如何判断消息是否发送成功
        logger.info("send message => "+respMessageVo);
        String routingKey = getTopicRoutingKey("demo", sessionId); //"demo-user"+sessionId
        amqpTemplate.convertAndSend("amq.topic", routingKey, JSON.toJSONString(respMessageVo));
    }

    public void sendListToUser(String sessionId, List<RespMessageVo> respList) {
        //发送多个消息给用户
        String routingKey = getTopicRoutingKey("demo", sessionId); //"demo-user"+sessionId
        logger.info("发送消息[{}]，路由键[{}]", sessionId, sessionId, routingKey);
        amqpTemplate.convertAndSend("amq.topic", routingKey, JSON.toJSONString(respList));
//        amqpTemplate.convertAndSend("amq.topic", routingKey, respList);
    }

    private String getTopicRoutingKey(String actualDestination, String sessionId) {
        return actualDestination + "-user" + sessionId;
    }

    public List<HistoryMessageVo> loadHistory(GetMessageVo getMessageVo) {
        int offset = getMessageVo.getOffset();
        String senderId = userService.getUserIdFromSecurity();
        String receiverId = getMessageVo.getReceiverId();
        return redisService.getHistory(Integer.parseInt(senderId), Integer.parseInt(receiverId), offset, HistoryMessageVo.class);
    }
}
