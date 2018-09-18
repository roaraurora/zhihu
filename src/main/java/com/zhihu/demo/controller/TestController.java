package com.zhihu.demo.controller;

import com.alibaba.fastjson.JSON;
import com.zhihu.demo.config.RabbitConfig;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.SessionKey;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.MailService;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.vo.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
public class TestController {

    private final MailService mailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RabbitTemplate rabbitTemplate;

    private UserService userService;

    private RedisService redisService;

    private AmqpTemplate amqpTemplate;

    @Autowired
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public TestController(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping("/mail")
    public Result<String> testMailContrller() {
        mailService.sendPlainTextMail("83080779@qq.com", "Test mail from Spring Boot Project", "o ha yo");
        return Result.success("test mail");
    }


    @RequestMapping("/books")
    public void defaultMessage() {
        Book book = new Book();
        book.setId("1");
        book.setName("延迟队列");
        //将消息发送到交换器REGISTER_DELAY_EXCHANGE上并使用DELAY_ROUTING_KEY作为路由键 设置过期时间为5秒
        this.rabbitTemplate.convertAndSend(RabbitConfig.REGISTER_DELAY_EXCHANGE, RabbitConfig.DELAY_ROUTING_KEY, book, message -> {
            message.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Book.class.getName());
            message.getMessageProperties().setExpiration(5 * 1000 + "");
            return message;
        });
        logger.info("[发送时间] - [{}]", LocalDateTime.now());
    }

    /**
     * /exchange/exchangename/[routing_key]
     */
    @RequestMapping("/exchange")
    @SendTo("/exchange/stomp.exchange/get-response")
    public Result<Boolean> testExchange() {
        return Result.success(true);
    }

    @RequestMapping("/defaultExchange")
    @SendTo("/queue/stomp-queue")
    public Result<Boolean> defaultExchange() {
        return Result.success(true);
    }

    @MessageMapping("/gotu")
    @SendTo("/topic/mural")
    public Book topicExchange() {
        Book book = new Book();
        book.setId("123");
        book.setName("undead book");
        return book;
    }

    @RequestMapping("/send2user")
    public Result<Boolean> sendTo() {
        String userId = userService.getUserIdFromSecurity();
        String sessionId = redisService.get(SessionKey.getById, userId, String.class);
        // 生成路由键值，生成规则如下: websocket订阅的目的地 + "-user" + websocket的sessionId值。生成值类似:
        String routingKey = getTopicRoutingKey("demo", sessionId); //"demo-user"+sessionId
        // 向amq.topic交换机发送消息，路由键为routingKey
        logger.info("向用户[{}]sessionId=[{}]，发送消息[{}]，路由键[{}]", userId, sessionId, sessionId, routingKey);
        Book book = new Book();
        book.setName("UnDead-Book");
        book.setId("123");
        amqpTemplate.convertAndSend("amq.topic",routingKey, JSON.toJSONString(book));
        return Result.success(true);
    }

    private String getTopicRoutingKey(String actualDestination, String sessionId) {
        return actualDestination + "-user" + sessionId;
    }
}
