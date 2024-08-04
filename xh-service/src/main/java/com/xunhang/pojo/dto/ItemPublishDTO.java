package com.xunhang.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPublishDTO {


    private String description;//描述
    private String location;//地点
    private LocalDate  date;//日期

    @NotNull
    private Boolean category;//0：失物，1:拾取的物品

    private String tag;

    @NotNull(message = "标题不可为空")
    private String title;

    private List<String> images;

}
