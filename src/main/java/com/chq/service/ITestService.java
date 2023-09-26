package com.chq.service;

import com.chq.common.R;
import com.chq.pojo.Test;
import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-08
 */
public interface ITestService extends IService<Test> {


//    R upload(MultipartFile file, String contentType, String originalFilename) throws IOException;

    R upload(byte[] bytes, String contentType, String originalFilename) throws IOException;

    R getList();
}
