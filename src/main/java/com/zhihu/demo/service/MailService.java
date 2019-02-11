package com.zhihu.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @author 邓超
 * @description 邮件服务类
 * @create 2018/9/19
 */
@Service
public class MailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Autowired
    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    /**
     * 发送纯文本邮件
     * @param to 收件人地址
     * @param subject 主题
     * @param content 内容 纯文本格式
     */
    public void sendPlainTextMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            sender.send(message);
            logger.info("已发送纯文本邮件");
        } catch (Exception e) {
            logger.error("发送纯文本邮件时发生异常 => ",e);
        }
    }

    /**
     * 发送html格式的邮件
     * @param to 收件人地址
     * @param subject 主题
     * @param content 内容 html格式
     */
    public void sendHtmlMail(String to, String subject, String content){
        MimeMessage message = sender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            sender.send(message);
            logger.info("html邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送html邮件时发生异常！", e);
        }
    }

    void sendRegMail(String to, Map<String, Object> mailMap){
        Context context = new Context();
        context.setVariables(mailMap);
        String emailContent = templateEngine.process("emailTemplate", context);
        String subject = "在线问答社区账号激活";
        sendHtmlMail(to,subject,emailContent);
    }

}
