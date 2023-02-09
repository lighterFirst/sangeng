package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author cheng
 * @since 2022-12-23 10:52:27
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult<Menu> listMenu(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult updateMenu(Menu menu);

    ResponseResult showMenu(Long id);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult getTreeSelect();

    ResponseResult getRoleMenuTree(String id);
}

