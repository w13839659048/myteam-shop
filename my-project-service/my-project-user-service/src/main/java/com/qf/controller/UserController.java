package com.qf.controller;


import com.qf.constant.CookieConstant;
import com.qf.dto.ResultBean;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("login")
    public ResultBean checkLogin(String username, String password) {
        return userService.checkLogin(username,password);
    }

    @RequestMapping("checkLogin")
    public ResultBean checkIsLogin(String uuid){
        return userService.checkIsLogin(uuid);
    }
}
