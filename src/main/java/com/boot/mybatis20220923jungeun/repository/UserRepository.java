package com.boot.mybatis20220923jungeun.repository;

import com.boot.mybatis20220923jungeun.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    public int save(User user);
}
