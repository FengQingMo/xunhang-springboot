package com.xunhang.service.impl;


import com.xunhang.mapper.AnnouncementMapper;
import com.xunhang.pojo.entity.Announcement;
import com.xunhang.service.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnnouncementServiceimpl implements AnnouncementService {


    @Autowired
    private AnnouncementMapper announcementMapper;
    @Override
    public List<Announcement> getContent(){
        log.info("Service获取公告内容，");
       return announcementMapper.getContent();
    }
}
