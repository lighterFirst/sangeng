package com.cheng.controller.system;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.LoginUser;
import com.cheng.domain.entity.Menu;
import com.cheng.domain.entity.User;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.handle.exception.SystemException;
import com.cheng.service.LoginService;
import com.cheng.service.MenuService;
import com.cheng.service.RoleService;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.utils.RedisCache;
import com.cheng.utils.SecurityUtils;
import com.cheng.vo.AdminUserInfoVo;
import com.cheng.vo.RoutersVo;
import com.cheng.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){

        //验证用户名不能为空
        if(!StringUtils.hasText(user.getUserName())){
            throw  new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return loginService.loginUser(user);

    }

    /**
     * 动态路由
     */

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){

        //获取当前登录的用户

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        //根据用户id查询角色信息
        List<String> menuList = menuService.selectPermsByUserId(loginUser.getUser().getId());

        //根据用户id查询权限信息
        List<String> roleList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);

        return ResponseResult.okResult(new AdminUserInfoVo(menuList,roleList,userInfoVo));

    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){

        Long userId = SecurityUtils.getUserId();
        //查询menu结果是 tree的形式
        List<Menu> list = menuService.selectRouterMenuTreeByUserId(userId);

        return ResponseResult.okResult(new RoutersVo(list));

    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        //获取当前用户的id
        return loginService.logout();
    }


}
