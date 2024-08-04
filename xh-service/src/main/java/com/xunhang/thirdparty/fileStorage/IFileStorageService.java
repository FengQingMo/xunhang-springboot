package com.xunhang.thirdparty.fileStorage;

import com.xunhang.pojo.vo.UploadImageVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 风清默
 * @date 2024/7/7 14:56
 * @Package com.xunhang.thirdparty.file
 * @description: 文件存储服务接口类
 */
public interface IFileStorageService {

    /**
     * 上传图片
     *
     * @param file 文件
     * @param dir 目录
     * @return 返回原图 和 缩略图
     */
     UploadImageVO uploadImage(MultipartFile file, String dir);

    /**
     * 上传文件
     *
     * @param file 文件
     * @param dir 目录
     * @return 文件url
     */
     String uploadFile(MultipartFile file,String dir) ;
    /**
     * 获取服务类型
     *
     * @return
     */
    Integer getType();

}
