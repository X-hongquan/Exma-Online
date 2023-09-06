package com.chq.pojo.dto;


import lombok.Data;

import java.util.List;

@Data
public class AnswerDto {

    private Integer examId;

    private List<String> radioList;

    private List<String> otherList;
}
