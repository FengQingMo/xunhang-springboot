package com.xunhang.service;

import com.xunhang.pojo.vo.UploadImageVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 风清默
 * @date 2024/7/9 21:18
 * @Package com.xunhang.service
 * @description: 文件上传接口类
 */

public interface FileService {

    UploadImageVO uploadImage(MultipartFile file, String filePath);
}
