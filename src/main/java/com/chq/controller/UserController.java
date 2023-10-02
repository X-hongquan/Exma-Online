package com.chq.controller;


import com.alibaba.excel.EasyExcel;
import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import com.chq.pojo.User;
import com.chq.pojo.dto.LoginDto;
import com.chq.pojo.dto.PasswordDto;
import com.chq.pojo.dto.UserDto;
import com.chq.pojo.eo.StuClassEo;
import com.chq.pojo.eo.UserEo;
import com.chq.pojo.vo.UserVo;
import com.chq.service.IUserService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileInputStream;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @GetMapping
    private R get(Integer id) {
        return userService.get(id);
    }

    @PostMapping
    public R add(@RequestBody User user) {
        return userService.add(user);
    }

    @DeleteMapping
    public R del(Integer id) {
        return userService.del(id);
    }

    @PutMapping
    public R up(@Validated @RequestBody User user) {
        return userService.up(user);
    }

    @GetMapping("/list")
    public R<List<UserVo>> getList(Integer currentPage) {
        return userService.getList(currentPage);
    }


    @PostMapping("/rLogin")
    public R adminLogin(@Validated @RequestBody LoginDto loginDto) {
        return userService.adminLogin(loginDto);
    }

    @GetMapping("/me")
    public R getMe() {
         return userService.getMe();
    }

    @GetMapping("/isLogin")
    public R andLogin(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        return userService.andLogin(token);
    }

    @PostMapping("/login")
    public R login(@Validated @RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @GetMapping("/tour")
    public R tour() {
        return userService.tour();

    }

    @GetMapping("/logout")
    public R logout(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        return userService.logout(token);
    }

    @GetMapping("/class")
    public R<List<UserDto>> listByClassId(Integer classId,Integer examId) {
        return userService.listByClassId(classId,examId);
    }

    @PostMapping("/excel")
    public R excelAdd(@RequestParam("file") MultipartFile file) throws IOException {
        return userService.excelAdd(file);
    }

    @PutMapping("/pwd")
    public R handleUpdatePwd(@Validated @RequestBody PasswordDto passwordDto)  {
        return userService.updatePwd(passwordDto);
    }

    @GetMapping("/code")
    public R handleSendCode() {
        return userService.sendCode();
    }


    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("班级", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName+".xlsx");
        List<UserEo> collect = userService.list().stream().map(item -> {
            UserEo eo = new UserEo();
            BeanUtils.copyProperties(item, eo);
            return eo;
        }).collect(Collectors.toList());
        EasyExcel.write(response.getOutputStream(), UserEo.class).sheet("模板").doWrite(collect);
    }

    @GetMapping("/use")
    public void getUse(HttpServletResponse response) {
         userService.getUse(response);
    }

}

