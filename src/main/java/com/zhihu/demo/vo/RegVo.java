package com.zhihu.demo.vo;

import com.zhihu.demo.vo.validator.IsIllegal;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class RegVo {

    @NotNull
    @Length(max = 32)
    @IsIllegal
    private String username;

    @Email
    @NotNull
    private String email;

    @Length(min = 6,max = 32)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
        return "RegVo{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
