package com.cheng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowMenuRoleVo {

    private List<ShowMenuVo> menus;

    private List<Long> checkedKeys;

}
