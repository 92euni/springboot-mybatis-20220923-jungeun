package com.boot.mybatis20220923jungeun.domain;

import com.boot.mybatis20220923jungeun.dto.SignupRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private int user_code;
    private String user_id;
    private String user_password;
    private String user_name;
    private String user_email;

    public SignupRespDto toDto(){
        return SignupRespDto.builder()
                .userCode(user_code)
                .userID(user_id)
                .userPassword(user_password)
                .userName(user_name)
                .userEmail(user_email)
                .build();
    }

}
