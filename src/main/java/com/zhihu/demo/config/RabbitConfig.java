package com.zhihu.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邓超
 * @description RabbitMQ配置类
 * @create 2018/9/20
 */
@Configuration
public class    RabbitConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> logger.info(" 消息发送成功:correlationData({}),ack({}),cause", connectionFactory, ack, cause));
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> logger.info("消息丢失: exchange({}),route({}),replyCode({}),replyText({}),message:({})", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }

    /**
     * 延迟队列名称 注意 延迟队列名称若和路由键名称相同 否则会被绑定到默认的direct交换器上
     */
    private static final String REGISTER_DELAY_QUEUE = "dec.book.register.delay.queue";

    /**
     * dead letter发送到的 exchange 直连交换机
     */
    public static final String REGISTER_DELAY_EXCHANGE = "dev.book.register.delay.exchange";

    /**
     * routing key 路由键的名字
     */
    public static final String DELAY_ROUTING_KEY = "";


    public static final String REGISTER_QUEUE_NAME = "dev.book.register.queue";

    /**
     * 死信交换器 DLX
     */
    public static final String REGISTER_EXCHANGE_NAME = "dev.book.register.exchange";

    public static final String ROUTING_KEY = "all";

    /**
     * 延迟队列配置
     * 1： params.put("x-message0ttl", 5*100) 直接设置 Queue 延迟时间
     * 2： rabbitTemplate.covertAndSend(book, message -> {
     *     message.getMessageProperties().setExpiration(2* 1000+"");
     *     return message;
     * }); 发送消息时动态设置延迟时间
     *
     * 通过params定义了这个队列中过期了的死信的转发处理
     *
     * 完整的延迟队列流程为
     * 1.定义了 队列 EGISTER_DELAY_QUEUE 的死信将会 携带路由键为 ROUTING_KEY 被转发到 死信交换器 REGISTER_EXCHANGE_NAME 上
     * 2.绑定 直连交换器 REGISTER_DELAY_EXCHANGE 与 队列 EGISTER_DELAY_QUEUE, 绑定键为DELAY_ROUTING_KEY
     * 3.绑定 队列 REGISTER_QUEUE_NAME 到主题交换器(死信) REGISTER_EXCHANGE_NAME 上 绑定键为ROUTING_KEY
     * 4.生产者向 直连交换器 REGISTER_DELAY_EXCHANGE 发送消息 路由键为 DELAY_ROUTING_KEY 并设置过期时间为 5秒
     * 5.消息被 直连交换器 REGISTER_DELAY_EXCHANGE 转发到 队列 EGISTER_DELAY_QUEUE上
     * 6.因为队列 EGISTER_DELAY_QUEUE没有被消费者订阅 所以消息将在5秒后过期
     * 7.过期的消息成为死信 被 1 处理
     * 8.消费者订阅 队列REGISTER_QUEUE_NAME 收到从死信交换器上转发来的消息
     */
    @Bean
    public Queue delayProcessQueue() {
        Map<String, Object> params = new HashMap<>();
        //x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称
        params.put("x-dead-letter-exchange", REGISTER_EXCHANGE_NAME);
        //x-dead-letter-routing-key 声明了这些死信在转发时携带的routing-key 名称
        params.put("x-dead-letter-routing-key", ROUTING_KEY);
        return new Queue(REGISTER_DELAY_QUEUE, true, false, false, params);
    }

    @Bean
    public DirectExchange delayExchange(){
        return new DirectExchange(REGISTER_DELAY_EXCHANGE);
    }

    /**
     * 这个binding 定义了 将delayProcessQueue绑定在direct类型的交换器delayExchange上 并使用bindingKey DELAY_ROUTING_KEY
     * @return
     */
    @Bean
    public Binding dlxBinding(){
        return BindingBuilder.bind(delayProcessQueue()).to(delayExchange()).with(DELAY_ROUTING_KEY);
    }

    @Bean
    public Queue registerBookQueue(){
        return new Queue(REGISTER_QUEUE_NAME, true);
    }

    /**
     * 主题交换器 需要 定义一个绑定 才能使用 否则 消息在被交换器转发时找不到对应的队列就会被 抛弃
     * 转发时 "#"匹配0个或多个词 "*"匹配一个词 routing-key 和binding-key以 "." 隔开
     * @return
     */
    @Bean
    public TopicExchange registerBookTopicExchange(){
        return new TopicExchange(REGISTER_EXCHANGE_NAME);
    }

    /**
     * 这个binding 定义了 将registerBookQueue绑定在Topic类型的交换器registerBookTopicExchange上 并使用bindingKey ROUTING_KEY
     * @return
     */
    @Bean
    public Binding registerBookBinding(){
        return BindingBuilder.bind(registerBookQueue()).to(registerBookTopicExchange()).with(ROUTING_KEY);
    }

    public static final String MAIL_QUEUE_NAME = "email_queue";
    public static final String MAIL_EXCHANGE = "email_exchange";
    public static final String MAIL_ROUTEKEY = "email_routekey";

    @Bean
    Queue mailQueue() {
        String name=MAIL_QUEUE_NAME;
        boolean durable = true; //持久性 如果启用，队列将会在Broker重启前都有效
        boolean exclusive = false; //排他性 队列只能被声明它的消费者使用 断开后自动删除
        boolean autoDelete=false; //自动删除 当所有消费者客户端断开连接后 自动删除队列
        return new Queue(name, durable, exclusive, autoDelete);

    }

    @Bean
    TopicExchange mailExchange() {
        String name = MAIL_EXCHANGE;
        boolean durable = true;
        boolean autoDelete = false;
        return new TopicExchange(name, durable, autoDelete);
    }

    @Bean
    Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTEKEY);
    }
}
