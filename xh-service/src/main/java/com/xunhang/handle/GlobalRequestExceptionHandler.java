package com.xunhang.handle;


import com.xunhang.common.enums.ResultCode;
import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 拦截所有@RequestMapping方法的抛异常情形并执行指定的处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalRequestExceptionHandler {

    /******************************************登录、认证、权限异常类**********************************************/

    /**
     * 认证失败异常提醒
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result handleValidateCodeException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    /**
     * 没有权限异常提醒
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return Result.error("没有权限，请联系管理员授权");
    }

    /**
     * 登录超时异常提醒
     */
    @ExceptionHandler(AccountExpiredException.class)
    public Result handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return Result.error( "登录超时,请重新登录");
    }

    /**
     * 用户名不存在异常提醒
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public Result handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return Result.error("用户名不存在");
    }

    /*********************************************接口请求异常类**************************************************/

    /**
     * 网络波动导致的请求数据包部分数据丢失异常
     */
    @ExceptionHandler(ClientAbortException.class)
    public Result handleClientAbortException(Exception e, HttpServletRequest request) {
        log.info("request URL:" + request.getRequestURL());
        log.error(e.getMessage(), e);
        return Result.error( e.getMessage());
    }

    /**
     * 接口请求参数类型、个数不对异常提醒
     **/
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodParam(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.info("request URL:" + request.getRequestURL());
        log.error(e.getMessage(), e);
        return Result.error("前端传入参数" + e.getName() + "不符合接口格式");
    }

    /**
     * 接口请求方式不对异常提醒
     **/
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.info("request URL:" + request.getRequestURL());
        log.error(e.getMessage(), e);
        return Result.error("请求地址'" + request.getRequestURL() + "',不支持'" + e.getMethod() + "'请求");
    }

    /**
     * 接口路径不对异常提醒
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handlerNoFoundException(Exception e, HttpServletRequest request) {
        log.info("request URL:" + request.getRequestURL());
        log.error(e.getMessage(), e);
        return Result.error("请求地址'" + request.getRequestURL() + "',不存在，请检查路径是否正确");
    }

    /**
     * 方法参数无效
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public Result handleValidated(Exception e, HttpServletRequest request) {
        List<FieldError> fieldErrors = null;
        if (e instanceof BindException){
            BindException e1=(BindException) e;
            fieldErrors= e1.getBindingResult().getFieldErrors();
        }else {
            MethodArgumentNotValidException e2 = (MethodArgumentNotValidException) e;
            fieldErrors =e2.getBindingResult().getFieldErrors();
        }
        List<String> validationResults = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            validationResults.add(fieldError.getDefaultMessage());
        }
        String messages = StringUtils.join(Arrays.asList(validationResults.toArray()), ";");
        log.info("request URL:" + request.getRequestURL());
        log.error("方法参数无效:" + messages);
        return Result.error(messages);
    }

    /**
     * 请求参数异常
     **/
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleParamException(ConstraintViolationException e, HttpServletRequest request) {
        List<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            msgList.add(constraintViolation.getMessage());
        }
        String messages = StringUtils.join(Arrays.asList(msgList.toArray()), ";");
        log.info("request URL:" + request.getRequestURL());
        log.error("请求参数异常:" + messages);
        return Result.error(messages);
    }

    /*************************************************SQL异常类*****************************************************/

    /**
     * 通用SQL异常提醒
     */
    @ExceptionHandler(SQLException.class)
    public Result handlerSqlException(SQLException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    /**
     * SQL记录已存在异常提醒
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.error("数据库中已存在该记录");
    }
    /*************************************************文件异常类*****************************************************/
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public Result handleFileSizeLimitExceededException(FileSizeLimitExceededException e){
        return ResultUtils.error(ResultCode.FILE_TOO_LARGE);
    }
}