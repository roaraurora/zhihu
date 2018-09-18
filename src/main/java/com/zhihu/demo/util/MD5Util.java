package com.zhihu.demo.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MD5Util {

    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    private static ConstantBean constantBean;

    @Autowired
    public void setConstantBean(ConstantBean constantBean) {
        MD5Util.constantBean = constantBean;
    }

    private static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToDBPass(String input) {
        String str = constantBean.getSalt() + input;
        return md5(str);
    }

    public static void main(String[] args) {
        System.out.println(md5("123456"));
        System.out.println(StringUtils.isEmpty(""));
    }
}
