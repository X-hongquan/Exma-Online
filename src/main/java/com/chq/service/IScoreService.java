package com.chq.service;

import com.chq.common.R;
import com.chq.pojo.Score;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chq.pojo.vo.ScoreVo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-09-04
 */
public interface IScoreService extends IService<Score> {

    R add(Score score);

    R<List<ScoreVo>> listAll(Integer examId);


}
