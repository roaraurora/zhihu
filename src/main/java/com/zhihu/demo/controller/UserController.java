package com.zhihu.demo.controller;

import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.User;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<String> login(@RequestParam("username") String username,
                                @RequestParam("password") String password) throws GlobalException{
        User user = userService.getUserByUsername(username);
        logger.error("UserController.login => "+user.getPassword());
        if (user.getPassword().equals(password)) {
            return Result.success(JWTUtil.sign(username, password));
        } else {
            throw new GlobalException(CodeMsg.UNAUTHORIZED);
        }
    }

    @GetMapping("/article")
    public Result<String> article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return Result.success("you are a logged user!");
        } else {
            return Result.success("you are a guest!");
        }
    }

    /**
     * 需要当前用户已登录(Authentication)
     */
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public Result<String> requireAuth() {
        return Result.success("you are authenticated");
    }

    /**
     * 需要当前用户的角色为admin
     */
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public Result<String> requireRole() {
        return Result.success("you are visiting require_role");
    }

    /**
     * 需要当前用户同时有 view 和 edit权限
     */
    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Result<String> requirePermission() {
        return Result.success("you are visiting permission require edit,view");
    }

    /**
     * 需要一个401地址用于跳转
     * Filter中token验证失败时不能直接返回401信息 而是跳转到这个路由
     */
    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> unauthorized() {
        return Result.error(CodeMsg.UNAUTHORIZED);
    }

}
