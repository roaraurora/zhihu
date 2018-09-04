package com.zhihu.demo.dao;

import com.zhihu.demo.model.Jobs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface JobsDao {
    /**
     * dao层 定义mybatis查询数据库使用的接口
     * 注解: Mapper和Component
     */

    @Select("select*from JOBS where job_id=#{job_id}")
    Jobs getJobs(@Param("job_id") String job_id);


}
