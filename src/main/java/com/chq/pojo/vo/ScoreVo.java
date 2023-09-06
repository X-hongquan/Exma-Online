package com.chq.pojo.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
@ColumnWidth(15)
public class ScoreVo {

    @ExcelIgnore
    private Integer userId;
    @ExcelProperty("学号")
    private String sno;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("班级")
    private String className;
    @ExcelProperty("科目")
    private String subject;
    @ExcelProperty("总分")
    private Integer score;
}
