package com.chq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chq.common.R;
import com.chq.common.Type;
import com.chq.pojo.Choice;
import com.chq.pojo.Question;
import com.chq.mapper.QuestionMapper;
import com.chq.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-08
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Override
    public R<List<Question>> getByTestId(Integer testId) {
        List<Question> list = lambdaQuery().eq(Question::getTestId, testId).orderByAsc(Question::getSort).list();
        Long count = lambdaQuery().eq(Question::getTestId, testId).le(Question::getTypeId, Type.JUDGE.getKey()).count();
        return R.ok(list,count);
    }

    @Override
    public R<String> getResult(Integer testId) {
        String s = RadioList(testId);
        return R.ok(s);
    }

    @Override
    public R<List<Question>> getAll(Choice choice) {

        Page<Question> page = new Page<>(Long.valueOf(choice.getConcurrentPage()), 10);
        LambdaQueryWrapper<Question> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Question::getCourseId,choice.getCourseId()).eq(Question::getDegree,choice.getDegree()).eq(Question::getTypeId,choice.getTypeId());
        Page<Question> page1 = page(page,lqw);
        List<Question> records = page1.getRecords();
        return R.ok(records,page1.getTotal());
    }

    public String RadioList(Integer testId) {
        List<Question> list = lambdaQuery().eq(Question::getTestId, testId).le(Question::getTypeId, Type.JUDGE.getKey()).orderByAsc(Question::getSort).list();
        StringBuilder builder = new StringBuilder();
        for (Question question : list) {
            builder.append(question.getResult());
        }
        return builder.toString();
    }
}
