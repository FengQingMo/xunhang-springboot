package com.xunhang.common.result;


import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.enums.ResultCode;

public final class ResultUtils {

    private ResultUtils() {
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(T data, String messsage) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(messsage);
        result.setData(data);
        return result;
    }



    public static <T> Result<T> error(Integer code, String messsage) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(messsage);
        return result;
    }


    public static <T> Result<T> error(ResultCode resultCode, String messsage) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(messsage);
        return result;
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMsg());
        return result;
    }

    public static <T> Result<T> error(ExceptionEnums resultCode) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMessage());
        return result;
    }


}
