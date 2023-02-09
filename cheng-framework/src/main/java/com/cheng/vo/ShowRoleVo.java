package com.cheng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoleVo {

    private Long id;

    private String remark;

    private String roleKey;

    private String roleName;

    private Integer roleSort;

    private String status;

}
