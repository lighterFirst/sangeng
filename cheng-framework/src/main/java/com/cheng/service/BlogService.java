package com.cheng.service;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.User;

public interface BlogService {

    ResponseResult loginUser(User user);

    ResponseResult logout();
}
