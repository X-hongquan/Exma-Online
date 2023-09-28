package com.chq.controller;


import com.alibaba.excel.EasyExcel;
import com.chq.common.R;
import com.chq.pojo.Choice;
import com.chq.pojo.Question;
import com.chq.pojo.eo.UserEo;
import com.chq.service.IQuestionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/all")
    public R<List<Question>> getAll(@RequestBody Choice choice) {
        return questionService.getAll(choice);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("题目", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName+".xlsx");
        EasyExcel.write(response.getOutputStream(), Question.class).sheet("模板").doWrite(new ArrayList<Question>());
    }

}

