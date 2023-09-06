package com.chq.service;

import com.chq.common.R;
import com.chq.pojo.Position;
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
public interface IPositionService extends IService<Position> {

    R get(Integer id);

    R add(Position position);

    R del(Integer id);

    R<List<Position>> getList();
}
