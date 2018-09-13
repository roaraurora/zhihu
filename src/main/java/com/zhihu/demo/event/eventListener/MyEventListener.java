package com.zhihu.demo.event.eventListener;

import com.zhihu.demo.event.MyApplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyEventListener {

    private SimpMessagingTemplate simpMessagingTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @EventListener(MyApplicationEvent.class)
    public void notifyUser(MyApplicationEvent event) {
        logger.info("监听到MyApplicationEvent事件");
        simpMessagingTemplate.convertAndSendToUser(event.getUsername(), "/topic/getResponse", "send from listener");
    }

}
