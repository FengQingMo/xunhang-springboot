package com.xunhang.thirdparty.fileStorage;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.xunhang.common.enums.FileStorageType;
import com.xunhang.pojo.vo.UploadImageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author 风清默
 * @date 2024/7/7 15:07
 * @Package com.xunhang.thirdparty.fileStorage
 * @description: 阿里云oss存储服务
 */
@Service
@Slf4j
public class AliyunossService implements IFileStorageService {

    @Value("${xunhang.alioss.endpoint}")
    private  String endpoint;
     @Value("${xunhang.alioss.access-key}")
    private String accessKeyId;
     @Value("${xunhang.alioss.access-key-secret}")
    private String accessKeySecret;
     @Value("${xunhang.alioss.bucket-name}")
    private String bucketName;
    @Override
    public UploadImageVO uploadImage(MultipartFile file, String dir) {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            String today = DateUtil.today();
            long currentTimeMillis = System.currentTimeMillis();
            String fileName = file.getOriginalFilename();
            String objectName = dir + '/' + today + '/' + currentTimeMillis + fileName.substring(fileName.lastIndexOf('.'));
            try {
                // 创建PutObject请求。
                ossClient.putObject(bucketName, objectName, file.getInputStream());
            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, " + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
                return null;
            } catch (ClientException | IOException ce) {
                System.out.println("Caught an ClientException, which means the client encountered " + "a serious internal problem while trying to communicate with OSS, " + "such as not being able to access the network.");
                System.out.println("Error Message:" + ce.getMessage());
                return null;
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
            //文件访问路径规则 https://BucketName.Endpoint/ObjectName
            StringBuilder stringBuilder = new StringBuilder("https://");
            stringBuilder.append(bucketName).append(".").append(endpoint).append("/").append(objectName);
            log.info("文件上传到:{}", stringBuilder);
            return new UploadImageVO(stringBuilder.toString(),stringBuilder.toString());

    }

    @Override
    public String uploadFile(MultipartFile file, String dir) {
        return null;
    }

    @Override
    public Integer getType() {
        return FileStorageType.ALIYUNOSS.getCode();
    }
}
