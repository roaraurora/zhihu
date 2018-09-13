package com.zhihu.demo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * 启用需要在WebSocketConfig中配置
 */
@Component
public class MyPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public MyPrincipalHandshakeHandler() {
    }

    public MyPrincipalHandshakeHandler(RequestUpgradeStrategy requestUpgradeStrategy) {
        super(requestUpgradeStrategy);
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        logger.error("from determineUser");
        return new MyPrincipal("kiddo");
    }
}
