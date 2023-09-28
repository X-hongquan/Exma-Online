package com.chq.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamVo {

    private Integer id;

    private String title;

    private String testId;


    private LocalDateTime beginTime;


    private LocalDateTime endTime;

    private Integer classId;

    private Integer userId;

    private String courseName;

    private String className;

    private String name;

    private Integer score;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
