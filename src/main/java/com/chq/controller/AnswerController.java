package com.chq.controller;


import com.chq.common.R;
import com.chq.pojo.Answer;
import com.chq.pojo.dto.AnswerDto;
import com.chq.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-09-02
 */
@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private IAnswerService answerService;


    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        return answerService.upload(bytes,contentType,originalFilename);
    }

    @PostMapping
    public R add(@RequestBody AnswerDto answer) {
        return answerService.add(answer);
    }

    @GetMapping
    public R handleGet(Integer examId,Integer userId) {
        return answerService.get(examId,userId);
    }

    @GetMapping("/byId")
    public R<Answer> getAnswer(Integer id) {
        return answerService.getAnswer(id);
    }

}
