package com.zhihu.demo.websocket;

import com.zhihu.demo.event.CheckMessageEvent;
import com.zhihu.demo.event.MyApplicationEvent;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.SessionKey;
import com.zhihu.demo.shiro.JWTToken;
import com.zhihu.demo.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.WebSocketSession;
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

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

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
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor != null ? accessor.getCommand() : null;
        String sessionId = accessor != null ? accessor.getSessionId() : null;
        if (command != null) {
            logger.info(this.getClass().getCanonicalName() + " 消息发送前");
            if (StompCommand.CONNECT.equals(command)) {
                //绑定securityManager到ThreadContext
                ThreadContext.bind(securityManager);
                String authorization = accessor.getFirstNativeHeader("Authorization");
                logger.info("接收到的token为 => " + authorization);
                JWTToken token = new JWTToken(authorization);
                Subject subject = SecurityUtils.getSubject();
                subject.login(token); //token verify失败时抛出 AuthenticationException异常
                String userId = JWTUtil.getId(authorization);
                logger.info("[命令: 连接] 用户为 => " + userId + " 登录状态： " + subject.isAuthenticated());
                MyPrincipal principal = new MyPrincipal(userId);
                accessor.setUser(principal);//这是消息能发送到目标用户的关键
                redisService.set(SessionKey.getById, userId, sessionId);//将用户的连接信息存入缓存
//                applicationContext.publishEvent(new CheckMessageEvent("", userId,sessionId)); //发布检查message的事件 此时发布事件似乎客户端并没有真正连接上
            }
            if (StompCommand.SUBSCRIBE.equals(command)) {
                //处理用户订阅消息的权限验证
            }
        }
        return message; //return null会使得建立了websocket连接但无法发送数据
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message); //两种方法都可以获取到accessor对象
        StompCommand stompCommand = accessor.getCommand();
        if (stompCommand != null) {
            logger.info(this.getClass().getCanonicalName() + " 消息发送后 类型为" + stompCommand.getMessageType());
            //CONNECT, CONNECT_ACK, MESSAGE, SUBSCRIBE, UNSUBSCRIBE, HEARTBEAT, DISCONNECT, DISCONNECT_ACK, OTHER;
            Principal principal = accessor.getUser();
            String userId = principal != null ? principal.getName() : null;
            if (StompCommand.SUBSCRIBE.equals(stompCommand)) {
                logger.info(this.getClass().getCanonicalName() + "订阅消息发送" + sent);
                applicationContext.publishEvent(new MyApplicationEvent(principal, userId));
                String sessionId = accessor.getSessionId();
                applicationContext.publishEvent(new CheckMessageEvent("", userId,sessionId)); //发布检查message的事件
            }
            if (StompCommand.DISCONNECT.equals(stompCommand)) {
                logger.info(this.getClass().getCanonicalName() + "用户断开连接成功");
                redisService.delete(SessionKey.getById,userId);//用户断开连接时删除用户信息
            }
        }
    }
}
