package com.zhihu.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhihu.demo.model.User;
import com.zhihu.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JWTUtil {

    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    private static UserService userService;

    private static ConstantBean constantBean;

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
     * @param email  email
     * @param secret password
     * @return token是否有效
     */
    public static boolean verify(String token, String email, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("email", email)
                    .build();
            verifier.verify(token);
            return true;
        } catch (TokenExpiredException exception) {
            if (canRefresh(token)) {
                logger.info("Token has expired, Generate new Token for :" + email);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token的payload中的email字段 无需密钥解密(base64编码)
     *
     * @param token json web token
     * @return email
     */
    public static String getEmail(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("email").asString();
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
    private static boolean canRefresh(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date refreshDate = jwt.getClaim("refreshTime").asDate();
            Date expireDate = jwt.getExpiresAt();
            return (refreshDate.getTime() >= System.currentTimeMillis()) && (expireDate.getTime() <= System.currentTimeMillis());
        } catch (JWTDecodeException e) {
            return false;
        }
    }

    public static String refreshToken(String token) {
        if (canRefresh(token)) {
            String email = getEmail(token);
            User user = userService.getUserByEmail(email);
            return sign(email, user.getPassword());
        }
        return null;
    }

    /**
     * 生成json web token
     * 使用password对其进行签名
     * 过期时间设为5min
     *
     * @param email  email
     * @param secret password
     * @return token
     */
    public static String sign(String email, String secret) {
        try {
            Date expireDate = new Date(System.currentTimeMillis() + constantBean.getExpire());
            Date refreshDate = new Date(expireDate.getTime() + constantBean.getRefresh());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("email", email)
                    .withClaim("refreshTime", refreshDate)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

