package com.chq.service.impl;

import com.chq.common.R;
import com.chq.pojo.Position;
import com.chq.mapper.PositionMapper;
import com.chq.service.IPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chq.cache.CachePool.POSITION_CACHE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {



    @PostConstruct
    public void  putMap() {
        list().stream().forEach(item->POSITION_CACHE.put(item.getId(), item.getPosition()));
    }


    @Override
    public R get(Integer id) {
        Position position = getById(id);
        return R.ok(position);
    }

    @Override
    public R add(Position position) {
        if (save(position)) {
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
    public R<List<Position>> getList() {
        List<Position> list = list();
        return R.ok(list);
    }
}
