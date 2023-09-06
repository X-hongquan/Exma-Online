package com.chq.controller;


import com.chq.common.R;
import com.chq.pojo.Course;
import com.chq.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-08-31
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;



    @PostMapping
    public R add(@RequestBody Course course) {
        return courseService.add(course);
    }


    @Value("${minio.bucket.answer}")
    private String answer;

    @Value("${minio.endpoint}")
    private String server;


    @DeleteMapping
    public R del(Integer id) {
        courseService.removeById(id);
        return R.ok();
    }

    @GetMapping
    public R get(String key) {
        return courseService.get(key);
    }



    @GetMapping("/list")
    public R getList() {
        return courseService.getList();
    }



}
