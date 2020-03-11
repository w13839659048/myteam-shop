package com.qf.controller;




import com.qf.constant.CookieConstant;
import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("user")
@CrossOrigin(origins = "*", maxAge = 3600,allowCredentials = "true")
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("showLogin")
    public String showLogin(){
        return "login";
    }





    @RequestMapping("checkLogin")
    public String checkLogin(String uname, String password, HttpServletResponse response) {
        System.out.println(uname);
        System.out.println(password);

        String url=String.format("http://user-service/login?username=%s&password=%s",uname,password);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        System.out.println(resultBean.getErrno());
        if (resultBean.getErrno()==0) {
            //登录成功
            //生成cookie
            String uuid = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(CookieConstant.USER_LOGIN,uuid);

            String key = StringUtil.getRedisKey(RedisConstant.USER_LOGIN_PRE, uuid);
            redisTemplate.opsForValue().set(key,resultBean.getData(),30, TimeUnit.DAYS);
            cookie.setMaxAge(30*24*60*60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return "success";



        }

        return "redirect:showLogin";
    }

}
