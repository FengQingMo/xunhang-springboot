package com.xunhang.handle;

import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获自定义业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(GlobalException.class)
    public Result exceptionHandler(GlobalException ex){
        log.error("GlobalExceptionHandler捕捉到异常信息：{}", ex.getMessage());
        return ResultUtils.error(ex.getCode(),ex.getMessage());
    }

       /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    //@ExceptionHandler(Exception.class)
    //public Result exceptionHandler(Exception ex){
    //    log.error("ExceptionHanlde捕捉到异常信息：{}", ex.getMessage());
    //    return Result.error(ex.getMessage());
    //}

}
