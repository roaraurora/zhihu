package com.zhihu.demo.dao;

import com.zhihu.demo.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDao {

    //根据email查询用户
    @Select("select*from users where email=#{email}")
    User selectUserByEmail(@Param("email") String email);

    //根据id查询用户
    @Select("select*from users where user_id=#{id}")
    User selectUserById(@Param("id") String id);

    //新建用户
    int insertUser(User user);

    int updateUser(User user);
}
