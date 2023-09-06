package com.chq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chq.common.R;
import com.chq.pojo.Course;
import com.chq.mapper.CourseMapper;
import com.chq.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-08-31
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Override
    public R getList() {
        return R.ok(list());
    }

    @Override
    public R add(Course course) {
        String courseName = course.getCourseName();
        Course one = getOne(new LambdaQueryWrapper<Course>().eq(Course::getCourseName, courseName));
        if (one!=null) return R.fail("该课程已经存在");
        save(new Course().setCourseName(courseName));
        return R.ok();
    }

    @Override
    public R get(String key) {
        List<Course> list = lambdaQuery().like(Course::getCourseName, key).list();
        List<String> collect = list.stream().map(item -> item.getCourseName()).collect(Collectors.toList());
        return R.ok(collect);
    }
}
