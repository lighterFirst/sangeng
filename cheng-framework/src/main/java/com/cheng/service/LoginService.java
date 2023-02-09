package com.cheng.service;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.User;

public interface LoginService {

    ResponseResult loginUser(User user);

    ResponseResult logout();
}
