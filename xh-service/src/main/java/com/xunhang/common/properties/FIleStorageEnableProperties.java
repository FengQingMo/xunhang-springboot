package com.xunhang.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author 风清默
 * @date 2024/7/7 16:51
 * @Package com.xunhang.common.properties
 * @description: 文件存储服务是否启用类
 */
@Component
@Data
@ConfigurationProperties(prefix = "xunhang.file.storage")
public class FIleStorageEnableProperties {
    private  Integer minio;
    private  Integer aliyunoss;
}
