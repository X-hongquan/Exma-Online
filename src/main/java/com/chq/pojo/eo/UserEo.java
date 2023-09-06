package com.chq.pojo.eo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import lombok.Data;

import java.time.LocalDate;


@Data
@ContentFontStyle(color = 98)
@ColumnWidth(15)
public class UserEo {
    @ExcelProperty("学号")
    private String sno;
    @ExcelProperty("手机")
    private String phone;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("性别")
    private String sex;
    @ExcelProperty("出生日期")
    private LocalDate bornDate;
    private String email;
    @ExcelProperty("身份ID,学生填2,老师填3")
    private Integer positionId;
    @ExcelProperty("班级ID,查看班级表对应")
    private Integer classId;
}
