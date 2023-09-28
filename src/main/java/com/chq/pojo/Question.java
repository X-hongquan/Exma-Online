package com.chq.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    @ExcelIgnore
    private Integer testId;

    @ExcelProperty("科目ID,计算机组成原理填2")
    private Integer courseId;

    //1.easy,2.medium,3.difficult
    @ExcelProperty("题目难度1-10")
    private Integer degree;

    //分值
    @ExcelProperty("选择题3,判断选2,填空选2,综合选10")
    private Integer grade;

    @ExcelProperty("内容")
    @ColumnWidth(130)
    private String content;
    @ExcelIgnore
    private Integer sort;

    @TableField("optionA")
    @ExcelProperty("选项A")
    @ColumnWidth(30)
    private String optiona;

    @TableField("optionB")
    @ExcelProperty("选项B")
    @ColumnWidth(30)
    private String optionb;

    @TableField("optionC")
    @ExcelProperty("选项C")
    @ColumnWidth(30)
    private String optionc;

    @TableField("optionD")
    @ExcelProperty("选项D")
    @ColumnWidth(30)
    private String optiond;

    @ExcelProperty("选择题和判断题需要填,其他题不需要")
    private String result;

    @ExcelProperty("选择1,判断2,填空3,综合4")
    private Integer typeId;

    @ExcelIgnore
    private Integer userId;



    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private LocalDateTime createTime;


}
