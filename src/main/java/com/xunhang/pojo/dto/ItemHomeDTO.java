package com.xunhang.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemHomeDTO {

    private String text;

    private String tag;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "category cannot be empty")
    private  Boolean category;

    @NotNull(message = "page cannot be empty")
    private Integer page;

    @NotNull
    private Integer limit;

}
