package com.chq.pojo.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserVo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String password;

    private String salt;

    private String sno;

    /**
     * 1男,0女
     */
    private String sex;

    private String phone;

    private String name;

    private LocalDate bornDate;

    private Integer positionId;

    private String className;

    private String position;

    private Integer classId;

    private String email;
    /**
     * 1,正常,0封禁
     */
    private Integer status;

    private LocalDateTime createTime;
}
