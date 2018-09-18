package com.zhihu.demo.websocket;

import com.zhihu.demo.event.CheckMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class OutBoundChannelInterceptor implements ChannelInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    /**
     * Invoked before the Message is actually sent to the channel.
     * This allows for modification of the Message if necessary.
     * If this method returns {@code null} then the actual
     * send invocation will not occur.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        logger.info("OutBoundChannel preSend");
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//        logger.info("OutBoundChannel afterSendCompletion");

    }
}
