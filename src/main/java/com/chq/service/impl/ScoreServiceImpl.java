package com.chq.service.impl;

import com.alibaba.excel.EasyExcel;
import com.chq.common.R;
import com.chq.mapper.CourseMapper;
import com.chq.mapper.ExamMapper;
import com.chq.mapper.UserMapper;
import com.chq.pojo.Course;
import com.chq.pojo.Exam;
import com.chq.pojo.Score;
import com.chq.mapper.ScoreMapper;
import com.chq.pojo.User;
import com.chq.pojo.dto.MsgDto;
import com.chq.pojo.eo.UserEo;
import com.chq.pojo.vo.ScoreVo;
import com.chq.service.IScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chq.util.Sender;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import static com.chq.cache.CachePool.CLASS_CACHE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-09-04
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements IScoreService {



    @Resource
    private UserMapper userMapper;




    @Resource
    private ExamMapper examMapper;



    @Override
    public R add(Score score) {
            save(score);
            return R.ok();
    }

    @Override
    public R<List<ScoreVo>> listAll(Integer examId) {
        List<Score> list = lambdaQuery().eq(Score::getExamId, examId).list();
        List<ScoreVo> collect = list.stream().map(item -> {
            Integer userId = item.getUserId();
            User user = userMapper.selectById(userId);
            ScoreVo scoreVo = new ScoreVo();
            scoreVo.setUserId(userId);
            scoreVo.setName(user.getName());
            scoreVo.setSno(user.getSno());
            scoreVo.setClassName(CLASS_CACHE.get(user.getClassId()));
            scoreVo.setSubject(examMapper.selectById(examId).getCourseName());
            scoreVo.setScore(Integer.sum(item.getSelectScore(), item.getOtherScore()));
            return scoreVo;
        }).collect(Collectors.toList());
        return R.ok(collect);
    }


}
