package com.chq.pojo.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginDto {

    @Pattern(regexp = "^1[3~9]\\d{9}$",message = "手机格式不对")
    private String phone;

    @Length(min = 6,max = 15,message = "密码长度6~15位")
    private String password;
}
