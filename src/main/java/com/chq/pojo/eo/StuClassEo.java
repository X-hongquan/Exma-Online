package com.chq.pojo.eo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
@ColumnWidth(15)
public class StuClassEo {

    @ExcelProperty("班级ID")
    private Integer id;

    @ExcelProperty("班级")
    private String className;
}
