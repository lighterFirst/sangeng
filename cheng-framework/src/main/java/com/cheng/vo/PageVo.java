package com.cheng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装所有的分页对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {

    private List rows;

    private long total;


}
