package com.exception;


import com.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author kai
 * @create 2022-09-17 17:26
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result exception(RuntimeException e) {
        log.error("系统运行时异常 --> {}", e.getMessage());
        return Result.fail(e.getMessage());
    }
}
