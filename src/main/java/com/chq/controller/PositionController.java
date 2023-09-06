package com.chq.controller;


import com.chq.common.R;
import com.chq.pojo.Position;
import com.chq.service.IPositionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/position")
public class PositionController {

    @Resource
    private IPositionService positionService;

    @GetMapping
    public R get(Integer id) {
        return positionService.get(id);
    }

    @PostMapping
    public R add(@RequestBody Position position) {
        return positionService.add(position);
    }

    @DeleteMapping
    public R del(Integer id) {
        return positionService.del(id);
    }

    @GetMapping("/list")
    private R<List<Position>> getList() {
        return positionService.getList();
    }

}

