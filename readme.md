> 基于springboot 的前后端分离架构的在线问答社区
## TODO LLST
1. 邮件验证 -done 
2. https -done 需要openssl生成一个IP方式访问的签名证书
3. 服务器端推送 WebSocket 甚至可以做私信功能 WS+SSL = WSS 管理员可以全站广播之类的
    - 异常处理
    - 想办法把用户认证集成起来 尝试发起ws连接时带上auth字段 看能不能获取到用户对象
    - 基于消息发布订阅 暂时看来不可行
    - 通过stomp可以设置header
4. 使用rabbitMQ作为消息代理(broker)
5. docker
6. ngix 动静分离
7. 头像 云
>>...

**TOKE**
1. token有两个时间属性,一个过期时间(短),一个有效刷新时间(长)
2. 后端每次对token校验时
2. 前端通过拦截器 检查发出的请求 若token过期 仍在刷新时间之内 则请求服务器刷新接口 获得一个新的token 再继续发生请求。
5. 当改密码或其它需要将所有已登录的客户端重登时 将旧的token加入黑名单 redis