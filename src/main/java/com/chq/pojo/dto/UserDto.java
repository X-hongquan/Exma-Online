package com.chq.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserDto implements Serializable {
    private Integer id;
    private String phone;
    private String name;
    private String sno;
    private Integer classId;
    private String sex;
    private LocalDate bornDate;
    private String email;
    private Integer positionId;
}
