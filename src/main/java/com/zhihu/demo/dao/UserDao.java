package com.zhihu.demo.dao;

import com.zhihu.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDao {

    @Select("select*from users where email=#{email}")
    User selectUserByEmail(@Param("email") String email);

}
