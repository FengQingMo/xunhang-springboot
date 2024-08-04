package com.xunhang.thirdparty.fileStorage;

import com.xunhang.common.utils.UserUtil;
import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.enums.FileStorageType;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.pojo.vo.UploadImageVO;
import com.xunhang.utils.FileUtil;
import com.xunhang.utils.ImageUtil;
import com.xunhang.utils.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 通过校验文件MD5实现重复文件秒传
 * 文件上传服务
 *
 * @author Blue
 * @date 2022/10/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService implements IFileStorageService{


    private final MinioUtil minioUtil;
    @Value("${minio.public}")
    private String minIoServer;
    @Value("${minio.bucketName}")
    private String bucketName;

    private long maxImageSize = 5 * 1024 * 1024;
    private long maxFileSize = 5 * 1024 * 1024;

    @PostConstruct
    public void init() {
        if (!minioUtil.bucketExists(bucketName)) {
            // 创建bucket
            minioUtil.makeBucket(bucketName);
            // 公开bucket
            minioUtil.setBucketPublic(bucketName);
        }
    }

    @Override
    public String uploadFile(MultipartFile file,String dir) {
        Long userId = UserUtil.getCurrentId();
        // 大小校验
        if (file.getSize() > maxFileSize) {
            throw new GlobalException(ExceptionEnums.FILE_TOO_BIGGER);
        }
        // 上传
        String fileName = minioUtil.upload(bucketName, dir, file);
        if (StringUtils.isEmpty(fileName)) {
            throw new GlobalException(ExceptionEnums.FILE_UPLOAD_ERROR);
        }
        String url = generUrl(dir, fileName);
        log.info("文件文件成功，用户id:{},url:{}", userId, url);
        return url;
    }


    /**
     * 生成文件url
     *
     * @param dir 目录名
     * @param fileName 文件名
     * @return
     */
     public String generUrl(String dir, String fileName) {
        String url = minIoServer + "/" + bucketName + "/"  + dir + "/";
        url += fileName;
        return url;
    }

    @Override
    public UploadImageVO uploadImage(MultipartFile file,String dir) {
         try {
            Long userId = UserUtil.getCurrentId();
            // 大小校验
            if (file.getSize() > maxImageSize) {
                throw new GlobalException(ExceptionEnums.FILE_TOO_BIGGER);
            }
            // 图片格式校验
            if (!FileUtil.isImage(file.getOriginalFilename())) {
                throw new GlobalException(ExceptionEnums.FILE_FORMAT_ERROR);
            }
            // 上传原图
            UploadImageVO vo = new UploadImageVO();
            String fileName = minioUtil.upload(bucketName, dir, file);
            if (StringUtils.isEmpty(fileName)) {
                throw new GlobalException(ExceptionEnums.FILE_UPLOAD_ERROR);
            }
            vo.setOriginUrl(generUrl(dir, fileName));
            // 大于30K的文件需上传缩略图
            if (file.getSize() > 30 * 1024 && !"itemImage".equals(dir)) {
                byte[] imageByte = ImageUtil.compressForScale(file.getBytes(), 30);
                fileName = minioUtil.upload(bucketName, dir, Objects.requireNonNull(file.getOriginalFilename()), imageByte, file.getContentType());
                if (StringUtils.isEmpty(fileName)) {
                    throw new GlobalException(ExceptionEnums.FILE_UPLOAD_ERROR);
                }
            }
            vo.setThumbUrl(generUrl(dir, fileName));
            log.info("图片上传成功，用户id:{},url:{}", userId, vo.getOriginUrl());
            return vo;
        } catch (Exception e) {
            log.error("上传图片失败，{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Integer getType() {
        return FileStorageType.MINIO.getCode();
    }
}
