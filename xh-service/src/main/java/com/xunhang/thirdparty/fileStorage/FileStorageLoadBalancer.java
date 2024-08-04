package com.xunhang.thirdparty.fileStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 风清默
 * @date 2024/7/21 15:10
 * @Package com.xunhang.thirdparty.fileStorage
 * @description: 文件上传负载均衡
 */
@Component
public class FileStorageLoadBalancer {

    private static ConcurrentHashMap<Integer, Long> responseTimes;
    private static List<IFileStorageService> storageServices;


    @Autowired
    public FileStorageLoadBalancer(List<IFileStorageService> storageServices) {
        FileStorageLoadBalancer.storageServices = storageServices;
        responseTimes = new ConcurrentHashMap<Integer, Long>();
        for (int i = 0; i < storageServices.size(); i++) {
            responseTimes.put(storageServices.get(i).getType(), 0L);
        }
    }

    public static void setResponseTime(Integer type, Long responseTime) {
        responseTimes.put(type, responseTime);
    }

    public static IFileStorageService getStorageService() {
        IFileStorageService selectedService = null;
        Long minScore = Long.MAX_VALUE;
        for (IFileStorageService service : storageServices) {
            Integer type = service.getType();
            Long responseTime = responseTimes.getOrDefault(type, Long.MAX_VALUE);
            if (responseTime <= minScore) {
                minScore = responseTime;
                selectedService = service;
            }
        }
        return selectedService;
    }
}
