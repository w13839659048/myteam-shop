package com.qf.controller;




import com.qf.constant.CookieConstant;
import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.util.HttpClientUtils;
import com.qf.util.RedisUtil;
import com.qf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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


    @RequestMapping("register")
    public String register(){
        return "register";
    }



    @RequestMapping("checkLogin")
    public String checkLogin(String uname, String password, HttpServletResponse response,
                             @CookieValue(name = CookieConstant.USER_CART,required = false)String userCartUuid) {
       /* System.out.println(uname);
        System.out.println(password);*/

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

            String url1 = "http://localhost:7768/cart/merge";
            StringBuilder sb = new StringBuilder();
            sb.append(CookieConstant.USER_CART);
            sb.append("=");
            sb.append(userCartUuid);
            sb.append(";");

            sb.append(CookieConstant.USER_LOGIN);
            sb.append("=");
            sb.append(uuid);
            HttpClientUtils.doGet(url1,sb.toString());


            return "redirect:http://localhost:7767";



        }

        return "redirect:showLogin";
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = CookieConstant.USER_LOGIN,required = false)String uuid){


        String url= String.format("http://user-service/checkLogin?uuid=%s",uuid);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        return resultBean;
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = CookieConstant.USER_LOGIN,required = false)String uuid,HttpServletResponse response) {
        if(uuid!=null&&!"".equals(uuid)){
            String redisKey = StringUtil.getRedisKey(RedisConstant.USER_LOGIN_PRE, uuid);
            redisTemplate.delete(redisKey);
        }
        Cookie cookie = new Cookie(CookieConstant.USER_LOGIN,"");
        cookie.setMaxAge(0);//删除cookie
        cookie.setPath("/");
        cookie.setHttpOnly(true);//只有后端程序能访问，提高cookie的安全性
        response.addCookie(cookie);
        return ResultBean.success("注销成功");
    }

    @RequestMapping("registByEmail")
    public String registByEmail(String email,String password) {

        String uuid = UUID.randomUUID().toString();
        // 2 发邮件 通过ribbon来调用
        String url=String.format("http://email-service/email/send?email=%s&uuid=%s",email,uuid);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        ResultBean result=null;
        if (resultBean.getErrno()==0) {
            //3 存到redis中

            redisTemplate.opsForValue().set(RedisUtil.getRedisKey(
                    RedisConstant.REGIST_EMAIL_URL_KEY_PRE,
                    uuid),email,15, TimeUnit.MINUTES);
            //4.去数据库中创建该用户
            result = restTemplate.getForObject(String.format("http://regist-service/user/regist?email=%s&password=%s", email, password), ResultBean.class);

            return "redirect:showLogin";

        }
        return "register";

    }
}
