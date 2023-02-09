package com.cheng.controller;

import com.cheng.annotation.SystemLog;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.User;
import com.cheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 查询用户信息
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //查询个人信息
    @GetMapping("/userInfo")
    public ResponseResult getUser(){

        return userService.getUserInfo();


    }

    //更新个人信息
    @PutMapping("/userInfo")
    @SystemLog(bussinessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    //注册用户信息
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
       return userService.register(user);
    }

}
