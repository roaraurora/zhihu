package com.zhihu.demo.service;

import com.sun.corba.se.spi.ior.IdentifiableFactory;
import com.zhihu.demo.dao.UserDao;
import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.User;
import com.zhihu.demo.redis.RedisService;
import com.zhihu.demo.redis.UserKey;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.util.ConstantBean;
import com.zhihu.demo.util.JWTUtil;
import com.zhihu.demo.util.JasyptUtil;
import com.zhihu.demo.util.MD5Util;
import com.zhihu.demo.vo.LoginVo;
import com.zhihu.demo.vo.RegVo;
import com.zhihu.demo.vo.TokenVo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserDao userDao;

    private ConstantBean constantBean;

    private RedisService redisService;

    private MailService mailService;

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setConstantBean(ConstantBean constantBean) {
        this.constantBean = constantBean;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByEmail(String email) {
        User user = this.userDao.selectUserByEmail(email);
        if (user == null) {
            throw new GlobalException(CodeMsg.USER_NOT_FOUND);
        }
        return user;
    }

    public User getUserById(String id) {
        User user = this.userDao.selectUserById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.USER_NOT_FOUND);
        }
        return user;
    }

    public TokenVo login(LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        logger.info(loginVo.toString());
        String email = loginVo.getEmail();
        User user = getUserByEmail(email);
        String password = user.getPassword();
        String rawPassword = loginVo.getPassword();
        if (!password.equals(MD5Util.inputPassToDBPass(rawPassword))) {
            throw new GlobalException(CodeMsg.PASSWORD_ERR);
        }
        String token = JWTUtil.sign(user.getUserId().toString(), constantBean.getSecret());
        return new TokenVo(token);
    }

    public void logout(Subject subject) {
        PrincipalCollection principalCollection = subject.getPrincipals();
        String id = JWTUtil.getId(principalCollection.toString()); //如果连token都可以伪造了 id算什么
        logger.info("logout user with id => " + id + " jti => " + JWTUtil.getJti(principalCollection.toString()));
        redisService.set(UserKey.getById, id, "invalid_token"); //这里的value没什么讲究 就是不能为null
    }

    public void reg(RegVo regVo) {
        if (regVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        logger.info(regVo.toString());
        User user = userDao.selectUserByEmail(regVo.getEmail());
        if (user == null) {
            user = new User();
            String mail = regVo.getEmail();
            String username = regVo.getUsername();
            user.setEmail(mail);
            user.setPassword(MD5Util.inputPassToDBPass(regVo.getPassword()));
            user.setUsername(username);
            user.setRoleId(3); //TODO 硬编码 有待改进 role_id 为1是 普通用户 user 3.未激活用户 inactivated
            int result = userDao.insertUser(user);
            if (result != 1) {
                throw new GlobalException(CodeMsg.CREATE_USER_FAIL);
            }
            Map<String,Object> mailMap = new HashMap<>();
            mailMap.put("username",username);
            String cryptoUserId = JasyptUtil.encryptPwd(constantBean.getSalt(),user.getUserId().toString());
            mailMap.put("id",cryptoUserId);
            mailService.sendRegMail(mail,mailMap);
        } else {
            throw new GlobalException(CodeMsg.EMAIL_ALREDY_USE);
        }

    }

    public void active(String cryptoUserId, HttpServletResponse response) {
        String userId = JasyptUtil.decryptPwd(constantBean.getSalt(), cryptoUserId);
        User user = getUserById(userId); //没有直接报错
        if (roleService.getRoleByUserId(userId).getRoleName().equals("inactivated")) {
            user.setRoleId(1);
            int result = userDao.updateUser(user);
            if (result != 1) {
                throw new GlobalException(CodeMsg.CREATE_USER_FAIL);
            }
        }else {
            //用户存在但状态不为未激活inactivated
            response.setStatus(403);
        }
    }
}
