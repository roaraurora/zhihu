package com.zhihu.demo.dao;


import com.zhihu.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDao {

    @Select("select*from temp_user where username=#{username}")
    User selectUserByUsername(@Param("username") String username);
}
