package com.chq.controller;


import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import com.chq.pojo.Exam;
import com.chq.pojo.dto.ExamDto;
import com.chq.pojo.vo.ExamVo;
import com.chq.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-08-31
 */
@RestController
@RequestMapping("/exam")
public class ExamController {


    @Autowired
    private IExamService examService;


    @GetMapping
    public R<ExamVo> get(Integer examId) {
        return examService.get(examId);
    }

    @PostMapping
    public R add(@RequestBody ExamDto dto) {
        return examService.add(dto);
    }

    @GetMapping("/doing")
    public R getDoing() {
        return examService.getDoing();
    }

    @GetMapping("/completed")
    public R getCompleted() {
        return examService.getCompleted();
    }

    @DeleteMapping
    public R del(Integer examId) throws AuthException {
        return examService.del(examId);
    }

    @GetMapping("/begin")
    public R begin(Integer examId) {
        return examService.begin(examId);
    }



}
