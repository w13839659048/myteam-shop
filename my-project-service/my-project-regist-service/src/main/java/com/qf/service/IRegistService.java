package com.qf.service;

import com.qf.dto.ResultBean;

public interface IRegistService {
    ResultBean regist(String email, String password);
}
