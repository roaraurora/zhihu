package com.zhihu.demo.service;

import com.alibaba.fastjson.JSON;
import com.zhihu.demo.config.RabbitConfig;
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
import com.zhihu.demo.vo.MailVo;
import com.zhihu.demo.vo.RegVo;
import com.zhihu.demo.vo.TokenVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

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

    /**
     * 登出一个用户 并修改redis白名单状态
     */
    public void logout() {
        String id = getUserIdFromSecurity();
        redisService.set(UserKey.getById, id, "invalid_token"); //这里的value没什么讲究 就是不能为null
    }

    /**
     * 注册一个用户 并为其发送激活邮件
     *
     * @param regVo 注册数据对象
     */
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
//            sendActiveMail(user);
            MailVo mailVo = new MailVo(user.getUsername(),user.getUserId().toString(),user.getEmail());
            rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE,RabbitConfig.MAIL_ROUTEKEY, JSON.toJSONString(mailVo));
        } else {
            throw new GlobalException(CodeMsg.EMAIL_ALREDY_USE);
        }

    }

    /**
     * 根据 被加密过后的用户id 激活相应的用户
     *
     * @param cryptoUserId 被加密过后又的用户id
     */
    public void active(String cryptoUserId, HttpServletResponse response) {
        String userId = JasyptUtil.decryptPwd(constantBean.getSalt(), cryptoUserId);
        if (!StringUtils.isNumeric(userId)) {
            //不是数字直接返回 省得查询数据库
            return;
        }
        logger.info("userid => " + userId);
        User user = getUserById(userId); //没有直接报错
        if (roleService.getRoleByUserId(userId).getRoleName().equals("inactivated")) {
            user.setRoleId(1);
            int result = userDao.updateUser(user);
            if (result != 1) {
                throw new GlobalException(CodeMsg.CREATE_USER_FAIL);
            }
        } else {
            //用户存在但状态不为未激活inactivated
            response.setStatus(403);
        }
    }

    /**
     * 发送一封激活邮件
     */
    public void sendActiveMail(MailVo mailVo) {
        Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", mailVo.getUsername());
        String cryptoUserId = JasyptUtil.encryptPwd(constantBean.getSalt(), mailVo.getId());
        mailMap.put("id", cryptoUserId);
        mailService.sendRegMail(mailVo.getMail(), mailMap);
    }

    /**
     * 从当前shiro security manager中获取到用户的token
     * 再根据token获取用户id
     */
    public String getUserIdFromSecurity() {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        return JWTUtil.getId(principalCollection.toString());
    }
}
