package com.cheng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录给前端的vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogUserLoginVo {


    private String token;
    private UserInfoVo userInfo;

}
