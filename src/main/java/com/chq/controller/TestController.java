package com.chq.controller;


import cn.hutool.crypto.digest.DigestUtil;
import com.chq.common.R;
import com.chq.service.ITestService;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
    public R upload(@RequestParam("file")MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        return testService.upload(bytes,contentType,originalFilename);
    }
}

