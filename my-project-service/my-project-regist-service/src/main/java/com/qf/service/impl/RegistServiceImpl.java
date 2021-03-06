package com.qf.service.impl;

import com.qf.dto.ResultBean;
import com.qf.entity.TUser;
import com.qf.mapper.TUserMapper;
import com.qf.service.IRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistServiceImpl implements IRegistService {

    @Autowired
    private TUserMapper mapper;

    @Override
    public ResultBean regist(String email, String password) {
        TUser user = new TUser();
        user.setUname(email);
        user.setPassword(password);
        mapper.insert(user);
        return ResultBean.success("注册成功");
    }
}
