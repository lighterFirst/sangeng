package com.cheng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagUpdateDto {

    private Long id;

    private String name;

    private String remark;


}
