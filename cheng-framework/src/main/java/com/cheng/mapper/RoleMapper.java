package com.cheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cheng.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author cheng
 * @since 2022-12-23 11:01:47
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleByUserId(Long id);
}
