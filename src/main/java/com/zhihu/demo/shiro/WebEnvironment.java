package com.zhihu.demo.shiro;

import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.mgt.SecurityManager;


/**
 * @author 邓超
 * @description shiro WebEnvironment
 * @create 2018/9/15
 */
public class WebEnvironment extends IniWebEnvironment {
    private static SecurityManager staticSecManager;

    @Override
    protected void configure() {
        super.configure();
        staticSecManager = getSecurityManager();
    }
}

