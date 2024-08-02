package com.xunhang.service;


import com.xunhang.pojo.entity.Announcement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnnouncementService {
    List<Announcement> getContent();
}
