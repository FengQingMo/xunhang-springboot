package com.xunhang.common.enums;

import lombok.Getter;

/**
 * @author cuikecheng
 * @version 1.0
 * @description:
 * @date 2024/6/26 上午10:01
 */
@Getter
public enum ExceptionEnums {
    SUCCESS(200, "成功"),
      /**
     * 未登录
     */
    NO_LOGIN(400, "未登录"),
    INVALID_TOKEN(401, "token无效或已过期"),
    FAIL(500, "失败"),
    TOKEN_DECODE_ERROR(501,"TOKEN非法"),
    USER_NUM_ERROR(502,"用户数量出错"),
    GATEWAY_ERROR(503, "非网关传递的请求"),
    REFRESH_TOKEN_OVERDUE(504,"refreshToken无效或已过期"),
    FILE_TOO_BIGGER(505,"文件太大"),
    FILE_UPLOAD_ERROR(506,"文件上传失败"),
    FILE_FORMAT_ERROR(507,"文件格式错误"),
    WX_ACCESSTOKEN_ERROR(508,"调用微信accessToken失败"),
    GET_OPENID_ERROR(509,"获取openid出错"),
    AUTHENTICATION_ERROR(510, "用户名或密码错误"),
    FILE_STORAGE_ERROR(511,"文件存储服务异常，管理员正在修复，很抱歉为您带来不便！"),

    USER_NOT_EXIST(512,"用户不存在" );

    private final Integer code;
    private final String message;
    ExceptionEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
