package com.zhihu.demo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

public class WebSocketEventListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @EventListener
    public void handleConnectListener(SessionConnectedEvent event) {
        logger.info("[ws-connected] socket connect :{}",event.getMessage());
    }

    @EventListener
    public void handleDisconnectListener(SessionConnectedEvent event) {
        logger.info("[ws-disconnected] socket disconnect :{}",event.getMessage());
    }


}
