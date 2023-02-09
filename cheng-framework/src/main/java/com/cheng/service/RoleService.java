package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddRoleDto;
import com.cheng.domain.dto.ChangeRoleStatusDto;
import com.cheng.domain.dto.RoleDto;
import com.cheng.domain.dto.UpdateRoleDto;
import com.cheng.domain.entity.Role;
import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author cheng
 * @since 2022-12-23 11:01:47
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);


    ResponseResult getRoleList(Long pageNum, Long pageSize, RoleDto roleDto);

    ResponseResult changeRoleStatus(ChangeRoleStatusDto changeRoleStatusDto);

    ResponseResult addRole(AddRoleDto addRoleVo);

    ResponseResult showRole(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();
}

