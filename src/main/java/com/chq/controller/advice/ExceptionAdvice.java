package com.chq.controller.advice;


import com.chq.common.R;
import com.chq.controller.advice.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionAdvice {




    @ExceptionHandler(AuthException.class)
    public R authError(AuthException e) {
        String message = e.getMessage();
        log.error(message);
        return R.fail(message);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return R.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }


}
