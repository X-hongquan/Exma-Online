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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    private R add(@RequestBody User user) {
        return userService.add(user);
    }

    @DeleteMapping
    private R del(Integer id) {
        return userService.del(id);
    }

    @PutMapping
    private R up(@RequestBody User user) {
        return userService.up(user);
    }

    @GetMapping("/list")
    public R<List<UserVo>> getList(Integer currentPage) {
        return userService.getList(currentPage);
    }


    @PostMapping("/rLogin")
    private R adminLogin(@RequestBody LoginDto loginDto) throws AuthException {
        return userService.adminLogin(loginDto);
    }

    @GetMapping("/me")
    public R getMe() {
         return userService.getMe();
    }

    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto) throws AuthException {
        return userService.login(loginDto);
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
    public R updatePwd(@RequestBody PasswordDto passwordDto) throws AuthException {
        return userService.updatePwd(passwordDto);
    }

    @GetMapping("/code")
    public R sendCode() {
        return userService.sendCode();
    }



    @GetMapping("download")
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

}

