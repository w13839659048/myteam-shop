package com.qf.mapper;

import com.qf.entity.TUser;

public interface TUserMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByUsername(String username);
}
