package com.xunhang.pojo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



public class ItemChangeDTO {
    private Long id;
    private String description;
    private String location;//地点
    private LocalDate date;//日期
    private Boolean category;//0：失物，1:拾取的物品

    private String tag;

    private String title;

    private List<String> images;
    private LocalDateTime updateTime;
}
