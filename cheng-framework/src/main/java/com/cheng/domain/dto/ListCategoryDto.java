package com.cheng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListCategoryDto {

    //分类名
    private String name;

    //状态0:正常,1禁用
    private String status;

}
