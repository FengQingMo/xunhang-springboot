package com.xunhang.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxInfoDTO {

    //物品id
    private Long id;

    //认领人openid
    private String toUser;

    //认领人ID
    private Long userId;
    private Boolean category;
    private String thing;
    private String name;
    private String phone;
    private LocalDate date;
}
