package com.qf.service;

import com.qf.dto.ResultBean;

public interface IUserService {
    ResultBean checkLogin(String username, String password);

    ResultBean checkIsLogin(String uuid);
}
