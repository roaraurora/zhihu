package com.zhihu.demo.websocket;

import org.apache.commons.lang3.StringUtils;

import java.security.Principal;


/**
 * @author 邓超
 * @description 自定义WebSocket对象类
 * @create 2018/9/19
 */
public class MyPrincipal implements Principal {
    private String name;

    MyPrincipal(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (StringUtils.isEmpty(this.name)) {
            return false;
        }
        return super.equals(obj);
    }
}
