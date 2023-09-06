package com.chq.service;

import com.chq.common.R;
import com.chq.pojo.Answer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chq.pojo.dto.AnswerDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-09-02
 */
public interface IAnswerService extends IService<Answer> {

    R upload(byte[] bytes, String contentType, String originalFilename);

    R add(AnswerDto answer);

    R get(Integer examId, Integer userId);

    R<Answer> getAnswer(Integer id);
}
