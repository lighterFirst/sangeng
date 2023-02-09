package com.cheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddUserDto;
import com.cheng.domain.dto.ListUserDto;
import com.cheng.domain.dto.ShowUserDto;
import com.cheng.domain.entity.LoginUser;
import com.cheng.domain.entity.Role;
import com.cheng.domain.entity.UserRole;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.handle.exception.SystemException;
import com.cheng.service.RoleService;
import com.cheng.service.UserRoleService;
import com.cheng.service.UserService;
import com.cheng.domain.entity.User;
import com.cheng.mapper.UserMapper;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.utils.SecurityUtils;
import com.cheng.vo.PageVo;
import com.cheng.vo.UpdateUserVo;
import com.cheng.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author cheng
 * @since 2022-12-12 17:45:14
 */
@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    //1:查询用户信息
    @Override
    public ResponseResult getUserInfo() {
        //获取用户当前的id
        Authentication authentication = SecurityUtils.getAuthentication();

        //根据用户id查询用户信息
       LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    //更新用户信息
    @Override
    public ResponseResult updateUserInfo(User user) {
        boolean flag = updateById(user);
        return ResponseResult.okResult();
    }

    //注册用户信息
    @Override
    public ResponseResult register(User user) {

        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICK_NAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(!judgeUserName(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if(!judgeEmail(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }


        //对密码进行加密
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    private boolean judgeUserName(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,userName);
        //相当于count * ，查询总记录条数
        long count = count(lambdaQueryWrapper);
        return count==0;
    }

    private boolean judgeEmail(String email) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail,email);
        //相当于count * ，查询总记录条数
        long count = count(lambdaQueryWrapper);
        return count==0;
    }

    //分页查询用户列表
    @Override
    public ResponseResult listUser(Long pageNum, Long pageSize, ListUserDto listUserDto) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(listUserDto.getPhonenumber()),User::getPhonenumber,listUserDto.getPhonenumber());
        lambdaQueryWrapper.like(StringUtils.hasText(listUserDto.getUserName()),User::getUserName,listUserDto.getUserName());
        lambdaQueryWrapper.eq(StringUtils.hasText(listUserDto.getStatus()),User::getStatus,listUserDto.getStatus());
        Page page = new Page(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    //新增用户
    @Override
    @Transactional
    public ResponseResult addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        //对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        List<Long> roleIds = addUserDto.getRoleIds();
        List<UserRole> userRoleList = roleIds.stream()
                .map(new Function<Long, UserRole>() {
                    @Override
                    public UserRole apply(Long roleId) {
                        UserRole userRole = new UserRole(user.getId(), roleId);
                        return userRole;
                    }
                })
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteUser(Long id) {
        removeById(id);
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,id);
        userRoleService.remove(lambdaQueryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showUser(Long id) {
        List<Role> roleList = roleService.list();
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,id);
        List<Long> roleIdList = userRoleService.list(lambdaQueryWrapper).stream()
                .map(new Function<UserRole, Long>() {
                    @Override
                    public Long apply(UserRole userRole) {
                        return userRole.getRoleId();
                    }
                })
                .collect(Collectors.toList());
        User user = getById(id);

        return ResponseResult.okResult(new ShowUserDto(roleIdList,roleList,user));
    }

    @Override
    @Transactional
    public ResponseResult updateUser(UpdateUserVo updateUserVo) {
        User user = BeanCopyUtils.copyBean(updateUserVo, User.class);
        updateById(user);
        List<Long> roleIds = updateUserVo.getRoleIds();
        List<UserRole> userRoleList = roleIds.stream()
                .map(roleId -> {
                    UserRole userRole = new UserRole(user.getId(), roleId);
                    return userRole;
                })
                .collect(Collectors.toList());
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(lambdaQueryWrapper);
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }
}

