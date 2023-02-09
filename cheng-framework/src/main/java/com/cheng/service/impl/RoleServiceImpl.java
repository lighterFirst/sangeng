package com.cheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.constants.SystemConstants;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddRoleDto;
import com.cheng.domain.dto.ChangeRoleStatusDto;
import com.cheng.domain.dto.RoleDto;
import com.cheng.domain.dto.UpdateRoleDto;
import com.cheng.domain.entity.RoleMenu;
import com.cheng.service.RoleMenuService;
import com.cheng.service.RoleService;
import com.cheng.domain.entity.Role;
import com.cheng.mapper.RoleMapper;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.vo.PageVo;
import com.cheng.vo.ShowRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author cheng
 * @since 2022-12-23 11:01:47
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {

        //判断是否是管理员
        if(id == 1){
            List<String> roleList = getBaseMapper().selectRoleByUserId(id);
            roleList.add("admin");
        }

        //否则：查询用户所具有的的角色信息
        List<String> roleList = getBaseMapper().selectRoleByUserId(id);

        return roleList;
    }

    @Override
    public ResponseResult getRoleList(Long pageNum, Long pageSize, RoleDto roleDto) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(roleDto.getRoleName()),Role::getRoleName,roleDto.getRoleName());
        lambdaQueryWrapper.like(StringUtils.hasText(roleDto.getStatus()),Role::getStatus,roleDto.getStatus());
        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    @Override
    public ResponseResult changeRoleStatus(ChangeRoleStatusDto changeRoleStatusDto) {
        Role role = getById(changeRoleStatusDto.getRoleId());
        role.setStatus(changeRoleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDto addRoleDto) {

        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        List<RoleMenu> roleList = addRoleDto.getMenuIds().stream()
                .map(new Function<Long, RoleMenu>() {
                    @Override
                    public RoleMenu apply(Long menuId) {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setMenuId(menuId);
                        roleMenu.setRoleId(role.getId());
                        return roleMenu;
                    }
                })
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleList);
        return ResponseResult.okResult();
    }

    /**
     * 回显角色
     * @param id
     * @return
     */
    @Override
    public ResponseResult showRole(Long id) {
        Role role = getById(id);
        ShowRoleVo showRoleVo = BeanCopyUtils.copyBean(role, ShowRoleVo.class);
        return ResponseResult.okResult(showRoleVo);
    }

    @Override
    @Transactional
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
        List<RoleMenu> roleMenuList = updateRoleDto.getMenuIds().stream()
                .map(new Function<Long, RoleMenu>() {
                    @Override
                    public RoleMenu apply(Long menuId) {
                        RoleMenu roleMenu = new RoleMenu(role.getId(), menuId);
                        return roleMenu;
                    }
                })
                .collect(Collectors.toList());
        roleMenuService.removeById(role.getId());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteRole(Long id) {
        removeById(id);
        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(lambdaQueryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roleList = list(lambdaQueryWrapper);
        return ResponseResult.okResult(roleList);
    }
}















