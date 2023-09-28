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
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 作者id
     */
    private Integer authorId;


    private String name;

    private Integer degree;

    private Integer courseId;


    private Integer sum;

    /**
     * 作者名字
     */
    private String authorName;

    /**
     * 文件路径
     */
    private String url;

    @TableField(fill = FieldFill.INSERT)

    private LocalDateTime createTime;


}
