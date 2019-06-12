package com.gemii.auth.exception;

import com.gemii.auth.data.ResultContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-11 18:22
 **/
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginFailedException.class)
    public ResultContent<String> handleException(LoginFailedException e) {
        log.error("error:{}", e);
        ResultContent<String> result = new ResultContent<>();
        result.setResultCode(e.getExceptionCode());
        result.setDescription(e.getExceptionDesc());
        return result;
    }

}
