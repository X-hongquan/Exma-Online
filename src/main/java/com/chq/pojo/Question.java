package com.chq.pojo;

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
    private Integer id;

    private String testId;

    private String content;

    private Integer sort;

    @TableField("optionA")
    private String optiona;

    @TableField("optionB")
    private String optionb;

    @TableField("optionC")
    private String optionc;

    @TableField("optionD")
    private String optiond;

    private String result;

    private Integer typeId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
