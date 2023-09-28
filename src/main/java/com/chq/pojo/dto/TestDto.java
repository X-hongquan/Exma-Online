package com.chq.pojo.dto;

import lombok.Data;

import java.util.List;


@Data
public class TestDto {


    private Integer courseId;
    private Integer degree;
    private String name;
    private Integer sum;
    List<QuestionDto> newTest;

}
