package com.xunhang.fileStorage;

import com.xunhang.common.enums.FileStorageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 风清默
 * @date 2024/7/21 15:10
 * @Package com.xunhang.thirdparty.fileStorage
 * @description: 文件上传器，降级存储，优先使用Minio,顺序调整改变FileStorageType的顺序即可
 */
@Component
public class FileStorageLoadBalancer {
    private static final Map<Integer, IFileStorageService> myServiceCache = new HashMap<>();


    private static List<IFileStorageService> storageServices;

    @Autowired
    public FileStorageLoadBalancer(List<IFileStorageService> storageServices) {
        FileStorageLoadBalancer.storageServices = storageServices;
        for (IFileStorageService service : storageServices) {
            myServiceCache.put(service.getType(), service);
        }
    }

    /**
     * 获取具体文件存储服务
     * @return 文件存储服务
     */
    public static IFileStorageService getStorageService() {
        for(Integer code : FileStorageType.getAllCodes()){
            if(myServiceCache.get(code)!=null)
                return myServiceCache.get(code);
        }
        return null;
    }
}
