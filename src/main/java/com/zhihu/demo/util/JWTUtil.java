package com.zhihu.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.catalina.valves.AbstractAccessLogValve;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JWTUtil {

    private static final long EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 使用用户的密码作为私有密钥进行加密
     *
     * @param token    json web token
     * @param username username
     * @param secret   password
     * @return token是否有效
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
//            throw
            return false;
        }
    }

    /**
     * 获得token的payload中的username字段 无需密钥解密(base64编码)
     *
     * @param token json web token
     * @return username
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成json web token
     * 使用password对其进行签名
     * 过期时间设为5min
     * @param username username
     * @param secret password
     * @return token
     */
    public static String sign(String username, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

