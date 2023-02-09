package com.cheng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriteCategoryVo {

    private Long id;

    private String name;

    private String description;

}
