package com.xunhang.common.enums;

/**
 * @Author 风清默
 * @date 2024/7/7 14:58
 * @Package com.xunhang.common.enums
 * @description: 文件存储服务类型
 */
public enum FileStorageType {
    /**
     * minio存储服务
     */
    MINIO(1,"minio"),
    /**
     * 阿里云oss存储服务
     */
    ALIYUNOSS(2,"aliyunoss");

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    // 静态方法来获取所有枚举实例的code值
    public static int[] getAllCodes() {
        FileStorageType[] values = values();
        int[] codes = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            codes[i] = values[i].getCode();
        }
        return codes;
    }
       public static String getDescByCode(int code) {
        for (FileStorageType type : FileStorageType.values()) {
            if (type.getCode() == code) {
                return type.getDesc();
            }
        }
        return null; // 或者抛出一个异常，或者返回一个默认值
    }

    FileStorageType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

}
