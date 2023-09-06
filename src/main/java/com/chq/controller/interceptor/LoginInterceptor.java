package com.chq.controller.interceptor;

import cn.hutool.json.JSONUtil;

import com.chq.pojo.dto.UserDto;

import com.chq.util.UserHolder;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;


import static com.chq.common.RedisConstant.LOGIN_KEY;

public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        if (StringUtils.isBlank(token)) {
            response.setStatus(403);
            return false;
        }
        String key = LOGIN_KEY+token;
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(s)) {
            response.setStatus(403);
            return false;
        }
        UserDto bean = JSONUtil.toBean(s, UserDto.class);
        UserHolder.saveUser(bean);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       UserHolder.removeUser();
    }
}
