package com.qf.service.impl;

import com.alibaba.fastjson.JSON;
import com.qf.constant.RedisConstant;
import com.qf.dto.ResultBean;
import com.qf.entity.TUser;
import com.qf.mapper.TUserMapper;
import com.qf.service.IUserService;
import com.qf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Component
public class UserServiceImpl implements IUserService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResultBean checkLogin(String username, String password) {

        TUser user = userMapper.selectByUsername(username);


        if (user !=null) {
            if(password.equals(user.getPassword())) {
                //这里使用spring security
                return ResultBean.success(user, "登录成功");
            }
        }
        return ResultBean.error("用户名或密码错误") ;
    }

    @Override
    public ResultBean checkIsLogin(String uuid) {
        System.out.println(uuid);
        if (uuid!=null&&!"".equals(uuid)) {
            String redisKey= StringUtil.getRedisKey(RedisConstant.USER_LOGIN_PRE,uuid);
            Object o = redisTemplate.opsForValue().get(redisKey);
            if (o!=null){
                TUser user= JSON.parseObject(JSON.toJSONString(o),TUser.class);
                user.setPassword("");
                System.out.println(user.getUname());
                return ResultBean.success(user,"用户已登录");
            }
        }
        return ResultBean.error("用户未登录");
    }
}
