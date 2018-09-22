package com.zhihu.demo.controller;

import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.util.ConstantBean;
import com.zhihu.demo.vo.LoginVo;
import com.zhihu.demo.vo.RegVo;
import com.zhihu.demo.vo.TokenVo;
import com.zhihu.demo.vo.validator.IsIllegal;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

@RestController
@RequestMapping("/user")
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
     *
     * @param loginVo 登录数据对象
     */
    @ApiOperation(value = "登录用户",notes = "用户通过邮箱和密码登录",httpMethod = "POST")
    @PostMapping("/login")
    public Result<TokenVo> login(@RequestBody @Valid LoginVo loginVo) throws GlobalException {
        TokenVo tokenVo = userService.login(loginVo);
        return Result.success(tokenVo);
    }

    @ApiOperation(value = "登出接口",notes = "登录用户可从此接口登出从而使TOKEN无效",httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/logout")
    @RequiresAuthentication
    public Result<Boolean> logout() throws GlobalException {
        userService.logout();
        return Result.success(true);
    }

    @ApiOperation(value = "注册用户",notes = "注册用户接口",httpMethod = "POST")
    @PostMapping("/reg")
    @IsIllegal
    public Result<Boolean> reg(@RequestBody @Valid RegVo regVo) throws GlobalException {
        userService.reg(regVo); //失败抛出异常
        return Result.success(true);
    }

    @ApiOperation(value = "测试角色",notes = "",httpMethod = "GET")
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
    @ApiOperation(value = "认证用户",notes = "只有已登录用户才能访问",httpMethod = "GET")
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public Result<String> requireAuth() {
        return Result.success("you are authenticated");
    }

    /**
     * 需要当前用户的角色为admin
     */
    @ApiOperation(value = "管理员",notes = "访问此APi须为管理员角色",httpMethod = "GET")
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public Result<String> requireRole() {
        return Result.success("you are visiting require_role");
    }

    /**
     * 需要当前用户同时有 view 和 edit权限
     */
    @ApiOperation(value = "编辑权限",notes = "访问此APi须有edit权限",httpMethod = "GET",code = 200)
    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Result<String> requirePermission() {
        return Result.success("you are visiting permission require edit,view");
    }

    /**
     * 可用于重定向
     */
    @ApiOperation(value = "未认证",notes = "当访问此API 时 会返回一个未认证的信息",httpMethod = "GET",code = 401)
    @GetMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> unauthorized() {
        logger.info("constant :salt => " + constantBean.getSalt() + " refresh => " + constantBean.getRefresh() + " expire => " + constantBean.getExpire());
        return Result.error(CodeMsg.UNAUTHORIZED);
    }

}
