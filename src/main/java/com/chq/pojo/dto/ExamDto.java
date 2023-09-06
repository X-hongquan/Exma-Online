package com.chq.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamDto {
    private String title;
    private Integer classId;
    private String courseName;
    private String testId;
    private List<LocalDateTime> timeArray;
}
