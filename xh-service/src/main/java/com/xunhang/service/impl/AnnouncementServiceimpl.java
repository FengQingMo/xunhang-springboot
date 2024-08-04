package com.xunhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.mapper.AnnouncementMapper;
import com.xunhang.pojo.entity.Announcement;
import com.xunhang.service.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnnouncementServiceimpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {


    @Autowired
    private AnnouncementMapper announcementMapper;
    @Override
    @Cacheable(value = "announcement",key = "getMethodName()")
    public List<String> selectTop3Contents() {
        return announcementMapper.selectTop3Contents();
    }
}
