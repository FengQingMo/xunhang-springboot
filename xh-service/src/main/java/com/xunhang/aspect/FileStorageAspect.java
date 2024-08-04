package com.xunhang.aspect;

import com.xunhang.common.enums.FileStorageType;
import com.xunhang.thirdparty.fileStorage.FileStorageLoadBalancer;
import com.xunhang.thirdparty.fileStorage.IFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author 风清默
 * @date 2024/7/21 14:30
 * @Package com.xunhang.aspect
 * @description: 上传文件切面 获取上传时间
 */
@Component
@Slf4j
@Aspect
public class FileStorageAspect {


    @Pointcut("execution(* com.xunhang.thirdparty.fileStorage.IFileStorageService.uploadImage(..))")
    public void monitorUploadTime(){}


    @Around("monitorUploadTime()")
    public Object arouneMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result  = joinPoint.proceed();
        long responseTime = System.currentTimeMillis()- beginTime;

        // 获取目标对象和参数
        Object target = joinPoint.getTarget();
        Integer type = ((IFileStorageService) target).getType();

        log.info(FileStorageType.getDescByCode(type) + "服务上传花费时间为:"+ responseTime + "ms") ;

        FileStorageLoadBalancer.setResponseTime(type,responseTime);
        return result;
    }


}
