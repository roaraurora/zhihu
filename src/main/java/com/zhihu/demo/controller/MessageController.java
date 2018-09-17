package com.zhihu.demo.controller;

import com.zhihu.demo.service.WebSocketService;
import com.zhihu.demo.vo.ReqMessageVo;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SimpMessagingTemplate simpMessagingTemplate;

    private WebSocketService webSocketService;

    @Resource
    org.apache.shiro.mgt.SecurityManager securityManager;

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * @param message        用于接收完整的消息
     * @param messageHeaders 用于接收消息中的头部信息
     * @param destination    目标url
     * @param headers        使用一个Map接收消息中的所有header
     * @param id             提取Header中的destination模板变量 (类似PathVariable())
     * @param body           请求体
     * @MessageMapping 注解处理用户client发送过来的信息
     */
    @MessageMapping("/test/{id}")
    public void test(Message message,
                     MessageHeaders messageHeaders,
                     @Header("destination") String destination,
                     @Headers Map<String, Object> headers,
                     @DestinationVariable long id,
                     @Payload Map body) {
        logger.info("[test] Message :{}", message);
        logger.info("[test] MessageHeader :{}", messageHeaders);
        logger.info("[test] Header :{}", destination); //  /app/test/567
        logger.info("[test] Headers :{}", headers);
        logger.info("[test] DestinationVariable :{}", id);
        logger.info("[test] Payload :{}", body.size());
    }

    @MessageMapping("/hello")
    public void sayHello(@Payload String body) {
        logger.info("receive message body: {}", body);
        simpMessagingTemplate.convertAndSend("/sub/public", "reply hello");
    }

    @MessageMapping("/hello1")
    @SendTo("/sub/public") //默认将消息发送到传入消息相同的地址
    public String sayHello1(@Payload String body) {
        logger.info("receive message body: {}", body);
        return "reply hello1";
    }

    @MessageMapping("/hello2")
    public void sayHello2(@Payload String body) {
        logger.info("receive message body: {}", body);
        simpMessagingTemplate.convertAndSend("/topic/getResponse", "reply hello2");
    }

    /**
     * 发送给特定的用户
     *
     * @param body      data
     * @param principal accessor.principal
     */
    @MessageMapping("/hello3")
    public void sayHello3(@Payload String body, Principal principal) {
        logger.info("receive message body: {}", body);
        logger.info("receive message principal: {}", principal.getName());
        simpMessagingTemplate.convertAndSendToUser("elder", "/topic/getResponse", "reply hello3");
    }

    @MessageMapping("/hello4")
    @SendToUser("/topic/getResponse")
    public String sayHello4(@Payload String body) {
        logger.info("receive message body: {}", body);
        return "reply hello4";
    }

    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public String say() {
        logger.info("enter say");
        return "welcome";
    }

    @MessageMapping("/message")
    public void sendMessage(@Valid ReqMessageVo messageVo, Principal principal) {
        logger.info("messageVo => " + messageVo.getMessage() + " principal => " + principal.getName());
        webSocketService.sendPrivateMessage(messageVo,principal);
    }
}
