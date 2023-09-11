package com.chq.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordDto {

    @Length(min = 6,max = 15,message = "密码长度6~15位")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @NotNull(message = "验证码不能为空")
    private String code;
}
