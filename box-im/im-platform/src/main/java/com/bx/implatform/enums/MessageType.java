package com.bx.implatform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum MessageType {

    /**
     * 文字
     */
    TEXT(0, "文字"),
    /**
     * 图片
     */
    IMAGE(1, "图片"),
    /**
     * 文件
     */
    FILE(2, "文件"),
    /**
     * 音频
     */
    AUDIO(3, "音频"),
    /**
     * 视频
     */
    VIDEO(4, "视频"),
    /**
     * 撤回
     */
    RECALL(10, "撤回"),
    /**
     * 已读
     */
    READED(11, "已读"),

    /**
     * 消息已读回执
     */
    RECEIPT(12, "消息已读回执"),
    /**
     * 时间提示
     */
    TIP_TIME(20,"时间提示"),
    /**
     * 文字提示
     */
    TIP_TEXT(21,"文字提示"),

    /**
     * 消息加载标记
     */
    LOADDING(30,"加载中"),

    /**
     * 语音呼叫
     */
    RTC_CALL_VOICE(100, "语音呼叫"),
    /**
     * 视频呼叫
     */
    RTC_CALL_VIDEO(101, "视频呼叫"),
    /**
     * 接受
     */
    RTC_ACCEPT(102, "接受"),
    /**
     * 拒绝
     */
    RTC_REJECT(103, "拒绝"),
    /**
     * 取消呼叫
     */
    RTC_CANCEL(104, "取消呼叫"),
    /**
     * 呼叫失败
     */
    RTC_FAILED(105, "呼叫失败"),
    /**
     * 挂断
     */
    RTC_HANDUP(106, "挂断"),
    /**
     * 同步candidate
     */
    RTC_CANDIDATE(107, "同步candidate");

    private final Integer code;

    private final String desc;


    public Integer code() {
        return this.code;
    }
}
