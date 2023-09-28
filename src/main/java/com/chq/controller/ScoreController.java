package com.chq.controller;


import com.alibaba.excel.EasyExcel;
import com.chq.common.R;
import com.chq.pojo.Score;
import com.chq.pojo.eo.UserEo;
import com.chq.pojo.vo.ScoreVo;
import com.chq.service.IScoreService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-09-04
 */
@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private IScoreService scoreService;

    @PostMapping
    public R handleAdd(@RequestBody Score score) {
        return scoreService.add(score);
    }

    @GetMapping("/list")
    public R<List<ScoreVo>> listAll(Integer examId) {
        return scoreService.listAll(examId);
    }

    @GetMapping("/download")
    public void handleDownload(Integer examId,HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("班级", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName+".xlsx");
        R<List<ScoreVo>> r = scoreService.listAll(examId);
        List<ScoreVo> data = r.getData();
        List<ScoreVo> list = data.stream().sorted((o1, o2) -> o2.getScore() - o1.getScore()).toList();
        EasyExcel.write(response.getOutputStream(), ScoreVo.class).sheet("模板").doWrite(list);

    }

    @GetMapping
    public R<Integer> getScore(Integer examId) {
        return scoreService.getScore(examId);
    }
}
