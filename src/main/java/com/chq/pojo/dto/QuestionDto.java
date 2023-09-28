package com.chq.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class QuestionDto {






    //1.easy,2.medium,3.difficult
    private Integer typeId;

    private Integer degree;
    //分值
    private Integer grade;

    private Integer sum;

    private String content;

    @TableField("optionA")
    private String optiona;

    @TableField("optionB")
    private String optionb;

    @TableField("optionC")
    private String optionc;

    @TableField("optionD")
    private String optiond;

    private String result;




}
