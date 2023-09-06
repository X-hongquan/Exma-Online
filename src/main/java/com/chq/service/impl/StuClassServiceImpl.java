package com.chq.service.impl;

import com.chq.common.R;
import com.chq.pojo.StuClass;
import com.chq.mapper.StuClassMapper;
import com.chq.service.IStuClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chq.cache.CachePool.CLASS_CACHE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
@Service
public class StuClassServiceImpl extends ServiceImpl<StuClassMapper, StuClass> implements IStuClassService {




    @PostConstruct
    public void putMap() {
        List<StuClass> list = list();
        list.forEach(o-> CLASS_CACHE.put(o.getId(),o.getClassName()));
    }

    @Override
    public R get(Integer id) {
        StuClass byId = getById(id);
        return R.ok(byId);
    }

    @Override
    public R add(StuClass stuClass) {
        if (save(stuClass)) {
            putMap();
            return R.ok();
        }
        return R.fail("服务器错误或id已存在");

    }

    @Override
    public R del(Integer id) {
        if (removeById(id)) {
            putMap();
            return R.ok();
        }
        return R.fail("服务器错误");
    }

    @Override
    public R<List<StuClass>> getList() {
        List<StuClass> list = list();
        return R.ok(list);
    }
}
