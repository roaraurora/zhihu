package com.zhihu.demo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class OutBoundChannelInterceptor implements ChannelInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Invoked before the Message is actually sent to the channel.
     * This allows for modification of the Message if necessary.
     * If this method returns {@code null} then the actual
     * send invocation will not occur.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        logger.info("OutBoundChannel preSend");
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        logger.info("OutBoundChannel afterSendCompletion");
    }
}
