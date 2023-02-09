package com.cheng.controller.system;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddRoleDto;
import com.cheng.domain.dto.ChangeRoleStatusDto;
import com.cheng.domain.dto.RoleDto;
import com.cheng.domain.dto.UpdateRoleDto;
import com.cheng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 查询角色列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getRoleList(Long pageNum, Long pageSize, RoleDto roleDto){
        return roleService.getRoleList(pageNum,pageSize,roleDto);
    }

    /**
     * 改变角色状态
     * @param changeRoleStatusDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changRoleStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        return roleService.changeRoleStatus(changeRoleStatusDto);

    }

    /**
     * 添加角色
     * @param addRoleDto
     * @return
     */
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    //修改角色有三个接口

    /**
     * 更新用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult showRole(@PathVariable("id") Long id){

        return roleService.showRole(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }

    /**
     * 查询角色列表接口
     * @return
     */

    @GetMapping("/listAllRole")
    public ResponseResult addRole(){

        return roleService.listAllRole();
    }


}












