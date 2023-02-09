package com.cheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.service.UserRoleService;
import com.cheng.domain.entity.UserRole;
import com.cheng.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author cheng
 * @since 2022-12-23 13:16:51
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

