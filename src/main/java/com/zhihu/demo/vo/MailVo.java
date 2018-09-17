package com.zhihu.demo.vo;

public class MailVo {
    private String username;
    private String id;
    private String mail;


    public MailVo(String username, String id, String mail) {
        this.username = username;
        this.id = id;
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MailVo{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
