package com.zhihu.demo.controller;


import com.zhihu.demo.model.Jobs;
import com.zhihu.demo.service.JobsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    private static Logger log = LoggerFactory.getLogger(HelloController.class);

    private final JobsService jobsService;

    @Autowired
    public HelloController(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        Jobs jobs = jobsService.getJobs("AD_VP");
        return jobs.getJobTitle();
    }

    @PostMapping("example")
    public Object example() {
        return "alright";
    }

}
