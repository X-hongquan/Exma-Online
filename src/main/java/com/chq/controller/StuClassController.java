package com.chq.controller;


import com.alibaba.excel.EasyExcel;
import com.chq.common.R;
import com.chq.pojo.StuClass;
import com.chq.pojo.eo.StuClassEo;
import com.chq.service.IStuClassService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/stuClass")
public class StuClassController {

    @Autowired
    private IStuClassService stuClassService;

    @GetMapping
    public R get(Integer id) {
        return stuClassService.get(id);
    }

    @PostMapping
    private R add(@RequestBody StuClass stuClass) {
        return stuClassService.add(stuClass);
    }

    @DeleteMapping
    private R del(Integer id) {
        return stuClassService.del(id);
    }

    @GetMapping("/list")
    public R<List<StuClass>> getList() {
        return stuClassService.getList();
    }

    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("班级", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName+".xlsx");
        List<StuClassEo> collect = stuClassService.list().stream().map(item -> {
            StuClassEo stuClassEo = new StuClassEo();
            BeanUtils.copyProperties(item, stuClassEo);
            return stuClassEo;
        }).collect(Collectors.toList());
        EasyExcel.write(response.getOutputStream(), StuClassEo.class).sheet("模板").doWrite(collect);

    }


}

