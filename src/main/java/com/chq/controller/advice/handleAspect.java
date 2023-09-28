package com.chq.controller.advice;

import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import com.chq.pojo.dto.UserDto;
import com.chq.util.UserHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class handleAspect {

    //方法修饰？ 返回值类型 类路径.方法名(..)参数任意
    @Pointcut("execution(* com.chq.controller.*.handle*(..))")
    public void pointCut(){}


    @Around("pointCut()")
    public R around(ProceedingJoinPoint joinPoint) throws Throwable {
        UserDto user = UserHolder.getUser();
        if (user.getStatus()==2) throw new AuthException("游客模式,有限制");
        else if (user.getStatus()==1) {
            throw new AuthException("账号异常");
        }
        return (R)joinPoint.proceed();

    }
}
