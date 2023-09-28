package com.chq.service;

import com.chq.common.R;
import com.chq.pojo.Choice;
import com.chq.pojo.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-08
 */
public interface IQuestionService extends IService<Question> {

    R<List<Question>> getByTestId(Integer testId);

    R<String> getResult(Integer testId);

    R<List<Question>> getAll(Choice choice);
}
