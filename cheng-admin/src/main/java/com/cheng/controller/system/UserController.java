package com.cheng.controller.system;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddRoleDto;
import com.cheng.domain.dto.AddUserDto;
import com.cheng.domain.dto.ListUserDto;
import com.cheng.service.UserService;
import com.cheng.vo.UpdateUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult getUserList(Long pageNum, Long pageSize, ListUserDto listUserDto){

        return userService.listUser(pageNum,pageSize,listUserDto);

    }

    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseResult showUser(@PathVariable Long id){
        return userService.showUser(id);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserVo updateUserVo){
        return userService.updateUser(updateUserVo);
    }

}
