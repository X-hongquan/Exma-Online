package com.chq.controller.advice;


import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {




    @ExceptionHandler(AuthException.class)
    public R authError(AuthException e) {
        String message = e.getMessage();
        return R.fail(message);
    }
}
