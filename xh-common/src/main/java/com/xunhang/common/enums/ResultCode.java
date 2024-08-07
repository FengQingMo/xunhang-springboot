package com.xunhang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author Blue
 * @date 2020/10/19
 **/
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "成功"),
    NO_LOGIN(400, "未登录"),
    USERNAME_NOT_EXIST(407,"用户名不存在"),
    INVALID_TOKEN(401, "token无效或已过期"),
    PROGRAM_ERROR(500, "系统繁忙，请稍后再试"),
    PASSWORD_ERROR(601, "用户名或密码不正确"),
    OLD_PASSWORD_ERROR(602,"旧密码不正确"),
    USERNAME_ALREADY_REGISTER(603, "该用户名已注册"),
    XSS_PARAM_ERROR(604, "请不要输入非法内容"),
    FILE_UPLOAD_ERROR(605,"文件上传失败"),
    FILE_TOO_LARGE(606,"文件太大" );

    private final int code;
    private final String msg;
}

