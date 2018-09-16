package com.zhihu.demo.RabbitMQ;

import com.alibaba.fastjson.JSON;
import com.zhihu.demo.config.RabbitConfig;
import com.zhihu.demo.vo.Book;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.vo.MailVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RabbitHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = {RabbitConfig.REGISTER_QUEUE_NAME})
    public void listenerDelayQueue(Book book, Message message, Channel channel) {
        logger.info("[listenerDelayQueue 监听的消息] - [消费时间] - [{}] - [{}]", LocalDateTime.now(), book.toString());
        try {
            //通知 MQ 消息已经被成功消费 可以ACK了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            //如果报错则进行容错处理 比如转移当前消息进入其他队列
        }
    }

    @RabbitListener(queues = {RabbitConfig.MAIL_QUEUE_NAME})
    public void listenerMailQueue(String mail, Message message,Channel channel) {
        logger.info("[listenerDelayQueue 监听的消息] - [消费时间] - [{}] - [{}]", LocalDateTime.now(), mail);
        try {
            MailVo mailVo = JSON.parseObject(mail, MailVo.class);
            userService.sendActiveMail(mailVo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            //todo 1.异常处理
        }

    }

}
