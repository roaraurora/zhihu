package com.zhihu.demo.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
public class LoginVo {

    @Email
    @NotNull
    private String email;

    @Length(min = 6,max = 32)
    @NotNull
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

