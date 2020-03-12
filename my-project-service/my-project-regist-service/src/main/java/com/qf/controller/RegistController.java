package com.qf.controller;


import com.qf.dto.ResultBean;
import com.qf.service.IRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class RegistController {

    @Autowired
    private IRegistService iRegistService;

    @RequestMapping("regist")
    public ResultBean regist(String email, String password){
        return iRegistService.regist(email,password);
    }

}
