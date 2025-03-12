package com.muybaby.food.exception;

import com.muybaby.food.bean.Result;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        // Log the exception
//        e.printStackTrace();
        log.error("发生异常:exception: ", e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Object> handleRuntimeException(RuntimeException e) {
        // Log the exception
//        e.printStackTrace();
        log.error("你好:A runtime exception occurred: ", e.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST, "请求错误: " + e.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public Result<Object> handleSMTPAddressFailedException(SMTPAddressFailedException e) {
        // Log the exception
//        e.printStackTrace();
        log.error("MessagingException: ", e);
        return Result.error(HttpStatus.BAD_REQUEST, "邮箱地址错误: " + e.getMessage());
    }
}