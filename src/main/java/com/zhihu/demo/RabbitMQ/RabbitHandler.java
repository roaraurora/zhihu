package com.zhihu.demo.rabbitMQ;

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

    /**
     * 若队列DELAY_ROUTING_KEY没有被消费者订阅 则消息会被转发至REGISTER_QUEUE_NAME 并被此函数处理
     * 通过yml 开启手动ACK 如果是自动ACK spring data amqp 会在消息消费完毕后自动去ACk
     * 当出现异常时 消息不会丢失 但会被无限循环消费 一直报错 如果开启错误日志 有可能将磁盘空间耗完
     * 解决方案 1.手动ACK 2.在try-catch中将消息转移到其他的队列
     * @param book    消息对象
     * @param message spring-AMQP为了便于快速操作 body and properties 而定义的对象
     * @param channel 信道 多个个线程可以访问一个socket连接 每个线程对应一个信道
     */
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

    /**
     * 订阅了延迟队列DELAY_ROUTING_KEY 消息没有成为死信
     */
//    @RabbitListener(queues = {RabbitConfig.DELAY_ROUTING_KEY})
    public void listenerQueue(Book book, Message message, Channel channel) {
        logger.info("[listenerDelayQueue 监听的消息] - [消费时间] - [{}] - [{}]", LocalDateTime.now(), book.toString());
        try {
            //通知 MQ 消息已经被成功消费 可以ACK了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            //如果报错则进行容错处理 比如转移当前消息进入其他队列
        }
    }

    /**
     * 订阅邮件消息
     */
    @RabbitListener(queues = {RabbitConfig.MAIL_QUEUE_NAME})
    public void listenerMailQueue(String mail, Message message, Channel channel) {
        logger.info("[listenerDelayQueue 监听的消息] - [消费时间] - [{}] - [{}]", LocalDateTime.now(), mail);
        try {
            MailVo mailVo = JSON.parseObject(mail, MailVo.class);
            userService.sendActiveMail(mailVo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            //todo 将发送失败的邮件 重新返回消息队列
            try {
                //重新发送 basic.nack basic.reject basic.recover
                // https://stackoverflow.com/questions/24107913/how-to-requeue-messages-in-rabbitmq
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
