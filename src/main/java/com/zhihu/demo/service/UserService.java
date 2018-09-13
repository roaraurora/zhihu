package com.zhihu.demo.service;

import com.zhihu.demo.dao.UserDao;
import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.User;
import com.zhihu.demo.result.CodeMsg;
import com.zhihu.demo.util.JWTUtil;
import com.zhihu.demo.util.MD5Util;
import com.zhihu.demo.vo.LoginVo;
import com.zhihu.demo.vo.TokenVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserDao userDao;

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

    public TokenVo login(LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        logger.info(loginVo.toString());
        String email = loginVo.getEmail();
        User user = getUserByEmail(email);
        String password = user.getPassword();
        String rawPassword = loginVo.getPassword();
        logger.info(MD5Util.inputPassToDBPass(rawPassword));
        if (!password.equals(MD5Util.inputPassToDBPass(rawPassword))) {
            throw new GlobalException(CodeMsg.PASSWORD_ERR);
        }
        String token = JWTUtil.sign(email, password);
        return new TokenVo(token);
    }
}
