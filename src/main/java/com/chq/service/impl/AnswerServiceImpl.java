package com.chq.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chq.common.R;
import com.chq.common.Type;
import com.chq.mapper.*;
import com.chq.pojo.*;
import com.chq.pojo.dto.AnswerDto;

import com.chq.service.IAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.chq.util.UserHolder;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-09-02
 */
@Service
@Slf4j
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {


    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.answer}")
    private String answer;

    @Value("${minio.endpoint}")
    private String server;


    @Resource
    private ScoreMapper scoreMapper;


    @Resource
    private ExamMapper examMapper;


    @Resource
    private QuestionMapper questionMapper;





    @Override
    public R upload(byte[] bytes, String contentType, String originalFilename) {
        String s = DigestUtil.md5Hex(bytes);
        String tail = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = s + tail;
        try (ByteArrayInputStream bs = new ByteArrayInputStream(bytes)) {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().object(fileName)
                    .contentType(contentType).stream(bs, bytes.length, -1)
                    .bucket(answer)
                    .build();
            minioClient.putObject(putObjectArgs);
            log.info("上传成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(server).append("/").append(answer).append("/").append(fileName);
        return R.ok(builder);
    }

    @Override
    public R add(AnswerDto answer) {
        Integer examId = answer.getExamId();
        LocalDateTime now = LocalDateTime.now();
        boolean after = now.isAfter(examMapper.selectById(answer.getExamId()).getEndTime());
        if (after) return R.fail("你已超时");
        Answer po = new Answer();
        po.setExamId(examId);
        po.setUserId(UserHolder.getUser().getId());
        List<String> radioList = answer.getRadioList();
        StringBuilder radio = new StringBuilder();
        for (String s : radioList) {
            radio.append(s);
        }
        po.setRadioList(radio.toString());
        List<String> otherList = answer.getOtherList();
        StringBuilder other = new StringBuilder();
        for (String s : otherList) {
            if (StringUtils.isNotBlank(s)) {
            other.append(s).append(";");
            }
        }
        po.setOtherList(other.toString());
        save(po);
        return R.ok();
    }

    @Override
    public R get(Integer examId, Integer userId) {
        Answer one = getOne(new LambdaQueryWrapper<Answer>().eq(Answer::getExamId, examId).eq(Answer::getUserId, userId));
        if (one==null) {
            Score score = new Score();
            score.setUserId(userId);
            score.setExamId(examId);
            score.setOtherScore(0);
            score.setSelectScore(0);
            scoreMapper.insert(score);
            return R.fail("该生没有作答,0分");
        }
        return R.ok(one);
    }

    @Override
    public R<Answer> getAnswer(Integer id) {
        Answer byId = getById(id);
        String radioList = byId.getRadioList();
        char[] charArray = radioList.toCharArray();
        int radioScore = 0;
        int i=0;
        Exam exam = examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getId, byId.getExamId()));
        String testId = exam.getTestId();
        List<Question> questions = questionMapper.selectList(new LambdaQueryWrapper<Question>()
                .eq(Question::getTestId, testId)
                .le(Question::getTypeId, Type.JUDGE.getKey())
                .orderByAsc(Question::getSort));
        for (Question question : questions) {
            String result = question.getResult();
            char c = charArray[i++];
            if (result.equals(String.valueOf(c)))
                    radioScore+=question.getGrade();
        }
        return R.ok(byId,Long.valueOf(radioScore));
    }
}
