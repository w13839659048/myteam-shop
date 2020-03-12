package com.qf.controller;


import com.google.gson.Gson;
import com.qf.constant.CookieConstant;
import com.qf.dto.ResultBean;
import com.qf.util.HttpClientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping({"","index"})
    public String  showIndex() {
        return "index";
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = CookieConstant.USER_LOGIN,required = false)String uuid) {

        String url = "http://localhost:7767/user/checkIsLogin";

        String cookie = new StringBuilder().append(CookieConstant.USER_LOGIN).append("=").append(uuid).toString();

        String result = HttpClientUtils.doGet(url, cookie);

        Gson gson = new Gson();

        ResultBean resultBean = gson.fromJson(result, ResultBean.class);
        return resultBean;

    }



}
