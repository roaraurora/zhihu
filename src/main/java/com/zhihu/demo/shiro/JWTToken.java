package com.zhihu.demo.shiro;

import org.apache.shiro.authc.AuthenticationToken;


/**
 * @author 邓超
 * @description 自定义token
 * @create 2018/9/15
 */
public class JWTToken implements AuthenticationToken {

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
