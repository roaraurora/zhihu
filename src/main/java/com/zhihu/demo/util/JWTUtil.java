package com.zhihu.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.UserKey;
import com.zhihu.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JWTUtil {

    private static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    private static UserService userService;

    private static ConstantBean constantBean;

    private static RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        JWTUtil.redisService = redisService;
    }

    @Autowired
    public void setConstantBean(ConstantBean constantBean) {
        //使用非静态setter方法为静态成员变量提供依赖注入
        JWTUtil.constantBean = constantBean;
    }

    @Autowired
    public void setUserService(UserService userService) {
        JWTUtil.userService = userService;
    }

    /**
     * 使用用户的密码作为私有密钥进行加密
     *
     * @param token  json web token
     * @param id     id
     * @param secret password
     * @return token是否有效
     */
    public static boolean verify(String token, String id, String secret) {
        try {
            if (!canRefresh(token)) {
                return false;
            }
            //检查redis白名单 用户名对应的 key有没有对应的value
            //若redis没有命中 get方法返回null值
            String jti = redisService.get(UserKey.getById, id, String.class);
            if (!jti.equals(getJti(token))) {
                // "" or null
                //已登出 或者 redis中找不到对应的key
                return false;
            }
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("id", id)
                    .build();
            verifier.verify(token);
            return true;
        } catch (TokenExpiredException exception) {
            logger.info("Token has expired, Generate new Token for :" + id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token的payload中的id字段 无需密钥解密(base64编码)
     *
     * @param token json web token
     * @return id
     */
    public static String getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getJti(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getId();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 当token过期且在有效刷新时间内时 返回true
     *
     * @param token token
     * @return 是否能刷新token
     */
    private static boolean allowRefresh(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expireDate = jwt.getExpiresAt();
            return (canRefresh(token)) && (expireDate.getTime() <= System.currentTimeMillis());
        } catch (JWTDecodeException e) {
            return false;
        }
    }

    private static boolean canRefresh(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date refreshDate = jwt.getClaim("refreshTime").asDate();
            return refreshDate.getTime() >= System.currentTimeMillis();
        } catch (JWTDecodeException e) {
            return false;
        }
    }

    public static String  refreshToken(String token) {
        if (allowRefresh(token)) {
            String id = getId(token);
//            User user = userService.getUserById(id); 使用用户的密码加密将引起不必要的数据库查询
//            return sign(id, user.getPassword());
            return sign(id, constantBean.getSecret());
        }
        return null;
    }

    /**
     * 生成json web token
     * 使用password对其进行签名
     * 过期时间设为5min
     *
     * @param id     id
     * @param secret password
     * @return token
     */
    public static String sign(String id, String secret) {
        try {
            Date expireDate = new Date(System.currentTimeMillis() + constantBean.getExpire());
            Date refreshDate = new Date(expireDate.getTime() + constantBean.getRefresh());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String uuid = UUIDUtil.uuid();
            boolean ret = redisService.set(UserKey.getById, id, uuid);
            return JWT.create()
                    .withClaim("id", id)
                    .withClaim("refreshTime", refreshDate)
                    .withJWTId(uuid)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

