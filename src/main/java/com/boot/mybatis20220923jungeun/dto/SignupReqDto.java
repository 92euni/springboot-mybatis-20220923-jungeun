package com.boot.mybatis20220923jungeun.dto;

import com.boot.mybatis20220923jungeun.domain.User;
import lombok.Data;

@Data
public class SignupReqDto {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;

    public User toEntity(){
        return User.builder()
                .user_id(userId)
                .user_name(userName)
                .user_email(userEmail)
                .build();
    }
}
