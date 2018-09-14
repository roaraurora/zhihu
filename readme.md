> 基于springboot 的前后端分离架构的在线问答社区
作者信息 @me @chen @sun
## TODO LLST
1. 邮件验证 -done 
2. https -done 需要openssl生成一个IP方式访问的签名证书
3. 服务器端推送 WebSocket 甚至可以做私信功能 WS+SSL = WSS 管理员可以全站广播之类的
    - 异常处理
    - 基于消息发布订阅 暂时看来不可行
    - 通过stomp可以设置header
4. 使用rabbitMQ作为消息代理(broker)
5. docker
6. ngix 动静分离
7. 头像 云
8. redis持久化
>>...

**TOKEN**
1. token有两个时间属性,一个过期时间(短),一个有效刷新时间(长)
2. 后端每次对token校验时 
    - 若token过期 但还在有效期内 则在响应头加入access_token字段
    - 若token不在有效刷新期内 返回403或其他类型
2. 前端通过拦截器 检查收到的请求 若响应头带有 access_token字段 则将其更新local_storage
5. 当改密码或其它需要将所有已登录的客户端重登时 更新redis 白名单 用jti字段唯一标识一个用户对应的token (白名单都用上了 越来越像session)

- 插件
- 私信
