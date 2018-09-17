package com.zhihu.demo.controller;

import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.User;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.vo.MailVo;
import com.zhihu.demo.vo.RegVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/mail")
public class MailController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/send")
    @RequiresRoles("inactivated")
    public Result<Boolean> reg() throws GlobalException {
        String id = userService.getUserIdFromSecurity();
        User user = userService.getUserById(id);
        MailVo mailVo = new MailVo(user.getUsername(), user.getUserId().toString(),user.getEmail());
        userService.sendActiveMail(mailVo);
        return Result.success(true);
    }

    @GetMapping("/activation")
    public Result<Boolean> activate(@RequestParam("id") String cryptoUserId, HttpServletResponse response) throws GlobalException {
        userService.active(cryptoUserId, response); //失败抛出异常
        //todo 激活成功路由到首页
        return Result.success(true);
    }

}
