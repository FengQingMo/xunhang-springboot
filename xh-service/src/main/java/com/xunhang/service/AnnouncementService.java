package com.xunhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhang.pojo.entity.Announcement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 查询前三条公告
     */
    List<String> selectTop3Contents();
}
