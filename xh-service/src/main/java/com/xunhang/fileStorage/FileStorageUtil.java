package com.xunhang.fileStorage;

import com.xunhang.pojo.vo.UploadImageVO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 风清默
 * @date 2024/7/7 15:31
 * @Package com.xunhang.thirdparty.fileStorage
 * @description: 文件服务上传
 */
@Component
public class FileStorageUtil {

    public static UploadImageVO uploadImage(MultipartFile file) {
        return uploadImage(file, "image");
    }

    public static UploadImageVO uploadImage(MultipartFile file, String dir) {
        IFileStorageService storageService = FileStorageLoadBalancer.getStorageService();
        return storageService.uploadImage(file, dir);
    }
}

