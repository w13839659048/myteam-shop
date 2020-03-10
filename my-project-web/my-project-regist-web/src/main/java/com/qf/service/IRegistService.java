package com.qf.service;

import com.qf.dto.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("regist-service")
public interface IRegistService {

    @RequestMapping("user/regist")
    ResultBean regist(@PathVariable String uname, @PathVariable String password);
}
