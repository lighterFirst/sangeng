package com.cheng.controller;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.User;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.handle.exception.SystemException;
import com.cheng.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *
     * @param user 使用User类接收账号和密码
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){

        //验证用户名不能为空
        if(!StringUtils.hasText(user.getUserName())){
            throw  new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return blogService.loginUser(user);

    }

    /**
     * 登出用户
      */

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogService.logout();
    }


}







