package com.zhihu.demo.controller;

import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.util.ConstantBean;
import com.zhihu.demo.vo.LoginVo;
import com.zhihu.demo.vo.TokenVo;
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

import javax.validation.Valid;
import java.util.Map;

@RestController
public class UserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService;

    private ConstantBean constantBean;

    @Autowired
    public void setConstantBean(ConstantBean constantBean) {
        this.constantBean = constantBean;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     * @param loginVo 登录数据对象
     */
    @PostMapping("/login")
    public Result<TokenVo> login(@RequestBody @Valid LoginVo loginVo) throws GlobalException {
        TokenVo tokenVo = userService.login(loginVo);
        return Result.success(tokenVo);
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
     * 可用于重定向
     */
    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> unauthorized() {
        logger.info("constant :salt => "+constantBean.getSalt()+" refresh => "+constantBean.getRefresh()+" expire => "+constantBean.getExpire());
        return Result.error(CodeMsg.UNAUTHORIZED);
    }

}
