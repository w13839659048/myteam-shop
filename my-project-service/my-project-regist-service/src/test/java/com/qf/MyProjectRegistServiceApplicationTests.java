package com.qf;

import com.qf.dto.ResultBean;
import com.qf.entity.TUser;
import com.qf.mapper.TUserMapper;
import com.qf.service.IRegistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyProjectRegistServiceApplicationTests {



    @Autowired
    private IRegistService iRegistService;

    @Test
    void contextLoads() {

        ResultBean regist = iRegistService.regist("123456@qq.com", "123456");

    }

}
