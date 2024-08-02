package com.xunhang.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemChangeDTO {
    private Long id;
    private String description;//描述
    private String location;//地点
    private LocalDate date;//日期
    private Boolean category;//0：失物，1:拾取的物品

    private String tag;

    private String title;

    private List<String> images;
    private LocalDateTime updateTime;
}
