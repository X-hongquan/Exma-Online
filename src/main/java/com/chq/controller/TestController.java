package com.chq.controller;


import cn.hutool.crypto.digest.DigestUtil;
import com.chq.common.R;
import com.chq.common.Role;
import com.chq.pojo.Test;
import com.chq.pojo.dto.TestDto;
import com.chq.service.ITestService;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
@RequestMapping("/test")
public class TestController {


    @Autowired
    private ITestService testService;


    @PostMapping("/upload")
    public R handleUpload(@RequestParam("file")MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        return testService.upload(bytes,contentType,originalFilename);
    }

    @GetMapping("/list/id")
    public R<List<Test>> list() {
        return testService.getList();
    }


    @DeleteMapping
    public R handleDel(Integer id) {
        return testService.del(id);
    }

    @PutMapping("/name")
    public R handleUpdateName(@RequestBody Test test) {
        return testService.updateName(test);
    }

    @PostMapping
    @Role
    public R handleAdd(@RequestBody TestDto testDto) {
        return testService.handleAdd(testDto);
    }
}

