package com.zhihu.demo.websocket;

import com.zhihu.demo.event.MyApplicationEvent;
import com.zhihu.demo.shiro.JWTToken;
import com.zhihu.demo.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * 可以在Message对象在发送到MessageChannel前后查看修改此值，也可以在MessageChannel接收MessageChannel对象前后修改此值
 */
@Component
public class InBoundChannelInterceptor implements ChannelInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
// SimpMessagingTemplate对象自动注入失败
    // private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    org.apache.shiro.mgt.SecurityManager securityManager;

    @Override
    public boolean preReceive(MessageChannel channel) {
        logger.info(this.getClass().getCanonicalName() + "preReceive");
        return true;
    }

    /**
     * Invoked before the Message is actually sent to the channel.
     * This allows for modification of the Message if necessary.
     * If this method returns {@code null} then the actual
     * send invocation will not occur.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        logger.info(this.getClass().getCanonicalName() + " 消息发送前");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor != null ? accessor.getCommand() : null;
        if (StompCommand.CONNECT.equals(command)) {
            //绑定securityManager到ThreadContext
            ThreadContext.bind(securityManager);
            SecurityUtils.getSubject();
//            final MultiValueMap<String, String> nativeHeaders = (MultiValueMap<String, String>) accessor.getHeader(StompHeaderAccessor.NATIVE_HEADERS);
//            List<String> auth = nativeHeaders.get("Authorization");
            String authorization = accessor.getFirstNativeHeader("Authorization");
            logger.info("接收到的token为 => " + authorization);
            JWTToken token = new JWTToken(authorization);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token); //token verify失败时抛出 AuthenticationException异常
            String username = JWTUtil.getId(authorization);
            logger.info("[命令: 连接] 用户为 => " + username + " 登录状态： " + subject.isAuthenticated());
            MyPrincipal principal = new MyPrincipal(username);
            accessor.setUser(principal);//这是消息能发送到目标用户的关键
        }
        if (StompCommand.SUBSCRIBE.equals(command)) {
            //处理用户订阅消息的权限验证
        }
        return message; //return null会使得建立了websocket连接但无法发送数据
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message); //两种方法都可以获取到accessor对象
        StompCommand stompCommand = accessor.getCommand();
        logger.info(this.getClass().getCanonicalName() + " 消息发送后 类型为" + stompCommand.getMessageType());
        //CONNECT, CONNECT_ACK, MESSAGE, SUBSCRIBE, UNSUBSCRIBE, HEARTBEAT, DISCONNECT, DISCONNECT_ACK, OTHER;
        if (StompCommand.SUBSCRIBE.equals(stompCommand)) {
            logger.info(this.getClass().getCanonicalName() + "订阅消息发送" + sent);
            Principal principal = accessor.getUser();
            String username = principal.getName();
            applicationContext.publishEvent(new MyApplicationEvent(principal, username));
        }
        if (StompCommand.DISCONNECT.equals(stompCommand)) {
            logger.info(this.getClass().getCanonicalName() + "用户断开连接成功");
        }
    }
}
