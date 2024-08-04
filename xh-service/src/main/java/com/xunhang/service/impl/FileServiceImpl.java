package com.xunhang.service.impl;

import com.xunhang.pojo.vo.UploadImageVO;
import com.xunhang.service.FileService;
import com.xunhang.thirdparty.fileStorage.FileStorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 风清默
 * @date 2024/7/9 21:18
 * @Package com.xunhang.service.impl
 * @description: 文件上传接口实现类
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {


    @Override
    public UploadImageVO uploadImage(MultipartFile file, String filePath) {
        return FileStorageUtil.uploadImage(file, filePath);
    }
}
