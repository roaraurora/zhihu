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

@Configuration
public class RabbitConfig {

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
     * 延迟队列 TTL (time to die) 名称
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
     * @return
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
     * 这个binding
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
     * 将路由键和某模式进行匹配 此时队列需要绑定在一个模式上。
     * 符合"#"匹配一个或多个词 "*"匹配一个词 非字
     * @return
     */
    @Bean
    public TopicExchange registerBookTopicExchange(){
        return new TopicExchange(REGISTER_EXCHANGE_NAME);
    }

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
        boolean durable = true; //持久化
        boolean exclusive = false; //仅创建者可以使用的私有队列 断开后自动删除
        boolean autoDelete=false; //当所有消费者客户端断开连接后 自动删除队列
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
