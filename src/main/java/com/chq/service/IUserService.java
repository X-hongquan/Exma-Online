package com.chq.service;

import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import com.chq.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chq.pojo.dto.LoginDto;
import com.chq.pojo.dto.PasswordDto;
import com.chq.pojo.dto.UserDto;
import com.chq.pojo.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-07
 */
public interface IUserService extends IService<User> {

    R add(User user);

    R del(Integer id);

    R up(User user);

    R get(Integer id);

    R<List<UserVo>> getList(Integer currentPage);

    R adminLogin(LoginDto loginDto) ;

    R logout(String token);

    R login(LoginDto loginDto) ;

    R getMe();

    R<List<UserDto>> listByClassId(Integer classId,Integer examId);

    R excelAdd(MultipartFile file) throws IOException;

    R updatePwd(PasswordDto passwordDto) ;

    R sendCode();

    R tour();

    void getUse(HttpServletResponse response);

    R andLogin(String token);
}
