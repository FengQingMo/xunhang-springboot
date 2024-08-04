package com.xunhang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemVO {

    private Long id;
    private Long publisherId;
    private String description;//描述
    private String location;//地点
    private LocalDate date;//日期
    private String claimerId;//认领者ID 0代表还没认领
    private Boolean isSuccess;
    private String tag;
    private String title;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> images;
    private UserVO userVO;
}
