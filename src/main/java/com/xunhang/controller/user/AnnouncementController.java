package com.xunhang.controller.user;


import com.xunhang.common.result.Result;
import com.xunhang.pojo.entity.Announcement;
import com.xunhang.service.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/getAnnouncement")
    public Result getContent(){
        log.info("获取公告内容，Controller");
        List<Announcement> announcementList = announcementService.getContent();
        List<String>content = new ArrayList<>();

        announcementList.forEach(announcement -> content.add(announcement.getContent()));

        return Result.success(content);
    }

}
