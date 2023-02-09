package com.cheng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 为登录服务，是登录vo（BlogUserLoginVo）的子类，负责用户信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {

    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;

}
