package com.zhihu.demo.config;

import com.zhihu.demo.websocket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * @author 邓超
 * @description WebSocket配置类
 * @create 2018/9/16
 */
@Configuration
@EnableWebSocketMessageBroker //使用stomp协议来传输基于消息代理的消息
@Order(1)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private InBoundChannelInterceptor inBoundChannelInterceptor;

    private MyHandShakeInterceptor myHandShakeInterceptor;

    private OutBoundChannelInterceptor outBoundChannelInterceptor;

    private MyPrincipalHandshakeHandler myPrincipalHandshakeHandler;

    private MyWebSocketHandlerDecoratorFactory myWebSocketHandlerDecoratorFactory;

    @Autowired
    public void setMyWebSocketHandlerDecoratorFactory(MyWebSocketHandlerDecoratorFactory myWebSocketHandlerDecoratorFactory) {
        this.myWebSocketHandlerDecoratorFactory = myWebSocketHandlerDecoratorFactory;
    }

    @Autowired
    public void setMyPrincipalHandshakeHandler(MyPrincipalHandshakeHandler myPrincipalHandshakeHandler) {
        this.myPrincipalHandshakeHandler = myPrincipalHandshakeHandler;
    }

    @Autowired
    public void setOutBoundChannelInterceptor(OutBoundChannelInterceptor outBoundChannelInterceptor) {
        this.outBoundChannelInterceptor = outBoundChannelInterceptor;
    }

    @Autowired
    public void setMyHandShakeInterceptor(MyHandShakeInterceptor myHandShakeInterceptor) {
        this.myHandShakeInterceptor = myHandShakeInterceptor;
    }

    @Autowired
    public void setInBoundChannelInterceptor(InBoundChannelInterceptor inBoundChannelInterceptor) {
        this.inBoundChannelInterceptor = inBoundChannelInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // addEndpoint: 添加stomp协议的端点
        // withSockJS(): 指定断点使用SockJS协议 支持服务器端扩展和负载均衡 对不支持WS的游览器提供回退(Http Stream/Long Polling)
        // 开启后url变为ws://localhost:8090/endpoint/<serverid>/<sessionid>/websocket
        // setAllowedOrigins("*"): 允许跨域访问
        registry.addEndpoint("/endpoint")
                .setAllowedOrigins("*")
                .addInterceptors(myHandShakeInterceptor)
                .setHandshakeHandler(myPrincipalHandshakeHandler)
                .withSockJS();
    }

    /**
     * 简单消息代理
     */
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        // 设置接收客户端消息的路径前缀 即客户端与服务器建立好WebSocket连接后 客户端访问服务器上的MessageMapping从而给服务器发送数据时 需要带上前缀
//        registry.setApplicationDestinationPrefixes("/app");
//        //设置接收客户端订阅的路径前缀 即客户端与服务器建立好WebSocket连接后 客户端想订阅服务器上的消息时  需要在MessageMapping的@SendTo注解的提供订阅的路径上 加上前缀
//        //topic通常代表广播
//        registry.enableSimpleBroker("/topic", "/queue");
//    }

    /**
     * 配置RabbitMQ作为消息代理
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/exchange", "/topic", "/queue", "/amp/queue")
                .setAutoStartup(true)
                .setRelayHost("120.79.223.90")
                .setRelayPort(61613)
                .setClientLogin("root")
                .setClientPasscode("546449")
                .setSystemLogin("root")
                .setSystemPasscode("546449")
                .setVirtualHost("/")
                .setSystemHeartbeatSendInterval(5000)
                .setSystemHeartbeatReceiveInterval(4000);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //添加拦截器 处理客户端发来的请求
        registration.interceptors(inBoundChannelInterceptor);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(outBoundChannelInterceptor);
    }

//    @Override
//    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
//        registry.addDecoratorFactory(myWebSocketHandlerDecoratorFactory);
//    }



}
