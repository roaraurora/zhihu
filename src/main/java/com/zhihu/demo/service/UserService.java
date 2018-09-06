package com.zhihu.demo.service;

import com.zhihu.demo.dao.UserDao;
import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.User;
import com.zhihu.demo.result.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByUsername(String username) {
        User user = this.userDao.selectUserByUsername(username);
        if (user == null) {
            throw new GlobalException(CodeMsg.USER_NOT_FOUND);
        }
        return user;
    }


}
