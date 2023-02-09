package com.cheng.handle.exception;

import com.cheng.domain.ResponseResult;
import com.cheng.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice等于下面的
 * @ControllerAdvice
 * @ResponseBody
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     *
     * @param e 自定义全局异常
     * @return 将自定义异常封装到 ResponseResult并返回
     */
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){

        //打印异常信息
        log.error("出现了异常！",e);
        //从异常信息中获取提示信息并返回
        return new ResponseResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){

        //打印异常信息
        log.error("出现了系统异常！",e);
        //从异常信息中获取提示信息并返回
        return new ResponseResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }


}














