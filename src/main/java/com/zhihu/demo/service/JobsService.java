package com.zhihu.demo.service;

import com.zhihu.demo.dao.JobsDao;
import com.zhihu.demo.model.Jobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobsService {
    /**
     *  service层实现主要的业务逻辑
     *  注解: @Service => spring 依赖注入
     */

    private JobsDao jobsDao;

    @Autowired
    public JobsService(JobsDao jobsDao) {
        this.jobsDao = jobsDao;
    }

    public Jobs getJobs(String code) {
        return jobsDao.getJobs(code);
    }
}
