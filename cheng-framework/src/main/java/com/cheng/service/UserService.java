package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddUserDto;
import com.cheng.domain.dto.ListUserDto;
import com.cheng.domain.entity.User;
import com.cheng.vo.UpdateUserVo;

/**
 * 用户表(User)表服务接口
 *
 * @author cheng
 * @since 2022-12-12 17:44:42
 */
public interface UserService extends IService<User> {


    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUser(Long pageNum, Long pageSize, ListUserDto listUserDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUser(Long id);

    ResponseResult showUser(Long id);

    ResponseResult updateUser(UpdateUserVo updateUserVo);
}

