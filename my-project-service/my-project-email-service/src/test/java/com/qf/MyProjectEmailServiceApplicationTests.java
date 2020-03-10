package com.qf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class MyProjectEmailServiceApplicationTests {

    @Autowired
    private JavaMailSender sender;

    @Test
    void contextLoads() {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("测试邮件主题-nz1909");
        simpleMailMessage.setText("测试邮件内容");
        simpleMailMessage.setFrom("1554909254@qq.com");
        simpleMailMessage.setTo("1603845649@qq.com");
        sender.send(simpleMailMessage);
    }

}
