package com.zhihu.demo.dao;

import com.zhihu.demo.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RoleDao {

    @Select("select role.role_id,role.role_name,role.permission from role,users where role.role_id = users.role_id and users.user_id=#{id}")
    Role selectRoleByUserId(@Param("id") String id);

}
