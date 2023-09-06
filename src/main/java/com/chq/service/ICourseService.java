package com.chq.service;

import com.chq.common.R;
import com.chq.pojo.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-08-31
 */
public interface ICourseService extends IService<Course> {

    R getList();

    R add(Course course);

    R get(String key);
}
