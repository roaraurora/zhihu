package com.zhihu.demo.shiro;

import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.User;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.util.JWTUtil;
import jdk.nashorn.internal.objects.Global;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class MyShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 必须重写此方法 否则shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * shiro会在检测用户 role/permission 时调用这个方法
     * 当没有使用缓存的时候 不断刷新页面 每次都会调用此鉴权方法
     * 开启缓存后就只需要执行一次 缓存过期后会再次执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.toString());
        User user = userService.getUserByUsername(username);
        logger.warn("MyShiroRealm.doGetAuthorizationInfo => " + user.getUsername());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //设置用户角色
        simpleAuthorizationInfo.addRole(user.getRole());
        //设置用户权限
        logger.warn(user.getPermission() + user.getRole());
        Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 检验用户名密码是否正确
     * @throws AuthenticationException => ShiroException的子类 会被@GlobalExceptionHandler.handle401 所捕获
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }
        logger.warn("Enter doGetAuthenticationInfo");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new AuthenticationException("Username or password error");
        }
        boolean isCorrect = JWTUtil.verify(token, username, user.getPassword());
        if (!isCorrect) {
            logger.error("MyShiroRealm.doGetAuthenticationInfo => verify " + false);
            throw new AuthenticationException("Username or password error");//不管你token咋错了 都当作未认证/认证失败
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
