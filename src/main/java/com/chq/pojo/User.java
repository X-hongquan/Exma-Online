package com.chq.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String sno;

    @Length(min = 6,max = 15,message = "密码长度6~15位")
    private String password;

    private String salt;


    private String sex;

    @Pattern(regexp = "^1[3~9]\\d{9}$",message = "手机格式不对")
    private String phone;

    private String name;

    private LocalDate bornDate;

    private Integer positionId;

    private Integer classId;

    @Email(message = "邮箱格式不对")
    private String email;

    /**
     * 1,正常,0封禁
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)

    private LocalDateTime createTime;


}
