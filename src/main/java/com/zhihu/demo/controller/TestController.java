package com.zhihu.demo.controller;

import com.alibaba.fastjson.JSON;
import com.zhihu.demo.config.RabbitConfig;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.ItemKey;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.MailService;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.util.ConstantBean;
import com.zhihu.demo.util.GeneralUtils;
import com.zhihu.demo.util.SinaPicBedUtil;
import com.zhihu.demo.vo.Book;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final MailService mailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RabbitTemplate rabbitTemplate;

    private UserService userService;

    private RedisService redisService;

    private AmqpTemplate amqpTemplate;

    private ConstantBean constantBean;

    @Autowired
    public void setConstantBean(ConstantBean constantBean) {
        this.constantBean = constantBean;
    }

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

    @PostMapping("/mail")
    public Result<String> testMailContrller() {
        mailService.sendPlainTextMail("83080779@qq.com", "Test mail from Spring Boot Project", "o ha yo");
        return Result.success("test mail");
    }


    @PostMapping("/books")
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
    @PostMapping("/exchange")
    @SendTo("/exchange/stomp.exchange/get-response")
    public Result<Boolean> testExchange() {
        return Result.success(true);
    }

    @PostMapping("/defaultExchange")
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

    @PostMapping("/send2user")
    public Result<Boolean> sendTo() {
        String userId = userService.getUserIdFromSecurity();
        String sessionId = redisService.get(ItemKey.getById, userId, String.class);
        // 生成路由键值，生成规则如下: websocket订阅的目的地 + "-user" + websocket的sessionId值。生成值类似:
        String routingKey = getTopicRoutingKey("demo", sessionId); //"demo-user"+sessionId
        // 向amq.topic交换机发送消息，路由键为routingKey
        logger.info("向用户[{}]sessionId=[{}]，发送消息[{}]，路由键[{}]", userId, sessionId, sessionId, routingKey);
        Book book = new Book();
        book.setName("UnDead-Book");
        book.setId("123");
        amqpTemplate.convertAndSend("amq.topic", routingKey, JSON.toJSONString(book));
        return Result.success(true);
    }

    private String getTopicRoutingKey(String actualDestination, String sessionId) {
        return actualDestination + "-user" + sessionId;
    }

    @PostMapping("/sina/upload")
    @ResponseBody
    public Result<List<String>> upload(@RequestParam("file") MultipartFile[] multipartFiles,
                                       @RequestParam(value = "size", required = false, defaultValue = "0") Integer size) throws Exception {
        String base64name = GeneralUtils.base64Encode(constantBean.getSinausername());
        String cookies = SinaPicBedUtil.getSinaCookie(base64name, constantBean.getSinapassword());// 持久化起来 不用每次都登录 一般失效7天左右
        List<String> url = SinaPicBedUtil.uploadFile(multipartFiles, cookies, size);
        return Result.success(url);
    }
}
