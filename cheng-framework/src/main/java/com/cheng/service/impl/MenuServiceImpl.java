package com.cheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.constants.SystemConstants;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.RoleMenu;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.handle.exception.SystemException;
import com.cheng.service.MenuService;
import com.cheng.domain.entity.Menu;
import com.cheng.mapper.MenuMapper;
import com.cheng.service.RoleMenuService;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.utils.SecurityUtils;
import com.cheng.utils.TreeUtils;
import com.cheng.vo.MenuVo;
import com.cheng.vo.ShowMenuRoleVo;
import com.cheng.vo.ShowMenuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author cheng
 * @since 2022-12-23 10:52:27
 */
@Service("menuService")
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有权限
        if (id == 1) {
            LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            lambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menuList = list(lambdaQueryWrapper);
            List<String> list = menuList.stream()
                    .map(new Function<Menu, String>() {
                        @Override
                        public String apply(Menu menu) {
                            return menu.getPerms();
                        }
                    }).collect(Collectors.toList());
            return list;
        }
        //否则返回其所具有的权限

        //根据用户和role关系表，查去roleId,再根据role和menu关系表，查权限（路由）
        List<String> menuList = getBaseMapper().selectPermsByUserId(id);


        //

        return menuList;
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {

        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        //如果是返回所有符合要求的Menu
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllMenu();
        }else{
            //否则返回当前用户所具有的menu
            menus = menuMapper.selectRouterMenuTreeByid(userId);
        }

        //构建tree
        List<Menu> menuTree = buildMenuTree(menus, 0L);
        return menuTree;
    }

    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {

        //先找出第一层的菜单，然后去找他们的子菜单设置到children属性中。
        List<Menu> menuList = menus.stream()
                .filter(new Predicate<Menu>() {
                    @Override
                    public boolean test(Menu menu) {
                        return menu.getParentId().equals(parentId);
                    }
                })
                .map(menu -> menu.setChildren(getChildren(menus, menu)))
                .collect(Collectors.toList());


        return menuList;
    }

    /**
     * 拿到父节点的子节点集合
     * @param menus  总集合
     * @param menu   父节点
     * @return
     */

    private static List<Menu> getChildren(List<Menu> menus, Menu menu) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> menu.getId().equals(m.getParentId()))
                .map(m -> m.setChildren(getChildren(menus,m)))
                .collect(Collectors.toList());

        return childrenList;

    }

    /**
     * 菜单管理，查询所有菜单
     * @param status
     * @param menuName
     * @return
     */

    @Override
    public ResponseResult<Menu> listMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        lambdaQueryWrapper.eq(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        lambdaQueryWrapper.orderByAsc(Menu::getId,Menu::getOrderNum);
        List<Menu> list = list(lambdaQueryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);

        return ResponseResult.okResult(list);
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }


    /**
     * 回显菜单
     */
    @Override
    public ResponseResult showMenu(Long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @Override
    public ResponseResult updateMenu(Menu menu) {
        if(menu.getId().equals(menu.getParentId())){
            throw new SystemException(AppHttpCodeEnum.MENU_NAME_EQUALS_PARENT);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> list = list(lambdaQueryWrapper);
        if (Objects.nonNull(list)){
            return ResponseResult.errorResult((AppHttpCodeEnum.MENU_EXIST_CHILDREN),"存在子菜单不能删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult getTreeSelect() {
        List<ShowMenuVo> menuVoList = showTreeList();
        return ResponseResult.okResult(menuVoList);
    }

    @Override
    public ResponseResult getRoleMenuTree(String id) {

        List<RoleMenu> roleMenuList = null;
        List<Long> menuIdList = null;
        List<ShowMenuVo> menuVoList = showTreeList();
        if(id != null && "1".equals(id) ){
            List<Menu> menuList = list();
             menuIdList = menuList.stream()
                    .map(new Function<Menu, Long>() {
                        @Override
                        public Long apply(Menu menu) {
                            return menu.getId();
                        }
                    })
                    .collect(Collectors.toList());
        }else{
            LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(RoleMenu::getRoleId, id);
            roleMenuList = roleMenuService.list(lambdaQueryWrapper);
            menuIdList = roleMenuList.stream()
                    .map(new Function<RoleMenu, Long>() {
                        @Override
                        public Long apply(RoleMenu roleMenu) {
                            return roleMenu.getMenuId();
                        }
                    })
                    .collect(Collectors.toList());
        }
        return ResponseResult.okResult(new ShowMenuRoleVo(menuVoList,menuIdList));
    }

    public List<ShowMenuVo> showTreeList(){
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = menuMapper.selectAllMenu();
        List<ShowMenuVo> showMenuVos = BeanCopyUtils.copyBeanList(menus, ShowMenuVo.class);
        List<ShowMenuVo> menuVoList = showMenuVos.stream()
                .map(new Function<ShowMenuVo, ShowMenuVo>() {
                    @Override
                    public ShowMenuVo apply(ShowMenuVo showMenuVo) {
                        showMenuVo.setLabel(showMenuVo.getMenuName());
                        return showMenuVo;
                    }
                })
                .collect(Collectors.toList());
        List<ShowMenuVo> list = TreeUtils.generateTrees(menuVoList);
        return list;
    }

}

