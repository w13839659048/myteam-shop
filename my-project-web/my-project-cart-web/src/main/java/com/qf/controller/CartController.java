package com.qf.controller;


import com.qf.constant.CookieConstant;
import com.qf.dto.ResultBean;
import com.qf.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean addProduct(@CookieValue(name = CookieConstant.USER_CART,required = false)String uuid,
                                 @PathVariable Long productId,
                                 @PathVariable int count,
                                 HttpServletResponse response,
                                 HttpServletRequest request) {

        Object o = request.getAttribute("user");
        if (o!=null){
            TUser user = (TUser) o;
            System.out.println(user);
            Long userId = user.getId();
            String url=String.format("http://cart-service/addPro?id=%s&productId=%s&count=%s",userId,productId,count);
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;

        }
        if (uuid==null||"".equals(uuid)){
           uuid= UUID.randomUUID().toString();
           Cookie cookie=new Cookie(CookieConstant.USER_CART,uuid);
           cookie.setPath("/");
           response.addCookie(cookie);

        }

        String url=String.format("http://cart-service/addPro?id=%s&productId=%s&count=%s",uuid,productId,count);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        return resultBean;

    }
    @RequestMapping("clean")
    @ResponseBody
    public ResultBean cleanCart(@CookieValue(value = CookieConstant.USER_CART,required = false)String uuid,
                                HttpServletRequest request,
                                HttpServletResponse response){
        Object o = request.getAttribute("user");
        if (o!=null) {
            TUser user = (TUser) o;
            String url=String.format("http://cart-service/cleanPro?uuid=%s",user.getId().toString());
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;

        }
        if(uuid!=null&&!"".equals(uuid)){
            Cookie cookie = new Cookie(CookieConstant.USER_CART,"");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            String url=String.format("http://cart-service/cleanPro?uuid=%s",uuid);
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;

        }
        return ResultBean.error("当前用户没有购物车");

    }

    @RequestMapping("show")
    @ResponseBody
    public ResultBean showCart(@CookieValue(name=CookieConstant.USER_CART,required = false)String uuid,HttpServletRequest request) {
        Object o = request.getAttribute("user");
        if(o!=null){
            TUser user = (TUser) o;
            Long userId = user.getId();
            String url=String.format("http://cart-service/showPro?id=%s",userId.toString());
            ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
            return resultBean;
        }
        String url=String.format("http://cart-service/showPro?id=%s",uuid);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        return resultBean;
    }

    @RequestMapping("merge")
    @ResponseBody
    public ResultBean merge(@CookieValue(name = CookieConstant.USER_CART,required = false)String uuid,
                            HttpServletResponse response,
                            HttpServletRequest request) {
        TUser user = (TUser) request.getAttribute("user");
        String userId = null;
        if(user!=null){
            userId = user.getId().toString();
        }
        Cookie cookie = new Cookie(CookieConstant.USER_CART,"");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        String url=String.format("http://cart-service/merge?uuid=%s&userId=%s",uuid,userId);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class);
        return resultBean;



    }
}
