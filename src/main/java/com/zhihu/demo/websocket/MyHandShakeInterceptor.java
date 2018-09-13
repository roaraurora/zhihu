package com.zhihu.demo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * HandshakeInterceptor: 管理握手和握手后的事情
 */
@Component
public class MyHandShakeInterceptor implements HandshakeInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ws协议建立之前的http握手的阶段
     * 必须带有authorization字段才能访问WebSocket连接
     * 一旦请求带来auth字段 就会被shiroFilter处理 如果token不合理将返回 token_invalid
     * **实际上stompJs并不支持握手阶段自定义HEADER**
     * 解决方案: 将认证信息从transportation layer转移到connect(frame layer)
     * token合理 登录用户
     *
     * @param serverHttpRequest  请求
     * @param serverHttpResponse 响应
     * @param webSocketHandler   handler
     * @param map                map
     * @return 返回true开始握手 返回false拒绝握手
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) {
        logger.info(this.getClass().getCanonicalName() + "http协议转换WebSocket协议进行前,握手前" + serverHttpRequest.getURI() + " mapSize => " + map.size());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        logger.info(this.getClass().getCanonicalName() + "握手成功后...");
    }
}
