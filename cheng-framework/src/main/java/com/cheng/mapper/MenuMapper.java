package com.cheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cheng.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author cheng
 * @since 2022-12-23 10:52:27
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllMenu();

    List<Menu> selectRouterMenuTreeByid(Long userId);
}
