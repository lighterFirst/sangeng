package com.cheng.controller.system;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.Menu;
import com.cheng.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult<Menu> listMenu(String status,String menuName){
        return menuService.listMenu(status,menuName);
    }

    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    /**
     * 回显菜单
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult showMenu(@PathVariable("id") Long id){
        return menuService.showMenu(id);
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenu(menuId);
    }

    /**
     * 获取菜单树接口
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult getTreeSelect(){
        return menuService.getTreeSelect();
    }

    /**
     * 2 加载对应角色菜单列表树接口
     * @param id
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getRoleMenuTree(@PathVariable("id") String id){

        return menuService.getRoleMenuTree(id);

    }
}
