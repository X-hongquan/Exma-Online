package com.chq.service;

import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import com.chq.pojo.Exam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chq.pojo.dto.ExamDto;
import com.chq.pojo.vo.ExamVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-08-31
 */
public interface IExamService extends IService<Exam> {

    R add(ExamDto dto);

    R getDoing();

    R getCompleted();

    R<ExamVo> get(Integer examId);

    R del(Integer examId) throws AuthException;

    R begin(Integer id);
}
