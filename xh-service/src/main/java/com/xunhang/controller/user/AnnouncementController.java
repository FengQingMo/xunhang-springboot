package com.xunhang.controller.user;

import com.xunhang.common.result.Result;
import com.xunhang.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 查询前三条公告
     */
    @GetMapping()
    public Result selectTop3Contents() {
        return Result.success(announcementService.selectTop3Contents());
    }
}
