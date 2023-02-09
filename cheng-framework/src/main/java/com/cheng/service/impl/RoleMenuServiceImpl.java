package com.cheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.service.RoleMenuService;
import com.cheng.domain.entity.RoleMenu;
import com.cheng.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author cheng
 * @since 2022-12-23 13:16:18
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

