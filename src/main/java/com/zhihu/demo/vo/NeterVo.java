package com.zhihu.demo.vo;

/**
 * 用来存储关注关系网当中的用户对象
 */
public class NeterVo {
    private String id;
    private String username;

    public NeterVo(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
