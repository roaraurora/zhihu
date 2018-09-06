package com.zhihu.demo.controller;

import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final MailService mailService;

    @Autowired
    public TestController(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping("/mail")
    public Result<String> testMailContrller() {
        mailService.sendPlainTextMail("83080779@qq.com","Test mail from Spring Boot Project","o ha yo");
        return Result.success("test mail");
    }
}
