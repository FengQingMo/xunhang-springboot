package com.xunhang.common.exception;

import com.xunhang.common.enums.ExceptionEnums;
import lombok.Data;

/**
 * @author cuikecheng
 * @version 1.0
 * @description:
 * @date 2024/6/26 上午9:59
 */

@Data
public class GlobalException extends RuntimeException{
    private Integer code;
    private String message;


    public GlobalException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public GlobalException(ExceptionEnums enums){
        this.code = enums.getCode();
        this.message = enums.getMessage();
    }
}
