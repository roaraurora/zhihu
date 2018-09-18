package com.zhihu.demo.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.zhihu.demo.constant")
public class ConstantBean {

    private long expire;

    private long refresh;

    private String salt;

    private String secret;

    private int maxhistory;

    private long userseqstart;

    public long getUserseqstart() {
        return userseqstart;
    }

    public void setUserseqstart(long userseqstart) {
        this.userseqstart = userseqstart;
    }

    public int getMaxhistory() {
        return maxhistory;
    }

    public void setMaxhistory(int maxhistory) {
        this.maxhistory = maxhistory;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getRefresh() {
        return refresh;
    }

    public void setRefresh(long refresh) {
        this.refresh = refresh;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
