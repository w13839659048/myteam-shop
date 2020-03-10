package com.qf.controller;


import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${activeServerUrl}")
    private String activeServerUrl;

    @RequestMapping("send/{addr}/{uuid}")
    public ResultBean sendEmail(@PathVariable String addr,@PathVariable String uuid) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper mailMessage=null;

        try {
            mailMessage = new MimeMessageHelper(message, true);
            mailMessage.setSubject("激活您的账号");

            Context context= new Context();
            context.setVariable("username",addr.substring(0,addr.lastIndexOf('@')));
            context.setVariable("url",activeServerUrl+uuid);

            String info = templateEngine.process("emailTemplate", context);

            mailMessage.setText(info,true);

            mailMessage.setFrom("1554909254@qq.com");//系统账号
            mailMessage.setTo(addr);

            sender.send(message);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultBean.success("邮件发送成功");

    }

    // http://localhost:8762/email/active/account/3d4a6bd4-8575-422b-a6f3-cd2de8d91ec6
    @RequestMapping("active/account/{uuid}")
    public ResultBean activeAccount(@PathVariable String uuid) {
        String redisKey = RedisUtil.getRedisKey(RedisConstant.REGIST_EMAIL_URL_KEY_PRE, uuid);

        String addr = (String) redisTemplate.opsForValue().get(redisKey);

//        TODO 去数据库，做一次更新，把这个账号对应的状态，从0改成1

        return ResultBean.success("激活成功");


    }

}
