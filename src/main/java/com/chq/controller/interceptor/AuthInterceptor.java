package com.chq.controller.interceptor;


import com.chq.common.Role;
import com.chq.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;


public class AuthInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Method method = ((HandlerMethod) handler).getMethod();
        if (method.isAnnotationPresent(Role.class)) {
            Role role = method.getAnnotation(Role.class);
            Integer id = UserHolder.getUser().getPositionId();
            int value = role.value();
            if (id!=value) {
                response.setStatus(403);
                return false;
            }
        }
        return true;
    }
}
