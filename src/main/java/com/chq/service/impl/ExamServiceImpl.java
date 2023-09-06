package com.chq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import com.chq.mapper.AnswerMapper;
import com.chq.mapper.UserMapper;
import com.chq.pojo.Answer;
import com.chq.pojo.Exam;
import com.chq.mapper.ExamMapper;
import com.chq.pojo.dto.ExamDto;
import com.chq.pojo.vo.ExamVo;
import com.chq.pojo.dto.UserDto;
import com.chq.service.IExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chq.util.UserHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.chq.cache.CachePool.CLASS_CACHE;
import static com.chq.cache.CachePool.POSITION_CACHE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-08-31
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements IExamService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public R add(ExamDto dto) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(dto,exam);
        exam.setBeginTime(dto.getTimeArray().get(0));
        exam.setUserId(UserHolder.getUser().getId());
        exam.setEndTime(dto.getTimeArray().get(1));
        exam.setId(UserHolder.getUser().getId());
        save(exam);
        return R.ok();
    }

    @Override
    public R getDoing() {
        UserDto user = UserHolder.getUser();
        if ("学生".equals(POSITION_CACHE.get(user.getPositionId()))) {
            List<Exam> list = lambdaQuery().eq(Exam::getClassId, user.getClassId()).ge(Exam::getEndTime, LocalDateTime.now()).list();
            List<ExamVo> examVo = toExamVo(list);
            return R.ok(examVo);
        }
        List<Exam> list = lambdaQuery().eq(Exam::getUserId, user.getId()).ge(Exam::getEndTime, LocalDateTime.now()).list();
        List<ExamVo> examVo = toExamVo(list);
        return R.ok(examVo);
    }


    private List<ExamVo> toExamVo(List<Exam> list) {
        List<ExamVo> collect = list.stream().map(item -> {
            ExamVo vo = new ExamVo();
            BeanUtils.copyProperties(item, vo);
            vo.setClassName(CLASS_CACHE.get(item.getClassId()));
            vo.setName(userMapper.selectById(item.getUserId()).getName());
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public R getCompleted() {
        UserDto user = UserHolder.getUser();
        if ("学生".equals(POSITION_CACHE.get(user.getPositionId()))) {
            List<Exam> list = lambdaQuery().eq(Exam::getClassId, user.getClassId()).le(Exam::getEndTime, LocalDateTime.now()).list();
            List<ExamVo> examVo = toExamVo(list);
            return R.ok(examVo);
        }
        List<Exam> list = lambdaQuery().eq(Exam::getUserId, user.getId()).le(Exam::getEndTime, LocalDateTime.now()).list();
        List<ExamVo> examVo = toExamVo(list);
        return R.ok(examVo);
    }

    @Override
    public R<ExamVo> get(Integer examId) {
        Exam exam = getById(examId);
        ExamVo vo = new ExamVo();
        BeanUtils.copyProperties(exam,vo);
        vo.setClassName(CLASS_CACHE.get(exam.getClassId()));
        vo.setName(userMapper.selectById(exam.getUserId()).getName());
        return R.ok(vo);
    }

    @Override
    public R del(Integer examId) throws AuthException {
        if (!UserHolder.getUser().getId().equals(getById(examId).getUserId())) throw new AuthException("权限不够");
        removeById(examId);
        return R.ok();
    }

    @Override
    public R begin(Integer id) {
        Exam exam = getById(id);
        Answer answer = answerMapper.selectOne(new LambdaQueryWrapper<Answer>().eq(Answer::getExamId, id).eq(Answer::getUserId, UserHolder.getUser().getId()));
        if (answer!=null) return R.fail("你已作答过");
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(exam.getBeginTime())&&now.isBefore(exam.getEndTime())) return R.ok();
        return R.fail("考试尚未开始");
    }


}
