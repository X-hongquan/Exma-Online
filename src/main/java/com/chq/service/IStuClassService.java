package com.chq.service;

import com.chq.common.R;
import com.chq.pojo.StuClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
public interface IStuClassService extends IService<StuClass> {

    R get(Integer id);

    R add(StuClass stuClass);

    R del(Integer id);

    R<List<StuClass>> getList();
}
