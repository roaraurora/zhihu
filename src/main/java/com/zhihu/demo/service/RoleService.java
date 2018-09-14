package com.zhihu.demo.service;

import com.zhihu.demo.dao.RoleDao;
import com.zhihu.demo.exception.GlobalException;
import com.zhihu.demo.model.Role;
import com.zhihu.demo.result.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleDao roleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role getRoleByUserId(String id) {
        Role role = roleDao.selectRoleByUserId(id);
        if (role == null) {
            //用户不存在时
            throw new GlobalException(CodeMsg.USER_NOT_FOUND);
        }
        return role;
    }
}
