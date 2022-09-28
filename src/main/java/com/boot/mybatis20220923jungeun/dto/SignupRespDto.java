package com.boot.mybatis20220923jungeun.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignupRespDto {
    private int userCode;
    private String userID;
    private String userPassword;
    private String userName;
    private String userEmail;


}
