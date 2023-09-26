package com.chq.controller;


import com.chq.common.R;
import com.chq.pojo.Question;
import com.chq.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-08
 */
@RestController
@RequestMapping("/question")
public class QuestionController {


    @Autowired
    private IQuestionService questionService;



    @GetMapping("/one")
    public R<List<Question>> getByTestId(Integer testId) {
        return questionService.getByTestId(testId);
    }

    @GetMapping("/result")
    public R<String> getResult(Integer testId) {
        return questionService.getResult(testId);
    }

}

